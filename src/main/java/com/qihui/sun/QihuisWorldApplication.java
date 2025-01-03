package com.qihui.sun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author bigsu
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class QihuisWorldApplication {

	public static void main(String[] args) {
		SpringApplication.run(QihuisWorldApplication.class, args);
	}

}
