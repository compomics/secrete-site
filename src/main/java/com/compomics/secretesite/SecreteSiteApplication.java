package com.compomics.secretesite;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;

@SpringBootApplication
@EnableAsync
public class SecreteSiteApplication {


    private static Environment env;

    @Autowired
    public SecreteSiteApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(SecreteSiteApplication.class, args);
        Arrays.stream(env.getActiveProfiles()).forEach(System.out::println);
    }


}