package com.project.myfirstproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyfirstprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyfirstprojectApplication.class, args);
    }

}
