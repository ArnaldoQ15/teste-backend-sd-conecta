package br.sdconecta.testebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class TesteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesteBackendApplication.class, args);
    }

}
