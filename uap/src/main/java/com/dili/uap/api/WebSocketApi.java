package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.redis.delayqueue.annotation.StreamListener;
import com.dili.ss.redis.delayqueue.consts.DelayQueueConstants;
import com.dili.ss.redis.delayqueue.dto.DelayMessage;
import com.dili.ss.redis.delayqueue.impl.DistributedRedisDelayQueueImpl;
import com.dili.ss.util.DateUtils;
import com.dili.uap.boot.RabbitConfiguration;
import com.dili.uap.glossary.WsEventType;
import com.dili.uap.manager.WsSessionManager;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;
import com.dili.uap.sdk.domain.dto.AnnunciateMessages;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * web socket接口
 * @author: WM
 * @time: 2021/1/20 9:32
 */
@RestController
@RequestMapping("/api/ws")
public class WebSocketApi {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;

    @Autowired
//    StandaloneRedisDelayQueueImpl redisDelayQueue;
    DistributedRedisDelayQueueImpl redisDelayQueue;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    /**
     * add
     */
    @PostMapping(value = "/add")
    public void add(DelayMessage delayMessage){
//        DelayMessage delayMessage = DTOUtils.newInstance(DelayMessage.class);
//        delayMessage.setBody("body2");
//        delayMessage.setDelayTime(System.currentTimeMillis()+5000L);
//        delayMessage.setTopic("testTopic");
        delayMessage.setCreateTime(LocalDateTime.now());
        delayMessage.setDelayTime(System.currentTimeMillis() + delayMessage.getDelayTime());
        logger.info(DateUtils.format(new Date())+"添加消息:"+delayMessage.getBody());
        redisDelayQueue.push(delayMessage);
    }


