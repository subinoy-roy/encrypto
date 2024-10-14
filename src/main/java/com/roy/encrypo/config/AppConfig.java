package com.roy.encrypo.config;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public AWSKMS awsKmsConfig() {
        return AWSKMSClient.builder().build();
    }
}
