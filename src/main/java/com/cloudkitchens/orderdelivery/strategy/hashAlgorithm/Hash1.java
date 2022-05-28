package com.cloudkitchens.orderdelivery.strategy.hashAlgorithm;

import com.cloudkitchens.orderdelivery.enums.HashImplEnum;

public class Hash1 implements IHash{
    @Override
    public String getImplName() {
        return HashImplEnum.HASHIMPL1.name();
    }

    /**
     * @param orderId 这里的orderId是java自带的UUID.randomUUID()生成的  like this:116f321d-4f7b-493c-ac2c-53aab3db6bec
     * @return
     */
    @Override
    public int orderIdHash(String orderId) {

        return 0;
    }
}
