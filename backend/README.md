# Backend

## Project setup
Asegúrate de tener instalado **Java 17+** y **Maven**.  
Luego, instala las dependencias del proyecto ejecutando:

1. Clonar el repositorio y entrar en el backend:
   
   ```bash
   git clone https://github.com/tuusuario/mydrive.git
   cd mydrive/backend
   ```

3. Configurar `application.properties`:

   ```properties
   server.port=8080
   spring.datasource.url=jdbc:mysql://localhost:3306/mydrive
   spring.datasource.username=root
   spring.datasource.password=1234
   ```

- Añadir credenciales y claves OAuth de los proveedores de nube.

## Compiles and hot-reloads for development

1. Ejecutar aplicación
   
  ```bash
   mvn spring-boot:run
   ```

## Compiles and builds for production

1. Para crear un JAR listo para producción:

   ```bash
   mvn clean package
   ```

2. El JAR se encontrará en target/backend-0.0.1-SNAPSHOT.jar y se puede ejecutar con:

   ```bash
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```

## Lints and checks

   ```bash
   mvn verify
   ```
