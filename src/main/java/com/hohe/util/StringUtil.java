package com.hohe.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class StringUtil {
	

    /**
     *将 1,2,3,4 装换成List<Integer>
     * */
	public static List<Integer> getIntegerListByString(String str){
        String[] strIds =  str.split(",");
        List<String> stringList = Arrays.asList(strIds);
        List<Integer> integerList = new ArrayList<Integer>();
        for(String s : stringList){
            try {
                integerList.add(Integer.valueOf(s));
            }catch (Exception ex){}
        }
        return integerList;
    }

	/**
	 * 去掉与JSON相冲的符号
	 *@param
	 */
	public static String getSplit(String val){
		if(val!=null && !"".equals(val)){
			val = val.replaceAll(",", "，");
			val = val.replaceAll(":", "：");
			val = val.replaceAll(";", "；");
			val = val.replaceAll("&nbsp；", " ");
			return val;
		}else{
			return "";
		}
	}
	public static String replaceSepChar(String str){
		String s = null;
		if(!isBlank(str)){
			s = str.replace("<", "&lt;").replace(">","&gt;");
		}
		return s;
	}
	
	/**
	 * 验证联系人座机格式，形如xxx-xxxxxxx(x)-xxx|xxx-xxxxxxx(x)|xxxxxxx(x)
	 * @param
	 * @return
	 */
	public static boolean validateLinkerTel(String linkerTel){
		String regex = "^\\s*[0-9]{7,8}\\s*$|^\\s*[0]{1}[0-9]{2,3}-[0-9]{7,8}\\s*$|^\\s*[0]{1}[0-9]{2,3}-[0-9]{7,8}-[0-9]{3}\\s*$";
		boolean isValid = Pattern.compile(regex).matcher(linkerTel).find();
		
		return isValid;
	}
	
	/**
	 * 将传入的字符串形式的浮点数小数点左移两位
	 * @param rateStr
	 * @return
	 */
	public static String left2DecimalPoint(String rateStr) {
		try {
			new Double(rateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}

		StringBuffer value = new StringBuffer();
		if (rateStr.indexOf(".") != -1) {
			String[] tmps = rateStr.split("\\.");
			value.append("0.");
			if (tmps[0].length() == 1) {
				value.append(0).append(tmps[0]);
			}
			if (tmps[0].length() == 2) {
				value.append(tmps[0]);
			}
			value.append(tmps[1]);
		} else {
			value.append("0.");
			if (rateStr.length() == 1) {
				value.append(0).append(rateStr);
			}
			if (rateStr.length() == 2) {
				value.append(rateStr);
			}
		}
		if (value.toString().length() > 6)
			return value.toString().substring(0, 6);
		return value.toString();
	}
	
	public static boolean isBlank(String str) {
		return str == null || str.equals("");
	}
	public static boolean isNullOr0(Integer i) {
		return i == null || i.intValue()==0;
	}
	public static boolean isNullOr0(Double i) {
		return i == null || (i-0)<=0;
	}
	
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

    public static byte[] getByteByStr(String value){
        if(value == null){
            return null;
        }else{
            try {
                return value.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getStrByByte(byte[] bytes){
        try {
            return bytes == null ? "" : new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String,String> getParams(HttpServletRequest request){
        Map<String,String> params = new HashMap<String,String>();
        //获得POST 过来参数设置到新的params中
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = request.getParameterValues(name);
            StringBuffer valueStr =new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                valueStr.append((i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",");
            }
            params.put(name, valueStr.toString());
        }
        return params;
    }

    public static String geOrderContStr(byte[] bytes){
            return bytes == null ? "" : new String(bytes);
    }
    
    public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
    
    public static int length(String value) {
    	 
        int valueLength = 0;
        
        //去掉链接再计算
	      
        String regex="\\bhttps?://[a-zA-Z0-9\\-.]+(?::(\\d+))?(?:(?:/[a-zA-Z0-9\\-._?,'+\\&%$=~*!():@\\\\]*)+)?";
        Pattern pt=Pattern.compile(regex);
        Matcher mt=pt.matcher(value);
        String h="";
        List<String> rwList=new ArrayList<String>();
        while(mt.find())
        {
        	rwList.add(mt.group());
        }
        for (String rw : rwList) {
        	value=value.replace(rw, "").replace("链接：", "");
		}
 
        String chinese = "[\u0391-\uFFE5]";
 
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
 
        for (int i = 0; i < value.length(); i++) {
 
            /* 获取一个字符 */
 
            String temp = value.substring(i, i + 1);
 
            /* 判断是否为中文字符 */
 
            if (temp.matches(chinese)) {
 
                /* 中文字符长度为2 */
 
                valueLength += 2;
 
            } else {
 
                /* 其他字符长度为1 */
 
                valueLength += 1;
 
            }
 
        }
 
        return valueLength/2;
 
    }
    
    /** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;  
        if(str.length()<11){
        	return b;
        }
        p = Pattern.compile("^[1][0-9][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
    
    static final Random fRandom = new Random(System.currentTimeMillis());

	public static synchronized String generateGUID()
	{
	    long time = System.currentTimeMillis();
	    String str = Long.toHexString(time);
	    float randFloat = fRandom.nextFloat();
	    if(randFloat < 0.42F)
	        randFloat += 0.42F;
	    int i = (int)(randFloat * 10000F);
	    String str1 = Integer.toHexString(i);
	    return str + str1;
	}
	/** 截取指定字符串的前n个字符  **/
    public static String getLimitLengthStr(String source, int length){
    	String result = "";
    	if(isNotBlank(source) && length>0){
    		if(source.length()>length){
    			result = source.substring(0, length)+"...";
    		}else{
    			result = source;
    		}
    	}
    	return result;
    }
    
    /** 获取Map中指定Key对应Value的值  **/
    public static String getValueFormMap(Map<String, Object> map, String key){
		String result = "";
		if(map!=null && !map.isEmpty() && key!=null){
			if(map.containsKey(key)){
				Object oo = map.get(key);
				if(oo!=null){
					String temp = (String) oo;
					if(temp!=null){
						return temp;
					}
				}
			}
		}
		return result;
	}
}
