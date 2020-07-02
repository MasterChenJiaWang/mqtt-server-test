package com.example.demo4;

import java.util.Optional;

import com.unittec.customize.iot.mqtt.api.RsocketMessageHandler;
import com.unittec.customize.iot.mqtt.common.connection.RetainMessage;

/**
 * @Description:
 * @author: chenjiawang
 * @CreateDate: 2020/6/28 10:20
 */
// @Component
public class RsocketMessageHandlerImpl implements RsocketMessageHandler {
    @Override
    public void saveRetain(boolean dup, boolean retain, int qos, String topicName, byte[] copyByteBuf) {
        System.out.println(
            String.format("saveRetain    >>>>>   dup: %s,retain:%s,qos:%s,topicName:%s", dup, retain, qos, topicName));
    }

    @Override
    public Optional<RetainMessage> getRetain(String topicName) {
        System.out.println(String.format("getRetain >>>>>>   topicName:%s", topicName));
        return Optional.empty();
    }
}