    /**
     * test
     */
    @GetMapping(value = "/test")
    public void test(){
        final String LUA_SCRIPT;
        StringBuilder sb = new StringBuilder(512);
        //KEY1:delay:wait:testTopic, KEY2:META_TOPIC_ACTIVE, KEY3:delay:active:testTopic
        //ARGV1:System.currentTimeMillis(), ARGV2:delay:active:testTopic
        //------------------------------------------------------------------------------------
        //返回有序集合KEYS[1]:(delay:wait:topic)中小于ARGV[1]:System.currentTimeMillis()score区间的成员(DelayMessage.body)，只取一条(就像SQL中的 SELECT LIMIT offset, count )
//        sb.append("local val = redis.call('zrangebyscore', KEYS[1], '-inf', ARGV[1], 'limit', 0, 1)\n");
        sb.append("local val = redis.call('zrangebyscore', KEYS[1], '-inf', ARGV[1])\n");
//        sb.append("redis.call('sadd', 'delay:val:length', '长度:'..tostring(#val))\n");
        sb.append("if(next(val) ~= nil) then\n");
        //向META_TOPIC_ACTIVE(zset)集合中添加topicKey(delay:active:topic)
        sb.append("    redis.call('sadd', KEYS[2], ARGV[2])\n");
        //移除有序集合KEYS[1](delay:wait:topic)中zrangebyscore取出的数据
        sb.append("    redis.call('zremrangebyscore', KEYS[1], '-inf', ARGV[1])\n");
        //向KEYS[3]:topicKey(delay:active:topic)列表中循环插入所有数组元素
        sb.append("  for i = 1, #val, 1 do\n");
//        sb.append("    redis.call('sadd', 'delay:i', tostring(i))\n");
//        sb.append("    redis.call('sadd', 'delay:val:item', val[i])\n");
        //unpack()函数是接受一个数组来作为输入参数，并默认从下标为1开始返回所有元素。
        sb.append("    redis.call('rpush', KEYS[3], val[i])\n");
        sb.append("  end\n");
        sb.append("  return 1\n");
        sb.append("end\n");
        sb.append("return 0");
        LUA_SCRIPT = sb.toString();
        String activeTopic = "delay:active:testTopic";

        Object[] keys = new Object[]{serialize("delay:wait:testTopic"), serialize(DelayQueueConstants.META_TOPIC_ACTIVE), serialize(activeTopic)};
        Object[] values = new Object[]{serialize(String.valueOf(System.currentTimeMillis())), serialize(activeTopic)};
        Long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            if (nativeConnection instanceof RedisAsyncCommands) {
                RedisAsyncCommands commands = (RedisAsyncCommands) nativeConnection;
                return (Long) commands.getStatefulConnection().sync().eval(LUA_SCRIPT, ScriptOutputType.INTEGER, keys, values);
            } else if (nativeConnection instanceof RedisAdvancedClusterAsyncCommands) {
                RedisAdvancedClusterAsyncCommands commands = (RedisAdvancedClusterAsyncCommands) nativeConnection;
                return (Long) commands.getStatefulConnection().sync().eval(LUA_SCRIPT, ScriptOutputType.INTEGER, keys, values);
            }
            return 0L;
        });
        if(result != null && result > 0) {
            logger.info("延迟队列[2]，消息到期进入执行队列({}): {}", activeTopic);
        }
    }

    /**
     * serialize
     * @param key
     * @return
     */
    private byte[] serialize(String key) {
        RedisSerializer<String> stringRedisSerializer =
                (RedisSerializer<String>) redisTemplate.getKeySerializer();
        //lettuce连接包下序列化键值，否则无法用默认的ByteArrayCodec解析
        return stringRedisSerializer.serialize(key);
    }

    /**
     * 发送平台公告
     * @param annunciateMessage
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sendAnnunciate")
    public BaseOutput sendAnnunciate(AnnunciateMessage annunciateMessage) {
        DelayMessage delayMessage = DTOUtils.newInstance(DelayMessage.class);
        delayMessage.setBody("body");
        delayMessage.setCreateTime(LocalDateTime.now());
        delayMessage.setDelayTime(System.currentTimeMillis()+10000L);
        delayMessage.setTopic("testTopic");
        redisDelayQueue.push(delayMessage);
        //设置事件类型为发送公告
        annunciateMessage.setEventType(WsEventType.SEND_ANNUNCIATE.getCode());
        return sendMessage(annunciateMessage);
    }

    /**
     * 测试延迟队列监听
     * @param message
     */
    @StreamListener("testTopic")
    public void listener(DelayMessage message){
        logger.info("==================================");
        logger.info(DateUtils.format(new Date())+"接收到消息:"+message.getBody());
    }

    /**
     * 发送平台公告给多目标id
     * @param annunciateMessages
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sendAnnunciates")
    public BaseOutput sendAnnunciates(AnnunciateMessages annunciateMessages) {
        //设置事件类型为发送公告
        annunciateMessages.setEventType(WsEventType.SEND_ANNUNCIATE.getCode());
        return sendMessages(annunciateMessages);
    }

    /**
     * 撤回平台公告
     * @param annunciateMessage， targetId和id必填
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/withdrawAnnunciate")
    public BaseOutput withdrawAnnunciate(AnnunciateMessage annunciateMessage) {
        //设置事件类型为撤销公告
        annunciateMessage.setEventType(WsEventType.WITHDRAW_ANNUNCIATE.getCode());
        return sendMessage(annunciateMessage);
    }

    /**
     * 批量撤销平台公告
     * @param annunciateMessages targetIds和id必填
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/withdrawAnnunciates")
    public BaseOutput withdrawAnnunciates(AnnunciateMessages annunciateMessages) {
        //设置事件类型为发送公告
        annunciateMessages.setEventType(WsEventType.WITHDRAW_ANNUNCIATE.getCode());
        return sendMessages(annunciateMessages);
    }

    /**
     * 发送消息给一个目标， 发送和撤销公告共用该方法
     * @param annunciateMessage
     * @return
     */
    private BaseOutput sendMessage(AnnunciateMessage annunciateMessage){
        try {
            WebSocketSession webSocketSession = WsSessionManager.get(annunciateMessage.getTargetId());
            //如果连接在本地，则直接发(多实例，用户的连接可能在其它UAP实例)
            if(webSocketSession != null){
                webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(annunciateMessage)));
                return BaseOutput.success("消息发送成功");
            }
            //如果当前服务器没有连接，则发送广播消息
            rabbitMQMessageService.send(RabbitConfiguration.UAP_TOPIC_EXCHANGE, RabbitConfiguration.UAP_ANNUNCIATE_KEY, JSON.toJSONString(annunciateMessage));
            return BaseOutput.success("消息发送成功");
        } catch (Exception e) {
            return BaseOutput.failure("消息发送失败:"+e.getMessage());
        }
    }

    /**
     * 发送消息给多个目标， 发送和撤销公告共用该方法
     * @param annunciateMessages
     * @return
     */
    public BaseOutput sendMessages(AnnunciateMessages annunciateMessages) {
        try {
            //保存不在本地的目标，发给MQ
            List<String> targetIdsLeft = new ArrayList<>();
            //原始目标列表
            List<String> targetIdsOri = annunciateMessages.getTargetIds();
            //清空目标列表
            annunciateMessages.setTargetIds(null);
            for (String targetId : targetIdsOri) {
                WebSocketSession webSocketSession = WsSessionManager.get(targetId);
                //如果连接在本地，则直接发(多实例，用户的连接可能在其它UAP实例)
                if(webSocketSession != null){
                    webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(annunciateMessages)));
                }else{
                    targetIdsLeft.add(targetId);
                }
            }
            if (targetIdsLeft != null) {
                for (String targetId : targetIdsLeft) {
                    annunciateMessages.setTargetId(targetId);
                    //如果连接不在当前服务器，则发送广播消息
                    rabbitMQMessageService.send(RabbitConfiguration.UAP_TOPIC_EXCHANGE, RabbitConfiguration.UAP_ANNUNCIATE_KEY, JSON.toJSONString(annunciateMessages));
                }
            }
            return BaseOutput.success("消息发送成功");
        } catch (Exception e) {
            return BaseOutput.failure("消息发送失败:"+e.getMessage());
        }
    }
}
