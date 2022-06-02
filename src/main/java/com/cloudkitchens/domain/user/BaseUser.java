package com.cloudkitchens.domain.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class BaseUser implements Serializable {
    private String id;
    private String userName;
    private String password;
    private String profile;
}
