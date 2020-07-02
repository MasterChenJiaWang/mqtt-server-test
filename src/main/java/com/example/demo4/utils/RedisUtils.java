// package com.example.demo4.utils;
//
// import java.util.ArrayList;
// import java.util.Collection;
// import java.util.Collections;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
// import java.util.concurrent.TimeUnit;
//
// import javax.annotation.Resource;
//
// import org.junit.platform.commons.util.StringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.redis.connection.RedisStringCommands;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.data.redis.core.ValueOperations;
// import org.springframework.data.redis.core.script.RedisScript;
// import org.springframework.data.redis.core.types.Expiration;
// import org.springframework.stereotype.Component;
//
/// ***
// *
// * @author wuxinwei
// */
// @Component
// public class RedisUtils {
//
// @Resource
// StringRedisTemplate stringRedisTemplate;
//
// @Autowired
// RedisTemplate<String, Object> redisTemplate;
//
// public RedisTemplate<String, Object> getRedisTemplate() {
// return redisTemplate;
// }
//
// private static final String SCRIPT =
// "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//
// @Value("${spring.application.name}")
// private String serviceName;
//
// private String getKey(String key) {
// if (key.contains(serviceName)) {
// return key;
// }
// return serviceName + ":" + key;
// }
//
// public boolean setObject(String key, Object object) {
// ValueOperations<String, Object> objectObjectValueOperations = redisTemplate.opsForValue();
// objectObjectValueOperations.set(getKey(key), object);
// return true;
// }
//
// /**
// * 加锁 尝试100次
// *
// * @param lockKey
// * @param requestId
// * @param expireTime
// * @return
// */
// public Boolean setRequestLock(String lockKey, String requestId, long expireTime) {
// Boolean aBoolean = tryLock(lockKey, requestId, expireTime);
// if (!aBoolean) {
// int count = 0;
// try {
// Thread.sleep(expireTime / 100);
// } catch (InterruptedException e) {
// return aBoolean;
// }
// while (count < 100) {
// Boolean aBoolean1 = tryLock(lockKey, requestId, expireTime);
// if (!aBoolean1) {
// count++;
// continue;
// }
// return aBoolean1;
// }
// }
// return aBoolean;
// }
//
// /**
// * 用原子命令SET my_key my_value NX PX milliseconds来获取锁
// *
// * @param lockKey
// * @param requestId
// * @param expireTime
// * @return
// */
// public boolean tryLock(String lockKey, String requestId, long expireTime) {
// try {
// return stringRedisTemplate.execute((connection) -> {
// Boolean set = connection.set(getKey(lockKey).getBytes(), requestId.getBytes(),
// Expiration.milliseconds(expireTime), RedisStringCommands.SetOption.SET_IF_ABSENT);
// if (null == set) {
// return false;
// }
// return set;
// }, true);
// } catch (Exception e) {
// return false;
// }
// }
//
// public boolean releaseLock(String lockKey, String requestId) {
//
// try {
// Object result = stringRedisTemplate.execute(RedisScript.of(SCRIPT, Long.class),
// Collections.singletonList(getKey(lockKey)), requestId);
// if (result.equals(1L)) {
// return true;
// } else {
// return false;
// }
// } catch (Exception e) {
// return false;
// }
// }
//
// /**
// * 读取缓存
// *
// * @param key
// * @return
// */
// public String get(final String key) {
// try {
// return stringRedisTemplate.opsForValue().get(getKey(key));
// } catch (Exception e) {
// e.printStackTrace();
// }
// return "";
// }
//
// /**
// * 写入缓存
// *
// * @param key
// * 缓存的KEY
// * @param value
// * 缓存的数据
// * @return
// */
// public boolean set(final String key, String value) {
// boolean result = false;
// try {
// ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
// operations.set(getKey(key), value);
// result = true;
// } catch (Exception e) {
// e.printStackTrace();
// }
// return result;
// }
//
// /**
// * 写入缓存
// *
// * @param key
// * 缓存的KEY
// * @param value
// * 缓存的数据
// * @param expireTime
// * 过期时间单位秒
// * @return
// */
// public boolean set(String key, String value, Long expireTime) {
// boolean result = false;
// try {
// ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
// operations.set(getKey(key), value);
// stringRedisTemplate.expire(getKey(key), expireTime, TimeUnit.SECONDS);
// result = true;
// } catch (Exception e) {
// e.printStackTrace();
// }
// return result;
// }
//
// /**
// * 删除对应的value
// *
// * @param key
// * 删除的key
// */
// public boolean remove(final String key) {
// try {
// if (exists(key)) {
// return stringRedisTemplate.delete(getKey(key));
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// return false;
// }
//
// /**
// * 判断缓存中是否有对应的value
// *
// * @param key
// * @return
// */
// public boolean exists(final String key) {
// try {
// return stringRedisTemplate.hasKey(getKey(key));
// } catch (Exception e) {
// e.printStackTrace();
// }
// return false;
// }
//
// /**
// * 添加队列
// *
// * @param key
// * @param value
// */
// public Long leftPushList(String key, String value) {
// try {
// return stringRedisTemplate.opsForList().leftPush(getKey(key), value);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// /**
// * 读取队列
// *
// * @param key
// * @return
// */
// public List<String> range(String key, Integer startIndex, Integer endIndex) {
// try {
// return stringRedisTemplate.opsForList().range(getKey(key), startIndex, endIndex);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return new ArrayList<>();
// }
//
// /**
// * 弹出队列
// *
// * @param key
// * @return
// */
// public String rightPopList(String key) {
// try {
// if (stringRedisTemplate.opsForList().size(getKey(key)) > 0) {
// return stringRedisTemplate.opsForList().rightPop(getKey(key)).toString();
// }
// } catch (Exception e) {
// return null;
// }
// return null;
// }
//
// /**
// * 插入缓存
// *
// * @param key
// * @param value
// */
// public Long setValueSet(String key, String value) {
// try {
// return stringRedisTemplate.opsForSet().add(getKey(key), value);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// /**
// * 读取缓存
// *
// * @param key
// * @return
// */
// public List<String> getValueSet(String key) {
// try {
// return new ArrayList<>(stringRedisTemplate.opsForSet().members(getKey(key)));
// } catch (Exception e) {
// e.printStackTrace();
// }
// return new ArrayList<>();
// }
//
// /**
// * 获取count数
// *
// * @param key
// * @return
// */
// public int getCountSet(String key) {
// try {
// return stringRedisTemplate.opsForSet().size(getKey(key)).intValue();
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0;
// }
//
// /**
// * 记数+1
// *
// * @param key
// * @param sonKey
// */
// public void plusCountHash(String key, String sonKey) {
// try {
// Object obj = stringRedisTemplate.opsForHash().get(getKey(key), sonKey);
// int i = obj != null ? Integer.parseInt(obj.toString()) : 0;
// stringRedisTemplate.opsForHash().put(getKey(key), sonKey, String.valueOf(i + 1));
// } catch (NumberFormatException e) {
// e.printStackTrace();
// }
// }
//
// /**
// * 读取hash 数据
// *
// * @param key
// * @param sonKey
// * @return
// */
// public String getValueHash(String key, String sonKey) {
// try {
// Object obj = stringRedisTemplate.opsForHash().get(getKey(key), sonKey);
// return obj == null ? null : obj.toString();
// } catch (Exception e) {
// e.printStackTrace();
// }
// return "";
// }
//
// /**
// * 删除hash
// *
// * @param key
// * @param sonKey
// */
// public Long delValueHash(String key, String sonKey) {
// try {
// return stringRedisTemplate.opsForHash().delete(getKey(key), sonKey);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// /**
// * 获取hash大小
// *
// * @param key
// * @return
// */
// public Long getSizeHash(String key) {
// try {
// return stringRedisTemplate.opsForHash().size(getKey(key));
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// /**
// * 计数
// *
// * @param key
// * @param value
// */
// public Long incrByFloat(String key, Long value) {
// try {
// return stringRedisTemplate.boundValueOps(getKey(key)).increment(value);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// /**
// * 计数
// *
// * @param key
// */
// public Long incr(String key) {
// try {
// return stringRedisTemplate.boundValueOps(getKey(key)).increment(1);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// /**
// * 减1
// *
// * @param key
// * @return
// */
// public boolean decr(String key) {
// final String script =
// "if tonumber(redis.call('get', KEYS[1])) > 0 then return redis.call('incrby', KEYS[1], -1) else return -1 end";
// try {
// Object result =
// stringRedisTemplate.execute(RedisScript.of(script, Long.class), Collections.singletonList(getKey(key)));
// if (((Long)result).longValue() > -1L) {
// return true;
// } else {
// return false;
// }
// } catch (Exception e) {
// return false;
// }
// }
//
// /**
// * 模糊查询KEY
// *
// * @param key
// * @return
// */
// public Set<String> keys(String key) {
// return stringRedisTemplate.keys(getKey(key) + "*");
// }
//
// public Long getValueHashValue(String key) {
// try {
// Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(getKey(key));
// long value = 0L;
// for (Map.Entry<Object, Object> entry : map.entrySet()) {
// value += Long.parseLong(entry.getValue() != null ? entry.getValue().toString() : "0");
// }
// return value;
// } catch (NumberFormatException e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// public void getCallCount(String redisName) {
// String service = get(redisName);
// if (StringUtils.isBlank(service)) {
// set(redisName, "1");
// } else {
// int count = Integer.parseInt(service);
// count += 1;
// set(redisName, count + "");
// }
// }
//
// /**
// * @param set
// * @return
// */
// public Long delete(Collection<String> set) {
// try {
// return stringRedisTemplate.delete(set);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return 0L;
// }
//
// /**
// * @param key
// * @return
// */
// public Boolean delete(String key) {
// try {
// return stringRedisTemplate.delete(getKey(key));
// } catch (Exception e) {
// e.printStackTrace();
// }
// return false;
// }
//
// /**
// * @param key
// * @return
// */
// public Boolean deleteNoApplication(String key) {
// try {
// return stringRedisTemplate.delete(key);
// } catch (Exception e) {
// e.printStackTrace();
// }
// return false;
// }
// }
