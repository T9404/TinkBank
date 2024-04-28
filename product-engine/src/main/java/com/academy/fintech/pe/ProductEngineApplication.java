package com.academy.fintech.pe;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
		exclude = {
				GsonAutoConfiguration.class,
				MultipartAutoConfiguration.class,
				WebSocketServletAutoConfiguration.class,
				NettyAutoConfiguration.class,

				RestTemplateAutoConfiguration.class,
				HttpMessageConvertersAutoConfiguration.class,
				HttpEncodingAutoConfiguration.class,
				ErrorMvcAutoConfiguration.class
	}
)
public class ProductEngineApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ProductEngineApplication.class)
				.beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
				.run(args);
	}
}
