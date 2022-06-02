package com.cloudkitchens.domain.order;

import lombok.Data;

@Data
public class ExceptionOrder extends Order {
    private String exceptionMessage;
}
