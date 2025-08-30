package com.eventmanagement.backend.config;

//package com.eventmanagement.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "drcbizxtz",
                "api_key", "827895228745366",
                "api_secret", "tz9IVCX-kte3s1aYlYAMjiKgjgY"));
    }
}

