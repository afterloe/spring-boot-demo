/**
 * create by aftelroe 2017-4-14
 */
package com.github.afterloe.spring_boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LanchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LanchApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("检查并输出 spring boot中注册的beans");
			String[] beanNames = ctx.getBeanDefinitionNames();
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		};
	}
}