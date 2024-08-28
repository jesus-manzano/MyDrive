package com.mydrive.backend.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la API de la aplicación MyDrive.
 *
 * <p>Esta clase está anotada con {@link Configuration} para que Spring la trate como una clase de configuración.
 * Utiliza las anotaciones de Swagger/OpenAPI para definir la información de la API y los esquemas de seguridad.</p>
 *
 * <p>La definición de OpenAPI incluye información sobre el título, versión y descripción de la API, así como los
 * requisitos de seguridad necesarios para acceder a los endpoints protegidos.</p>
 *
 * <p>Se configuran dos esquemas de seguridad para la autenticación mediante OAuth2, uno para Google y otro para
 * Dropbox. Estos esquemas se utilizan para proteger los endpoints de la API que requieren autenticación.</p>
 *
 * @see OpenAPIDefinition
 * @see SecurityScheme
 */
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
                                // URL de autorización de Google
                                authorizationUrl = "http://localhost:8080/api/google-drive/oauth/authorize"
                        )
                )
        ),
        @SecurityScheme(
                name = "dropbox_oauth2_scheme",
                type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(
                        authorizationCode = @OAuthFlow(
                                // URL de autorización de Dropbox
                                authorizationUrl = "http://localhost:8080/api/dropbox/oauth/authorize"
                        )
                )
        )
})
public class OpenApiConfig {
}
