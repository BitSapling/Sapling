package com.github.bitsapling.sapling.autoconfig;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.github.bitsapling.sapling")
public class MyBatisMapperConfig {
}
