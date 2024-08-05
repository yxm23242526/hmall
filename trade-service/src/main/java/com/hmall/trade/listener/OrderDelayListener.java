package com.hmall.trade.listener;

import com.hmall.api.client.PayClient;
import com.hmall.api.dto.PayOrderDTO;
import com.hmall.trade.constants.MQConst;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderDelayListener {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private PayClient payClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConst.DELAY_ORDER_QUEUE_NAME),
            exchange = @Exchange(name = MQConst.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConst.DELAY_ORDER_ROUTING_KEY
    ))
    public void orderDelayListener(Long orderId) {
        Order order = orderService.getById(orderId);
        Integer status = order.getStatus();
        if (status != 1){
            return;
        }
        PayOrderDTO payOrderDTO = payClient.queryPayOrderByBizOrderNo(orderId);
        int payStatus = payOrderDTO.getStatus();
        if (payStatus == 3){ //支付成功
            orderService.markOrderPaySuccess(orderId);
        } else { // 未支付
            orderService.cancelOrder(orderId);
        }
    }

}
