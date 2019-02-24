package com.hohe.log.aop;


import com.alibaba.fastjson.JSON;
import com.hohe.log.annotation.SysOperationLog;
import com.hohe.model.OperationLog;
import com.hohe.service.OperationLogService;
import com.hohe.util.WebUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @ClassName SysOperationLogAspect
 * @Description 操作日志切面拦截处理
 * @author
 *
 */
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class SysOperationLogAspect {

    Logger logger = LoggerFactory.getLogger(SysOperationLogAspect.class);

    @Autowired
    private OperationLogService operationLogService;

    @Pointcut("@annotation(com.hohe.log.annotation.SysOperationLog)") //这里使用的是注解的方式, 也可以使用 execution(public * *(..)) 的方式来进行切入
    public void pointCut() {}

    /**
     *
     * @Title around
     * @Description 方法执行后拦截
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) throws Throwable {
        try {
            Class<?> classTarget = joinPoint.getTarget().getClass();
            Logger logger = LoggerFactory.getLogger(classTarget);

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = methodSignature.getMethod();

            Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            Method implMethod = classTarget.getMethod(targetMethod.getName(), parameterTypes);

            //获取调用方法
            SysOperationLog methodLogConfig = implMethod.getAnnotation(SysOperationLog.class);

            // 设置操作日志
            String functionName = "";
            if (methodLogConfig != null) {
                functionName = methodLogConfig.functionName();
            }
            // 操作日志记录

            OperationLog operationLog = new OperationLog();
            operationLog.setCompanyCode("1111");
            operationLog.setFunctionName(functionName);
            operationLog.setOperateDate(new Date());

            /*entity.setCurrentVersion(null);
            entity.setFunctionName(functionName);
            entity.setOperateDate(new Date());
            entity.setOperator(SessionUtil.getloginUserAccountName());
            entity.setCompanyCode(SessionUtil.getCompanyCode());*/
            Map<String, Object> params = getMethodParams(joinPoint);
            String jsonParams = JSON.toJSONString(params);
            if(logger.isDebugEnabled()){
                logger.debug("{}接口方法{}的参数{}", new Object[]{functionName, targetMethod.getName(), jsonParams});
            }
            operationLog.setParameters(jsonParams);
            operationLog.setUrl(getUrl());
            operationLog.setIp(getIp());
            operationLogService.addOperationLog(operationLog);

            if(logger.isDebugEnabled()){
                logger.debug("切面记录功能操作日志成功！");
            }
        } catch (Throwable e) {
            logger.error("切面记录功能操作日志失败:", e);
        }
    }

    /**
     * 获取方法参数
     *
     * @Title: getMethodParams
     * @param @param joinPoint
     * @param @return  参数说明
     * @throws
     */
    private Map<String, Object> getMethodParams(JoinPoint joinPoint) {
        Object[] paramValues = joinPoint.getArgs();
        CodeSignature codeSignature= (CodeSignature) joinPoint.getSignature();
        String[] paramNames = codeSignature.getParameterNames();

        // Class<?>[] paramNames = targetMethod.getParameterTypes();

        // 获取方法参数
        Map<String, Object> params = new HashMap<String, Object>();

        int i = 0;
        for (String paramName : paramNames) {
            if("response".equals(paramName) || "request".equals(paramName) || paramName.startsWith("model")){
                i++;
                continue;
            }
            params.put(paramName, paramValues[i]);
            i++;
        }

        return params;
    }

    /**
     * 获取客服登录账号
     * @return
     */
    private String getPlatformLoginId() {
        /*SysUser userVo = (SysUser) SessionUtil.getCurrentUser();
        return userVo != null ? userVo.getPlatformLoginId():null;*/

        return "account";
    }

    /**
     *
     * @Title getUrl
     * @Description 获取用户使用的IP
     * @return
     */
    private String getIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return WebUtil.getHost(request);
    }

    /**
     *
     * @Title getUrl
     * @Description 获取用户使用的IP
     * @return
     */
    private String getUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getRequestURL().toString();
    }

}
