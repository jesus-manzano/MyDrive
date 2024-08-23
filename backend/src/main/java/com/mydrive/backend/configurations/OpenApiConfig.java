package com.mydrive.backend.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MyDrive API",
                version = "v1",
                description = "API for MyDrive Application"
        ),
        security = {
                @SecurityRequirement(name = "google_oauth2_scheme"),
                @SecurityRequirement(name = "dropbox_oauth2_scheme")
        }
)
@SecuritySchemes({
        @SecurityScheme(
                name = "google_oauth2_scheme",
                type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(
                        authorizationCode = @OAuthFlow(
                                authorizationUrl = "http://localhost:8080/api/google-drive/oauth/authorize" // URL de autorización de Google
                        )
                )
        ),
        @SecurityScheme(
                name = "dropbox_oauth2_scheme",
                type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(
                        authorizationCode = @OAuthFlow(
                                authorizationUrl = "http://localhost:8080/api/dropbox/oauth/authorize" // URL de autorización de Dropbox
                        )
                )
        )
})
public class OpenApiConfig {
}
