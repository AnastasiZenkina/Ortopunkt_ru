package com.example.medicalcrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicalcrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalcrmApplication.class, args);
    }

}
