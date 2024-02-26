package com.example.stockmsanewsfeed.kafka;

import com.example.stockmsanewsfeed.service.newsFeedService.NewsFeedService;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
@Slf4j
@Data
public class KafkaConsumer {
    private final NewsFeedService newsFeedService;
    @KafkaListener(topics = "NewsFeed",
            groupId = "myGroup1")
    public void consume(CreateNewsFeedRequestDto record) {
        log.info(record.toString());
        newsFeedService.createNewsFeed(record);
    }
}