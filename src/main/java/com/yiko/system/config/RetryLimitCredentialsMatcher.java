package com.yiko.system.config;

import com.yiko.common.config.ApplicationContextRegister;
import com.yiko.ss.domain.User;
import com.yiko.system.dao.UserDao;
import com.yiko.system.domain.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    SessionDAO sessionDAO;

    //集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
    private Cache<String, AtomicInteger> lgoinRetryCache;

    private int maxRetryCount = 5;

    private String lgoinRetryCacheName;

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public RetryLimitCredentialsMatcher(CacheManager cacheManager, String lgoinRetryCacheName) {
        this.lgoinRetryCacheName = lgoinRetryCacheName;
        lgoinRetryCache = cacheManager.getCache(lgoinRetryCacheName);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        Map<String, Object> map = new HashMap<>(16);
        map.put("username", username);
        String password = new String((char[]) token.getCredentials());

        UserDao userMapper = ApplicationContextRegister.getBean(UserDao.class);
        List<UserDO> users = userMapper.list(map);
        UserDO user = null;
        if (!users.isEmpty()) {
            user = userMapper.list(map).get(0);
        }
        //声明一个UserDO用来获取sessionDao中的user
        UserDO userSession = null;
        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号不存在");
        }


        // 密码错误
        if (!password.equals(user.getPassword())) {

            //retry count + 1
            AtomicInteger retryCount = lgoinRetryCache.get(username);
            if (null == retryCount) {
                retryCount = new AtomicInteger(0);
                lgoinRetryCache.put(username, retryCount);
            }
            if (retryCount.incrementAndGet() > 5) {
                log.warn("username: " + username + " tried to login more than 5 times in period");
                user.setStatus(0);
                userMapper.update(user);
//                throw new ExcessiveAttemptsException("该账号已被锁定，请联系管理员");
            } else {
                throw new IncorrectCredentialsException("账号或密码不正确");
            }
        }


        //boolean matches = super.doCredentialsMatch(token, info);


        boolean matches = user.getPassword().equals(password);
        if (matches) {
            //clear retry data
//            lgoinRetryCache.remove(username);
            lgoinRetryCache.clear();
            Collection<Session> sessionCollection = sessionDAO.getActiveSessions();
            for (Session session : sessionCollection) {
                //session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY); 获取simpleAuthenticationInfo的第一个参数的值
                if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
                    //根据session build出一个subject
                    Subject subject = new Subject.Builder().session(session).buildSubject();
                    //拿到这个登陆的对象
                    userSession = (UserDO) subject.getPrincipal();
                    if (user.getUserId() == userSession.getUserId()) {
                        //两者一致的时候，设置这个session的失效时间 （0：立刻）
                        //session.stop();
                        sessionDAO.delete(session);
                        //throw new UnknownAccountException("该账号已被登陆");
                    }
                }
            }
        }

        // 账号锁定
        if (user.getStatus() == 0) {
//            if (null == lgoinRetryCache.get(username)) {
//                user.setStatus(1);
//                userMapper.update(user);
//            } else {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
//            }
        }


        return matches;
    }
}