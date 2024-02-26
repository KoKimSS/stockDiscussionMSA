package com.example.stockmsaactivity.kafka;

import com.example.stockmsaactivity.client.dto.CreateNewsFeedRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, CreateNewsFeedRequestDto> kafkaTemplate;

    public void createNewsFeed(CreateNewsFeedRequestDto payload) {
        log.info("sending payload={} ", payload);
        kafkaTemplate.send("NewsFeed", payload);
    }
}