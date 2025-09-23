package com.nhohantu.tcbookbe.config.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper(List<Converter<?, ?>> converters) {
        ModelMapper modelMapper = new ModelMapper();

        converters.forEach(modelMapper::addConverter);

        return modelMapper;
    }
}
