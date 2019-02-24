package com.hohe.service.impl;

import com.hohe.mapper.OperationLogMapper;
import com.hohe.model.OperationLog;
import com.hohe.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "operationLogService")
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public int addOperationLog(OperationLog operationLog) {
        return operationLogMapper.insert(operationLog);
    }
}
