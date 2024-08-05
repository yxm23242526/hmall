package com.hmall.trade.constants;

public interface MQConst {
    String DELAY_EXCHANGE_NAME = "trade.delay.dircet";

    String DELAY_ORDER_QUEUE_NAME = "trade.delay.order.queue";

    String DELAY_ORDER_ROUTING_KEY = "delay.order.key";

    int DELAY_ORDER_TIME = 150000;
}
