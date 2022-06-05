package com.cloudkitchens.util;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderUtil {
    static Logger log = LoggerFactory.getLogger(OrderUtil.class);

    public static void printTimegap(Order order) {

        long kitchenWaitCourierTime = order.getKitchenWaitCourierTime();
        long courierWaitKitchenTime = order.getCourierWaitKitchenTime();
        if (courierWaitKitchenTime > 0) {//快递先到的，等待餐后来才制作完成
            log.info("orderid:{},courier wait time {}(milliseconds) between arrival and order pickup", order.getId(), courierWaitKitchenTime);
        }
        if (kitchenWaitCourierTime > 0) {
            log.info("orderid:{},food wait time {}(milliseconds) between order ready and pickup", order.getId(), kitchenWaitCourierTime);
        }
    }


    public static void setTimeGap(Order order) {
        long courierArriveTime = order.getCourierArriveAtTime();//快递到达时间
        long kitchenReadyTime = order.getKitchenReadyTime();//餐厅准备好的时间
        long orderPickUpTime = order.getOrderPickUpTime();//快递员拣货时间
        //只要有一个值为0，则不做设置
        if (kitchenReadyTime == 0 || courierArriveTime == 0 || orderPickUpTime == 0) {
            return;
        }

        String formatData = TimeUtil.getFormatData(courierArriveTime);
        String formatData1 = TimeUtil.getFormatData(kitchenReadyTime);
        String formatData2 = TimeUtil.getFormatData(orderPickUpTime);
        System.out.println("快递到达：" + formatData);
        System.out.println("餐厅备好：" + formatData1);
        System.out.println("取餐时间：" + formatData2);


        long l = kitchenReadyTime - courierArriveTime;
        if (l > 0) {//快递员到了，食物还没准备好
            long l1 = orderPickUpTime - courierArriveTime;//取餐实际-餐厅准备好的时间
            order.setCourierWaitKitchenTime(l1);
        } else {//食物准备好了，快递员还没到--食物等快递员
            long l1 = orderPickUpTime - kitchenReadyTime;
            order.setKitchenWaitCourierTime(l1);
        }
    }

    /**
     * 下单
     *
     * @throws InterruptedException
     */
    public static void simulateConcurrentCreateOrder(OrderService orderService) throws Exception {
//        String orderConcurrentProp = PropertyUtils.getOrderTestProp("order.concurrent");//并发度（每秒几单）
        String orderTotalProp = PropertyUtils.getOrderTestProp("order.totalNumber");//总订单数（每秒几单）
//        int concurrentNumber = Integer.parseInt(orderConcurrentProp);//4
        int concurrentTotal = Integer.parseInt(orderTotalProp);//3

        for (int i = 0; i < concurrentTotal; i++) {
            orderService.createOrder();
        }
//        ExecutorService executorService = Executors.newFixedThreadPool(5);

//        //分多次 并发请求，才能处理完请求  使用CyclicBarrier
//        if (concurrentTotal >= concurrentNumber) {
//            batchConcurrent(concurrentTotal, concurrentNumber, executorService,orderService);
//        } else {//一次搞定 使用countDownLaunch
//            justOnceConcurrent(concurrentTotal, executorService,orderService);
//        }
    }

    public static void main(String[] args) {
        long l = -123;
        l = Math.abs(l);
        System.out.println(l);
    }
//    private static void batchConcurrent(int concurrentTotal, int concurrentNumber, ExecutorService executorService,OrderService orderService) {
//        //分批次打完
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(concurrentNumber, new Runnable() {
//            //当倒计数器为0 ，立即执行
//            @Override
//            public void run() {
//                //计数器变为了0，统一收集任务，开始批量执行收集到的任务
//                System.out.println("订单并发数量够了,可以开始创建了");
//            }
//        });
//
//
//        int parties = concurrentTotal / concurrentNumber;//0
//        int useCyclicBarrier = parties * concurrentTotal;//0
//        //任务
//        for (int i = 0; i < concurrentTotal; i++) {
//            final int index = i;
//            if (index < useCyclicBarrier) {
//                //任务
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("线程" + index + "到达屏障点-准备好创建订单");
//                        try {
//                            cyclicBarrier.await();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (BrokenBarrierException e) {
//                            e.printStackTrace();
//                        }
//                        simulateCreatOrder(orderService);
//                        System.out.println("线程" + index + "订单创建完成");
//                    }
//                };
//                executorService.submit(runnable);//任务提交到线程池执行
//            } else {
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("线程" + index + "准备好创建订单");
//                        simulateCreatOrder(orderService);
//                        System.out.println("线程" + index + "订单创建完成");
//                    }
//                };
//                executorService.submit(runnable);//任务提交到线程池执行
//            }
//        }
//        executorService.shutdown();
//    }
//
//    private static void justOnceConcurrent(int concurrentTotal, ExecutorService executorService,OrderService orderService) throws InterruptedException {
//        CountDownLatch countDownLatch = new CountDownLatch(concurrentTotal);
//        for (int i = 0; i < concurrentTotal; i++) {
//            final int index = i;
//            //任务
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("线程" + index + "到达起跑线-准备好创建订单");
//                    countDownLatch.countDown();
//                    simulateCreatOrder(orderService);
//                    System.out.println("线程" + index + "订单创建完成");
//                }
//            };
//            executorService.submit(runnable);//任务提交到线程池执行
//        }
//        countDownLatch.await();//等待计数器归0
////        System.out.println(concurrentTotal + "订单并发，准备就绪");
//
//        //
//        executorService.shutdown();
//    }


}
