package com.example.blog.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.blog.mapper")
class MyBatisConfig {

    // 这里可以添加一些其他的配置，例如数据源、事务管理器等

}
