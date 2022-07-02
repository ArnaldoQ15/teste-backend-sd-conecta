package br.sdconecta.testebackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@OpenAPIDefinition
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class TesteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesteBackendApplication.class, args);
    }

}
