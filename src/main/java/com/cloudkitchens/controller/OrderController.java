package com.cloudkitchens.controller;

import com.cloudkitchens.common.Response;
import com.cloudkitchens.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Response createOrder() {
        try {
            orderService.createOrder();
            return Response.succ("success");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(e.getMessage());
        }
    }

}
