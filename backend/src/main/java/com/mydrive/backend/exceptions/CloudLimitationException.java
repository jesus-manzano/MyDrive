package com.mydrive.backend.exceptions;

/**
 * Excepción personalizada que se lanza para indicar limitaciones en los servicios de almacenamiento en la nube.
 *
 * <p>Esta excepción se utiliza para señalar que una operación solicitada no puede ser realizada debido
 * a restricciones o limitaciones específicas del servicio en la nube. Hereda de {@link RuntimeException},
 * lo que la convierte en una excepción de tiempo de ejecución.</p>
 *
 * <p>La clase proporciona varios constructores para permitir la creación de instancias con un mensaje de
 * error, una causa subyacente, o ambos.</p>
 *
 * @see RuntimeException
 */
public class CloudLimitationException extends RuntimeException {

    /**
     * Constructor sin argumentos para {@code CloudLimitationException}.
     *
     * <p>Este constructor crea una excepción sin un mensaje de error ni una causa subyacente.</p>
     */
    public CloudLimitationException() {
        super();
    }

    /**
     * Constructor con mensaje de error para {@code CloudLimitationException}.
     *
     * <p>Este constructor permite especificar un mensaje de error que describe la razón de la excepción.</p>
     *
     * @param message El mensaje de error que describe la razón de la excepción.
     */
    public CloudLimitationException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje de error y causa para {@code CloudLimitationException}.
     *
     * <p>Este constructor permite especificar un mensaje de error y una causa subyacente para la excepción.
     * La causa se puede utilizar para proporcionar detalles adicionales sobre el error que causó la excepción.</p>
     *
     * @param message El mensaje de error que describe la razón de la excepción.
     * @param cause La causa subyacente de la excepción, o {@code null} si no hay ninguna.
     */
    public CloudLimitationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor con causa para {@code CloudLimitationException}.
     *
     * <p>Este constructor permite especificar una causa subyacente para la excepción, sin un mensaje de error.
     * La causa se puede utilizar para proporcionar detalles adicionales sobre el error que causó la excepción.</p>
     *
     * @param cause La causa subyacente de la excepción, o {@code null} si no hay ninguna.
     */
    public CloudLimitationException(Throwable cause) {
        super(cause);
    }
}
