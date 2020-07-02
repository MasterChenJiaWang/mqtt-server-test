package com.example.demo4;

import org.springframework.stereotype.Component;

import com.unittec.customize.iot.mqtt.container.AuthencationSession;

@Component
public class MqttAuthencation implements AuthencationSession {
    @Override
    public boolean auth(String username, String password) {
        System.out.println("设备校验>>>>>>>>>" + String.format("username:%s,password:%s", username, password));
        return true;
    }
}
