package com.springliviu.expensetracker;

import com.springliviu.expensetracker.config.JwtProperties;
import com.springliviu.expensetracker.config.SwaggerProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({
        JwtProperties.class,
        SwaggerProperties.class
})
public class ExpenseTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner printProperties(JwtProperties jwt, SwaggerProperties swagger) {
        return args -> {
            System.out.println("🔐 JWT Config:");
            System.out.println("  Secret:     " + jwt.getSecret());
            System.out.println("  Expiration: " + jwt.getExpiration());

            System.out.println("\n📘 Swagger Config:");
            System.out.println("  Title:       " + swagger.getTitle());
            System.out.println("  Description: " + swagger.getDescription());
            System.out.println("  Version:     " + swagger.getVersion());
        };
    }
}
