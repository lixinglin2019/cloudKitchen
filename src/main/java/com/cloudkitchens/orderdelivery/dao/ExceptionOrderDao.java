package com.cloudkitchens.orderdelivery.dao;

import com.cloudkitchens.orderdelivery.domain.order.ExceptionOrder;

import javax.annotation.Resource;

@Resource
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
