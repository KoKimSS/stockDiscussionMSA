package com.example.stockbatch.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@ConditionalOnMissingBean(JobRegistry.class)
//public class JobRegistryConfiguration {
//    @Bean
//    public JobRegistry jobRegistry() {
//        return new MapJobRegistry();
//    }
//}