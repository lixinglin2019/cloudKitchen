package com.cloudkitchens.orderdelivery.dao;

import com.cloudkitchens.orderdelivery.domain.Kitchen;

import javax.annotation.Resource;

@Resource
public class CloudKitchenDao implements BaseDao<Kitchen>{


    @Override
    public int save(Kitchen object) {
        return 0;
    }

    @Override
    public int delete(Kitchen object) {
        return 0;
    }

    @Override
    public int update(Kitchen object) {
        return 0;
    }

    @Override
    public Kitchen findById(String id) {
        return null;
    }
}
