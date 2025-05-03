package com.appcenter.wnt.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create(Long userId, Long couponId){
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("couponId", couponId);

        kafkaTemplate.send("create_reservation", toJson(data));
    }

    private String toJson(Map<String, Object> data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        }catch (JsonProcessingException e){
            throw new RuntimeException("kafak 메시지 직렬화 실패", e);
        }
    }
}
