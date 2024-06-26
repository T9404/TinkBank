package com.academy.fintech;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
        exclude = {
                GsonAutoConfiguration.class,
                MultipartAutoConfiguration.class,
                WebSocketServletAutoConfiguration.class,
                NettyAutoConfiguration.class,
                DataSourceAutoConfiguration.class
        }
)
public class ScoringApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ScoringApplication.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .run(args);
    }
}
