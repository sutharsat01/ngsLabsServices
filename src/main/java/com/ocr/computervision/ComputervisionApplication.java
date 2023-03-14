package com.ocr.computervision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@AutoConfiguration
@SpringBootApplication(scanBasePackages = "com.ocr")
@ComponentScan(basePackages ="com.ocr.computervision.*")
@EntityScan("com.ocr.computervision.*")
@EnableMongoRepositories(basePackages ="com.ocr.computervision.repository")

public class ComputervisionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComputervisionApplication.class, args);
	}

}
