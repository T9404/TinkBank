package com.academy.fintech.origination;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(
		exclude = {
				GsonAutoConfiguration.class,
				MultipartAutoConfiguration.class,
				WebSocketServletAutoConfiguration.class,
				NettyAutoConfiguration.class,
		}
)
public class OriginationApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(OriginationApplication.class)
				.beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
				.run(args);
	}
}
