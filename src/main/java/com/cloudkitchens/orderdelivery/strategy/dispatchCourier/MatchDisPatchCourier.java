package com.cloudkitchens.orderdelivery.strategy.dispatchCourier;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;
import com.cloudkitchens.orderdelivery.enums.CourierArriveQueueEnum;
import com.cloudkitchens.orderdelivery.enums.DispatchCourier2OrderEnum;
import com.cloudkitchens.orderdelivery.enums.DispatchCourierStrategyEnum;
import com.cloudkitchens.orderdelivery.enums.HashImplEnum;
import com.cloudkitchens.orderdelivery.strategy.hashAlgorithm.IHash;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class MatchDisPatchCourier implements IDispatchCourier{

    Map<String,IHash> allHashImpls = new HashMap<>();
    public MatchDisPatchCourier(List<IHash> hashImpls) {
        for (IHash hashImpl : hashImpls) {
            String implName = hashImpl.getImplName();
            allHashImpls.put(implName,hashImpl);
        }
    }

    @Override
    public String getType() {
        return DispatchCourierStrategyEnum.MATCH.name();
    }


    /**
     * 给所有订单都 match 到快递员
     * @param ordersqueue
     * @param courierUserqueue
     *
     * 注意：这里很有可能，一个快递员被分配了多个订单
     */
    @Override
    public void dispatchCourier(LinkedBlockingQueue<Order> ordersqueue, LinkedBlockingQueue<CourierUser> courierUserqueue) {
        //Global Map  store the relashionship between order & courier
        Map<Order, CourierUser> dispatchCourier2OrderMap = DispatchCourier2OrderEnum.DISPATCH_COURIER_2_ORDER_ENUM.getDispatchCourier2OrderMap();
        List<Order> orders = queue2List(ordersqueue);
        List<CourierUser> courierUsers = queue2List(courierUserqueue);
        int courierUserCount = courierUsers.size();
        for (Order order : orders) {
            String orderId = order.getId();
            int orderHash = allHashImpls.get(HashImplEnum.HASHIMPL1).orderIdHash(orderId);
            int courierIndex = orderHash % courierUserCount;//改为按照位与运算（提升效率）--前提条件长度必须是2^n
            CourierUser courierUser = courierUsers.get(courierIndex);
            order.setCourierId(courierUser.getId());//订单确认curision
            dispatchCourier2OrderMap.put(order,courierUser);


        }

        Map<String, List<Order>> courierOrderList = CourierArriveQueueEnum.COURIER_ARRIVE_QUEUE_ENUM.getCourierOrderList();
        //快递视角组装map  快递员-->订单列表
        for (Order order : orders) {
            String courierId = order.getCourierId();
            List<Order> orders1 = courierOrderList.get(courierId);
            if (orders1 == null) {
                List<Order> orderList= new ArrayList<>();
                orderList.add(order);
                courierOrderList.put(courierId, orderList);
            }else {
                orders1.add(order);
                courierOrderList.put(courierId,orders1);
            }
        }
    }

    private List queue2List(LinkedBlockingQueue courierUserqueue) {
        List list = new ArrayList();
        for (Object o : courierUserqueue) {
            list.add(o);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(8%8);
    }
}
