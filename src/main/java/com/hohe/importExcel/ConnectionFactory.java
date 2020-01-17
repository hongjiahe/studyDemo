package com.hohe.importExcel;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConnectionFactory {
	
	private static ConnectionPool conn=null;	
	
	public static ConnectionPool getConnectionPool(){
		if(conn==null ){
				Integer maxPoolSize=Integer.valueOf(ScriptActionFactory.p.getProperty("jdbc.maxConnectionCount"));
				Integer minPoolSize=Integer.valueOf(ScriptActionFactory.p.getProperty("jdbc.minConnectionCount"));
				Integer acquireIncrement=Integer.valueOf(ScriptActionFactory.p.getProperty("jdbc.acquire.increment"));
				String user=ScriptActionFactory.p.getProperty("jdbc.username");
				String password=ScriptActionFactory.p.getProperty("jdbc.password");
				String jdbcUrl=ScriptActionFactory.p.getProperty("jdbc.url");
				String driverClass=ScriptActionFactory.p.getProperty("jdbc.driverClassName");
				conn=new ConnectionPool(driverClass, jdbcUrl, user, password, maxPoolSize, minPoolSize, acquireIncrement);
		}
	    return conn;  
	}

}
