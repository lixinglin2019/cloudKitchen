package com.cloudkitchens.orderdelivery;

import com.cloudkitchens.orderdelivery.common.Response;
import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/dispatchOrder")
@Api(value = "下单", tags = "用户下单")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @param order
     * @return
     *
     * {
     *     "id":"",
     *     "name":"Cheese Pizza",
     *     "prepTime":13
     * }
     */
    @ApiOperation(value = "入口：用户下单")
    @PostMapping("/createOrder")
    public Response createOrder(@RequestBody Order order) {
        try {
            orderService.createOrder(order);
            return Response.succ(order);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(e.getMessage());
        }
    }

}
