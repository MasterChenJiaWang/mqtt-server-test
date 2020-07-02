// package com.example.demo4;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import com.example.demo4.utils.RedisUtils;
// import com.unittec.customize.iot.mqtt.api.RsocketTopicManager;
// import com.unittec.customize.iot.mqtt.api.TransportConnection;
//
// import cn.hutool.cache.CacheUtil;
// import cn.hutool.cache.impl.LFUCache;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
/// **
// * @Description:
// * @author: chenjiawang
// * @CreateDate: 2020/6/29 10:13
// */
//// @Component
// public class MyCacheTopicManger implements RsocketTopicManager {
//
// @Autowired
// private RedisUtils redisUtils;
// /**
// *
// */
//// private static final LFUCache<String, List<TransportConnection>> CACHE_TOPIC =
//// CacheUtil.newLFUCache(100, 100 * 60 * 1000);
//
// private static final String TOPIC_CACHE_KEY="iot:server:topic:";
// /**
// *
// * @param topic
// * @return
// */
// @Override
// public List<TransportConnection> getConnectionsByTopic(String topic) {
// System.out.println("topic: " + topic);
//// return CACHE_TOPIC.get(topic);
// String s = redisUtils.get(TOPIC_CACHE_KEY + topic);
//
// return redisUtils.get(TOPIC_CACHE_KEY+topic);
// }
//
// @Override
// public void addTopicConnection(String topic, TransportConnection transportConnection) {
// System.out.println("addTopicConnection: " + topic);
// List<TransportConnection> transportConnections = CACHE_TOPIC.get(topic);
// if (transportConnections == null) {
// transportConnections = new ArrayList<>();
// }
// transportConnections.add(transportConnection);
// CACHE_TOPIC.put(topic, transportConnections);
// }
//
// @Override
// public void deleteTopicConnection(String topic, TransportConnection transportConnection) {
// System.out.println("deleteTopicConnection: " + topic);
// List<TransportConnection> transportConnections = CACHE_TOPIC.get(topic);
// if (transportConnections == null) {
// return;
// }
// transportConnections.remove(transportConnection);
// CACHE_TOPIC.put(topic, transportConnections);
// }
// }
