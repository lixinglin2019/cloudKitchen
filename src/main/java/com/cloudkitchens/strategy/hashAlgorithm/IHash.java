package com.cloudkitchens.strategy.hashAlgorithm;

public interface IHash {
    public String getImplName();
    int orderIdHash(String orderId);
}
