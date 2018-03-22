# SAPE
**S**istema de apoyo a la gestión de la **A**signación de **P**royectos en **E**mpresas para la realización de estancias en prácticas y trabajo final de grado de Ingeniería en Informática.

## Desarrollo
Este projecto ha sido creado con, y por lo tanto requiere de:
- Java 9.0.4
- Gradle 4.6
- Lombok Plugin ([Intellij](https://projectlombok.org/setup/intellij), [Eclipse](https://projectlombok.org/setup/eclipse))

Para poder ser desarrolado correctamente.

## Ejecución
Para que la aplicación pueda ejecutarse correctamente, esta necesita conectarse a una base de datos.
La información de conexión a la base de datos debe estar almacenada en las siguientes variables de entorno para que la aplicación sea capaz de detectarlo automáticamente:
- `SAPE_URL` : `jdbc:postgresql://[dirección_url]:[puerto]/[ruta_base_de_datos]`
- `SAPE_USER` : `[usuario]`
- `SAPE_PASS` : `[contraseña]`

Para establecer estas variables de entorno puede usar el siguiente comando en Windows:
```powershell
setx [nombre_variable] [valor_variable] /M
```

Finalmente para iniciar la aplicación ejecute el siguiente comando, en el diretorio del projecto:
```powershell
./gradlew bootRun
```
