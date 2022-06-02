package com.cloudkitchens.enums;

public enum HashImplEnum {
    HASHIMPL1("hasimpl1"),
    HASHIMPL2("hasimpl2");
    private String hashImplName;

    private HashImplEnum(String hashImplName) {
        this.hashImplName = hashImplName;
    }

    public String getHashImplName() {
        return hashImplName;
    }
}
