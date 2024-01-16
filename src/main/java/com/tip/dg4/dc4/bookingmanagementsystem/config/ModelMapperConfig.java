package com.tip.dg4.dc4.bookingmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * ModelMapperConfig
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/4/2023
 * @since 1.0.0
 */
@Configuration
public class ModelMapperConfig {
	@Bean
	public ModelMapper modelMapper() {
		// Create ModelMapper object and configure
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT)
				.setSkipNullEnabled(true);
		return modelMapper;
	}
}
