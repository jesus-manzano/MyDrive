package com.mydrive.backend.annotations;

/**
 * Anotación personalizada para inyectar el parámetro `provider` en todos los endpoints que lo requieran.
 *
 * <p>Esta anotación se utiliza en los controladores para asegurar que el parámetro `provider` se
 * documente y sea requerido en las operaciones expuestas en Swagger. El `provider` indica el
 * proveedor de almacenamiento en la nube (por ejemplo, "google-drive" o "dropbox") y se pasa
 * como un parámetro en la URL.</p>
 *
 * <p>El parámetro `provider` es obligatorio y se espera que su valor sea uno de los siguientes:
 * <ul>
 *   <li>google-drive</li>
 *   <li>dropbox</li>
 * </ul>
 * </p>
 *
 * @see io.swagger.v3.oas.annotations.Operation
 * @see io.swagger.v3.oas.annotations.Parameter
 * @see io.swagger.v3.oas.annotations.enums.ParameterIn
 * @see io.swagger.v3.oas.annotations.media.Schema
 */
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
