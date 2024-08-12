package co.saiyan.common.kafka.core;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

/**
 * @description: MessageHandler
 * @author: lex
 * @date: 2024-06-13
 **/
public interface MessageHandler {

    void onMessage(@Payload List<ConsumerRecord<String, String>> records);
}
