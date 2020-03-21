package org.serbia.covid19.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Covid19Configuration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}