package com.yiko.common.utils.poi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 该类用于Date对象和String之间相互转换
 * @author wu199406
 *
 */
public class DateAndString 
{
	//yyyy-MM-dd HH:mm:ss的正则表达式
	private static String duan = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}$";
	private static String duanStr = "yyyy-MM-dd HH:mm:ss";
	
	//yyyy/MM/dd HH:mm:ss的正则表达式
	private static String duan1 = "^[0-9]{4}[/][0-9]{2}[/][0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}$";
	private static String duanStr1 = "yyyy/MM/dd HH:mm:ss";
	
	//yyyy-MM-dd的正则表达式
	private static String chang = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
	private static String changStr = "yyyy-MM-dd";
	
	//yyyy/MM/dd的正则表达式
	private static String chang1 = "^[0-9]{4}[/][0-9]{2}[/][0-9]{2}$";
	private static String changStr1 = "yyyy/MM/dd";
	
	//EEE MMM dd HH:mm:ss Z yyyy的正则表达式
	private static String zuiChang = "^\\S{3}\\s\\S{3}\\s[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}\\s\\S{3}\\s[0-9]{4}$";
	private static String zuiChangStr = "EEE MMM dd HH:mm:ss Z yyyy";
	
	/**
	 * 将输入的格式为source的字符串dateString转换为对应的Date对象
	 * @param source	源字符串的格式，即dateString的格式
	 * @param dateString	要转换的日期字符串
	 * @return
	 */
	public static Date StringToDate(String source,String dateString)
	{
		 SimpleDateFormat sdf1 = new SimpleDateFormat (source, Locale.UK);
		 
		 try 
		 {
			Date date=sdf1.parse(dateString);
			
			return date;
		 } 
		 catch (ParseException e) 
		 {
			System.out.println("DateAndString---StringToDate--错误：将日期字符串转换为Date对象错误");
			e.printStackTrace();
		 }
		 
		 return null;
	}
	
	/**
	 * 自动将日期参数字符串转换为日期对象，在方法中会自动获取日期参数字符串相应的日期格式，如果日期参数字符串不满足
	 * 一定的格式要求，将会返回null
	 * 
	 * @param source  要转换的日期参数字符串，会去除首尾的空格
	 * 
	 * @return  成功，返回Date对象；失败返回null
	 */
	public static Date autoStringToDate(String source)
	{	
		source = source.trim();//去除首尾的空格
		
		String format = getDateFormatOfString(source);//获取输入字符串的日期格式
		
		try 
		 {
			if( format != null && !"".equals(format) )
			{
				SimpleDateFormat sdf1 = new SimpleDateFormat(format,Locale.UK);
				
				Date date=sdf1.parse(source);
				return date;
			}		
		 } 
		 catch (ParseException e) 
		 {
			System.out.println("要转换的日期格式:"+format+"---要转换的字符串:"+source);
			System.out.println("DateAndString---StringToDate--错误：将日期字符串转换为Date对象错误");
			e.printStackTrace();
		 }
		return null;
	}
	
	/**
	 * 自动将日期参数字符串转换为日期对象，在方法中会自动获取日期参数字符串相应的日期格式
	 * 
	 * 该方法与autoStringToDate方法的不同之处在于:日期参数字符串不正确时，会主动抛出异常；
	 * 
	 * @param source  要转换的日期字符串，会去除首尾的空格
	 * 
	 * @return  成功，返回Date对象；失败返回null
	 * @throws ParseException 
	 * @throws Exception 
	 */
	@SuppressWarnings("null")
	public static Date autoStringToDate2(String source) throws ParseException
	{	
		source = source.trim();//去除首尾的空格
		
		String format = getDateFormatOfString(source);//获取输入字符串的日期格式
		
		
			if( format != null && !"".equals(format) )
			{	
					SimpleDateFormat sdf1 = new SimpleDateFormat(format,Locale.UK);
					
					Date date=sdf1.parse(source);
					return date;
			}
			else if( source != null || !"".equals(source.trim()) )
			{
				throw new ParseException("日期字符串的格式不正确，请修改!",0);
			}
			return null;
	}
	
	/**
	 * 判读参数字符串是否满足日期格式，如果满足就返回相应的日期格式字符串。
	 * 
	 * @param str  要判断的字符串
	 * 
	 * @return  成功，返回对应的日期格式字符串；失败，返回null;
	 */
	public static String getDateFormatOfString(String str)
	{
		if(Pattern.matches(duan, str))
		{
			return duanStr;
		}
		if(Pattern.matches(duan1, str))
		{
			return duanStr1;
		}
		if(Pattern.matches(chang, str))
		{
			return changStr;
		}
		if(Pattern.matches(chang1, str))
		{
			return changStr1;
		}
		if(Pattern.matches(zuiChang, str))
		{
			return zuiChangStr;
		}
	    
		return null;
	}
	
	/**
	 * 将日期对象转换为指定的格式的字符串
	 * 
	 * @param date	要转换的日期对象
	 * @param format	要转换的对象，默认为yyyy-mm-dd
	 * 
	 * @return  成功，返回日期字符串；失败，返回null;
	 */
	public static String DateToString(Date date,String format)
	{
		if( date == null )
		{
			return null;
		}
		
		if( format == null || "".equals(format.replaceAll("\\s*", "")) )
		{
			format = "yyyy-MM-dd";
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		
		try 
		{
			String str = simpleDateFormat.format(date);
			return str;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
