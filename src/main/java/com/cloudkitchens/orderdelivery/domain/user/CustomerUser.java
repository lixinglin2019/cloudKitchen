package com.cloudkitchens.orderdelivery.domain.user;

import lombok.Data;

@Data
public class CustomerUser extends BaseUser{
    private String address;
}
