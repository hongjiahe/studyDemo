package com.hohe.mysqlReadWrite;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAop {

    @Pointcut("!@annotation(com.hohe.mysqlReadWrite.Master) " +
            "&& (execution(* com.hohe.service..*.select*(..)) " +
            "|| execution(* com.hohe.service..*.get*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.hohe.mysqlReadWrite.Master) " +
            "|| execution(* com.hohe.service..*.insert*(..)) " +
            "|| execution(* com.hohe.service..*.add*(..)) " +
            "|| execution(* com.hohe.service..*.update*(..)) " +
            "|| execution(* com.hohe.service..*.edit*(..)) " +
            "|| execution(* com.hohe.service..*.delete*(..)) " +
            "|| execution(* com.hohe.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }


    /**
     * 另一种写法：if...else...  判断哪些需要读从数据库，其余的走主数据库
     */
//    @Before("execution(* com.hohe.service.impl.*.*(..))")
//    public void before(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//
//        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
//            DBContextHolder.slave();
//        }else {
//            DBContextHolder.master();
//        }
//    }

}
