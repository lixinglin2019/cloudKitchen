package com.cloudkitchens.strategy.hashAlgorithm;

import com.cloudkitchens.enums.HashImplEnum;

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