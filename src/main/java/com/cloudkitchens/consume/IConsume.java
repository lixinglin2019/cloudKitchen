package com.cloudkitchens.consume;

import com.cloudkitchens.enums.ConsumerEnum;

import java.util.Queue;

public interface IConsume {
    public ConsumerEnum type();
    public Object consume();
    public Queue queue();
}
