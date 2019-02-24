package com.hohe.mapper;

import com.hohe.model.OperationLog;

public interface OperationLogMapper {

    int deleteByPrimaryKey(Integer id);
    int insert(OperationLog record);
    int insertSelective(OperationLog record);
    OperationLog selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(OperationLog record);
    int updateByPrimaryKeyWithBLOBs(OperationLog record);
    int updateByPrimaryKey(OperationLog record);

}