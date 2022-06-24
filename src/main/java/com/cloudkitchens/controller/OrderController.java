package com.cloudkitchens.controller;

import com.cloudkitchens.common.APIException;
import com.cloudkitchens.common.AppCode;
import com.cloudkitchens.common.Response;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring 中 @RestControllerAdvice 注解
 * 可以拦截+获取带有 @Controller 或 @RestController 注解类的异常，
 * 通过 @ExceptionHandler 注解设置捕获异常类型。
 */
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
    public Response createOrder( @Validated @RequestBody Order order) {
        if (order == null) {
            throw new APIException(AppCode.ORDER_ERROR,"订单参数有误");
        }
        try {
            orderService.createOrder();
            return Response.succ("success");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(e.getMessage());
        }
    }

}
