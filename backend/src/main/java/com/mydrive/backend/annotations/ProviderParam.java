package com.mydrive.backend.annotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        parameters = @Parameter(
                name = "provider",
                description = "Cloud provider",
                required = true,
                in = ParameterIn.PATH,
                schema = @Schema(allowableValues = {"google-drive", "dropbox"})
        )
)
public @interface ProviderParam {
}
