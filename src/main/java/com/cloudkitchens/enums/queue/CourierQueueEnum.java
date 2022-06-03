package com.cloudkitchens.enums.queue;

import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.dto.CourierDelayDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;

public enum CourierQueueEnum {
    COURIER_QUEUE;

    /**
     * 所有的快递员列表，在每次创建订单时--同时创建一个 快递员
     */
    public List<Courier> allCouriers = new ArrayList<>();

    /**
     * 快递员按照到达餐厅时间顺序拍好队-放入该队列中
     * <p>
     * Match策略  -- 只需遍历这个队列，每个对象中的order,是否在ready队列中存在（这里就是所谓的某人指定match具体某个餐），
     * 找到，则直接取走（同时各自在队列中清除，同时记得修改order属性中的时间字段），
     * 没找到，则把快递员（设置 还需要等待的时间）放入 courierDelayQueue 等待到时执行 ---该延时队列在执行时，记得执行上面找到的动作
     * <p>
     * FIFO 策略 -- 从该队列（快递员队列）中取一个快递员，然后再去ready队列中找一个出队列，找到，则直接取走，ready队列为空（没有已经准备好的餐）则两个队列都不动
     * 需要快递排好队，所以选择这样的数据结构
     * 快递到达队列，（根据到达时间的一个优先级队列）
     * 比较器就是到达实际
     */

    /**
     * Match策略不需要排队，只要到了，不管前面有没有人，直接取联系餐厅取餐
     *
     * 下面两队列都是为FIFO策略准备的，
     *
     */

    public  PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue = new PriorityBlockingQueue<CourierArriveDTO>();//快递员到达餐厅队列

    public  DelayQueue<CourierDelayDTO> courierdelayQueue = new DelayQueue<CourierDelayDTO>();//快递到达后，餐还没做好，则把这些快递放入延迟队列，（延迟时间=餐厅接收时间+订单耗时-当前系统时间）


    private CourierQueueEnum() {

    }


   }
