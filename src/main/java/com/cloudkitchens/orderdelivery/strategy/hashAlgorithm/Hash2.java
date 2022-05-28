package com.cloudkitchens.orderdelivery.strategy.hashAlgorithm;

import com.cloudkitchens.orderdelivery.enums.HashImplEnum;

public class Hash2 implements IHash{
    @Override
    public String getImplName() {
        return HashImplEnum.HASHIMPL2.name();
    }

    @Override
    public int orderIdHash(String orderId) {

        return 0;
    }
}