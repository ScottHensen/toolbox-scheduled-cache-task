package com.scotthensen.toolbox.scheduledtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ToolboxScheduledTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToolboxScheduledTaskApplication.class, args);
	}
}
