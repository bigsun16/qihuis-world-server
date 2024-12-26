package com.qihui.sun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author bigsu
 */
@SpringBootApplication
@MapperScan(basePackages = "com.qihui.sun.mapper")
@EnableCaching
public class QihuisWorldApplication {

	public static void main(String[] args) {
		SpringApplication.run(QihuisWorldApplication.class, args);
	}

}
