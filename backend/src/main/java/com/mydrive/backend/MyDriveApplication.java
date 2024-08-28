package com.mydrive.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal para la aplicación MyDrive.
 *
 * <p>Esta clase está anotada con {@link SpringBootApplication}, lo que indica que es el punto de entrada principal
 * para la aplicación. La anotación {@code @SpringBootApplication} combina las anotaciones {@code @Configuration},
 * {@code @EnableAutoConfiguration} y {@code @ComponentScan}, que configuran automáticamente la aplicación y
 * escanean los componentes necesarios.</p>
 *
 * <p>El método {@code main} es el punto de entrada de la aplicación.
 * Llama a {@link SpringApplication#run(Class, String...)} para iniciar la aplicación Spring Boot.</p>
 *
 * @see SpringBootApplication
 * @see SpringApplication
 */
@SpringBootApplication
public class MyDriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyDriveApplication.class, args);
	}

}
