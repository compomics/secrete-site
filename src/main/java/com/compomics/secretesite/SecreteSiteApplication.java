package com.compomics.secretesite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication
public class SecreteSiteApplication {

	public static void main(String[] args) {
        SpringApplication.run(SecreteSiteApplication.class, args);
	}


	//TODO generate data.sql
}