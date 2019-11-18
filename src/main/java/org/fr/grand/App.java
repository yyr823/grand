package org.fr.grand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Hello world!
 *
 */
@SpringBootApplication
/*
 * @EnableAsync
 * 
 * @EnableTransactionManagement
 */
@MapperScan("org.fr.grand.mapper")
public class App {
	public static void main(String[] args) {
		new SpringApplicationBuilder(App.class).run(args);
	}
}
