package com.hohe.mysqlReadWrite;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.master")
   public DataSource masterDataSource(){
       DataSource build = DataSourceBuilder.create().build();
        return build;
   }


    @Bean
    @ConfigurationProperties("spring.datasource.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().build();
    }


    public DataSource myRoutingDataSource(@Qualifier("master") DataSource master,  @Qualifier("slave1") DataSource slave1){
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceEnum.MASTER.getCode(), master);
        targetDataSource.put(DataSourceEnum.SLAVE1.getCode(), slave1);

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(master);
        myRoutingDataSource.setTargetDataSources(targetDataSource);
        return myRoutingDataSource;
    }




}
