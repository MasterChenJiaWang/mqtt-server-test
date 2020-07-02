package com.example.demo4;

import org.springframework.stereotype.Component;

import com.unittec.customize.iot.mqtt.container.ExceptorAcceptor;

@Component
public class ExceptionHandler implements ExceptorAcceptor {
    @Override
    public void accept(Throwable throwable) {
        System.out.println("异常处理>>>>>>>>>");
        System.out.println(throwable.getMessage());
    }
}
