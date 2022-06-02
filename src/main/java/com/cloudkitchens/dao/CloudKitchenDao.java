package com.cloudkitchens.dao;

import com.cloudkitchens.domain.KitchenInstance;
import org.springframework.stereotype.Component;

@Component
public class CloudKitchenDao implements BaseDao<KitchenInstance>{


    @Override
    public int save(KitchenInstance object) {
        return 0;
    }

    @Override
    public int delete(KitchenInstance object) {
        return 0;
    }

    @Override
    public int update(KitchenInstance object) {
        return 0;
    }

    @Override
    public KitchenInstance findById(String id) {
        return null;
    }
}
