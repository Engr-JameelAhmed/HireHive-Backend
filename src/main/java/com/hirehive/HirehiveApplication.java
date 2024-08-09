package com.hirehive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@EnableTransactionManagement
public class HirehiveApplication {

	public static void main(String[] args) {
		log.info("Hire-Hive    Running.........");
		SpringApplication.run(HirehiveApplication.class, args);
	}



}
