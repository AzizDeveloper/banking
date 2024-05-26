package dev.aziz.bankingservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Aziz",
                        email = "abdukarimovtheaziz.com"
                ),
                description = "OpenApi documentation for banking service",
                title = "OpenApi specification - Azizdev",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:1243"
                )
        }
)

public class OpenApiConfig {
}