package com.yiko.system.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_short_message")
public class ShortMessage {
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "message_code")
    private String messageCode;

    @Column(name = "ip_address")
    private String ipAddress;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * @return create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    /**
     * @return message_code
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * @param messageCode
     */
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode == null ? null : messageCode.trim();
    }

    /**
     * @return ip_address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }
}