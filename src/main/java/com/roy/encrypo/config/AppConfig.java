package com.roy.encrypo.config;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${app.aws_kms_region.value}")
    private String AWS_KMS_REGION;

    @Bean
    public AWSKMS awsKmsConfig() {
        return AWSKMSClient.builder()
                .withRegion(AWS_KMS_REGION)
                .build();
    }
}
