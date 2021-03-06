package com.hohe.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.web.util.WebUtils;


/**
 * Web层辅助类
 * 
 */
public final class WebUtil {
    private WebUtil() {
    }

    /**
     * 获取指定Cookie的值
     * 
     * @param cookieName cookie名字
     * @param defaultValue 缺省值
     * @return
     */
    public static final String getCookieValue(HttpServletRequest request, String cookieName, String defaultValue) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie == null) {
            return defaultValue;
        }
        return cookie.getValue();
    }
    
    /**
	 * 删除一个cookie
	 * @param cookieName
	 */
	public static final void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName){
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		if(cookie != null){
			cookie.setMaxAge(0); // 设置cookie的存活时间为0秒,即马上失效
			cookie.setPath("/"); // path:  localhost:8080/xxx 代表localhost:8080/xxx/所有访问路径都有效
			response.addCookie(cookie);
		}
	}
	
	/**
	 * 删除hoolinks.com cookie
	 * @param cookieName
	 */
	public static final void deleteHoolinksDotComCookie(HttpServletRequest request, HttpServletResponse response, String cookieName){
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		if(cookie != null){
			cookie.setDomain("hoolinks.com");
			cookie.setMaxAge(0); // 设置cookie的存活时间为0秒,即马上失效
			cookie.setPath("/"); // path:  localhost:8080/xxx 代表localhost:8080/xxx/所有访问路径都有效
			response.addCookie(cookie);
		}
	}

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     */
    public static final void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     */
    public static final void setSession(HttpServletRequest request, String key, Object value) {
        HttpSession session = request.getSession();
        if (null != session) {
            session.setAttribute(key, value);
        }
    }

    /**
     * 获得国际化信息
     * 
     * @param key 键
     * @param request
     * @return
     */
    public static final String getApplicationResource(String key, HttpServletRequest request) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources", request.getLocale());
        return resourceBundle.getString(key);
    }

    /**
     * 获得参数Map
     * 
     * @param request
     * @return
     */
    public static final Map<String, Object> getParameterMap(HttpServletRequest request) {
        return WebUtils.getParametersStartingWith(request, null);
    }

    /** 获取客户端IP */
    public static final String getHost(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip.equals("0:0:0:0:0:0:0:1")){
        	ip = "127.0.0.1";
        }
        if ("127.0.0.1".equals(ip)) {
            InetAddress inet = null;
            try { // 根据网卡取本机配置的IP
                inet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if(inet !=null){
            	ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
    
    public static SavedRequest getSavedRequest() {
    	SavedRequest savedRequest = (SavedRequest)SecurityUtils.getSubject()
    			.getSession().getAttribute("shiroSavedRequest"); 
    	return savedRequest;
    } 
}
