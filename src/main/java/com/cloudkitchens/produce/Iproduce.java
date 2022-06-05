package com.cloudkitchens.produce;

import com.cloudkitchens.enums.ProducerEnum;

import java.util.Queue;

public interface Iproduce {
    public ProducerEnum type();
    public  void produce(Object object);
    public  void produce(Queue queue,Object object);
    public Queue queue();
}
