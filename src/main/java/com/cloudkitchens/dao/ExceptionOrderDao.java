package com.cloudkitchens.dao;

import com.cloudkitchens.domain.order.ExceptionOrder;
import org.springframework.stereotype.Component;

@Component
public class ExceptionOrderDao implements BaseDao<ExceptionOrder> {

    @Override
    public int save(ExceptionOrder object) {
        return 0;
    }

    @Override
    public int delete(ExceptionOrder object) {
        return 0;
    }

    @Override
    public int update(ExceptionOrder object) {
        return 0;
    }

    @Override
    public ExceptionOrder findById(String id) {
        return null;
    }
}
