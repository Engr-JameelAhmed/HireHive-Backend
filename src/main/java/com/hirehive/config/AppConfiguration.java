package com.hirehive.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper(){ // this will directly convert entity to dto, this is shortest way
        return new ModelMapper();
    }


}
