package com.hohe.importExcel;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class ConnectionPool {
	
	private static Logger _log = LoggerFactory.getLogger(ConnectionPool.class);
	
	private ComboPooledDataSource _cpds;

	public ConnectionPool(String driverClass, String jdbcUrl, String user,
			String password, int maxPoolSize, int minPoolSize,
			int acquireIncrement) {
		String s=System.getProperty("db.acquired.retry.attemps");
		int ara=5;
		if(s!=null&&!s.isEmpty()){
			ara=new Integer(s).intValue();
		}
		try {
			_cpds = new ComboPooledDataSource();
			
			_cpds.setDriverClass(driverClass);
			_cpds.setJdbcUrl(jdbcUrl);
			_cpds.setUser(user);
			_cpds.setPassword(password);
			//_cpds.setMaxStatementsPerConnection(100);
			_cpds.setMinPoolSize(minPoolSize);
			_cpds.setMaxPoolSize(maxPoolSize);
			_cpds.setAcquireIncrement(acquireIncrement);
			_cpds.setAcquireRetryAttempts(ara);
			
			_cpds.setIdleConnectionTestPeriod(60);//每60秒检查一下连接
			_cpds.setMaxIdleTime(120);//连接最大空闲
			
//			_cpds.setTestConnectionOnCheckin(true);
		} catch (Exception e) {
			_log.error(e.getMessage(),e);
		}
	}
	public void _destroy() throws SQLException {
		DataSources.destroy(_cpds);
	}
	public Connection getConnection() throws SQLException {
		return _cpds.getConnection();
	}
	public DataSource getDataSource(){
		return _cpds;
	}

}
