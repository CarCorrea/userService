
Este proyecto es un microservicio REST desarrollado en Spring Boot, diseñado para la creación y consulta de usuarios siguiendo estándares de arquitectura y validaciones específicas. Se basa en una estructura modular con capas bien definidas y autenticación mediante JWT.
Tecnologías utilizadas
- Java 11
- Spring Boot 2.5.14
- Spring Data JPA 
- JWT 
- JUnit
- Gradle 7.4

Arquitectura del sistema
El sistema sigue un modelo basado en MVC (Modelo-Vista-Controlador) y se compone de los siguientes elementos:
- Controlador (UserController)
- Maneja las solicitudes HTTP (/sign-up y /login).
- Retorna respuestas en formato JSON con códigos HTTP adecuados.
- Servicio (UserService)
- Contiene la lógica de negocio.
- Valida y persiste los datos utilizando Spring Data JPA.
- Gestiona la autenticación con JWT y actualización de tokens.
- Repositorio (UserRepository)
- Se encarga del acceso a la base de datos H2.
- Utiliza Spring Data JPA para consultas y persistencia.
- Modelo (User)
- Representa la entidad de usuario con campos como id, created, lastLogin, token, etc.
- Incluye validaciones de email y contraseña mediante expresiones regulares.

Pasos:
1 - Clonar el repositorio:

git clone https://github.com/CarCorrea/userService.git

2 - Compilar y ejecutar el servicio:
./gradlew build
./gradlew bootRun

3 - Acceder a los endpoints:
/userService
POST /user-registration
POST /login
GET /{id}

Endpoints del sistema

1. Creación de usuario (POST /user-registration)
  {
  "name": "Juan Pérez2",
  "email": "juan.perez2@example.com",
  "password": "Abcdefg1",
  "phones": [
    {
      "number": 123456789,
      "citycode": 1,
      "countrycode": 57
    }
  ]
}

Ejemplo de salida: 
{
    "userId": "27ac31c6-27aa-4faa-affa-72e4b16df72b",
    "email": "juan.perez2@example.com",
    "dateCreated": "2025-06-01T19:46:13.074725500",
    "lastLogin": "2025-06-01T19:46:13.074725500",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuLnBlcmV6MkBleGFtcGxlLmNvbSIsImlhdCI6MTc0ODgyMTU3MywiZXhwIjoxNzQ4OTA3OTczfQ.HmHnFbbt6i0R2eghYUfZjwb7JK7Kjo4SEQP8Akw8kGw",
    "active": true
}

Ejemplo en caso de error en la entrada
{
    "error": [
        {
            "code": 400,
            "description": "La contraseña debe tener solo una mayúscula, un máximo de 2 números y entre 8 a 12 caracteres",
            "timestamp": "2025-06-01T19:47:09.3517018"
        }
    ]
}

2. Autenticación de usuario (POST /login)

Requiere el token generado en /sign-up
Salida JSON en caso de éxito:
{
    "userId": "316c4a6f-2636-4efa-bbc8-d90abdf05205",
    "email": "juan.perez112@example.com",
    "dateCreated": "2025-06-01T19:49:06.503207",
    "lastLogin": "2025-06-01T19:49:21.845759700",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuLnBlcmV6MTEyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzQ4ODIxNzYxLCJleHAiOjE3NDg5MDgxNjF9.VMET8ndmMORfM_C3r7acqSirRXMESxp_juhnjoEzZQU",
    "active": true
}

3. Buscar Usuario (GET /{id}
Requiere el token generado en /sign-up
Salida JSON en caso de éxito:

{
    "userId": "316c4a6f-2636-4efa-bbc8-d90abdf05205",
    "email": "juan.perez112@example.com",
    "dateCreated": "2025-06-01T19:49:06.503207",
    "lastLogin": "2025-06-01T19:49:21.845760",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuLnBlcmV6MTEyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzQ4ODIxNzYxLCJleHAiOjE3NDg5MDgxNjF9.VMET8ndmMORfM_C3r7acqSirRXMESxp_juhnjoEzZQU",
    "active": true
}

- Creación de usuario
- El cliente envía una solicitud POST /user-registration con los datos del usuario.
- El controlador valida los datos y los pasa al servicio.
- Se genera un UUID y se persisten los datos en H2.
- Se devuelve un token JWT válido junto con la información del usuario.
- Autenticación (login)
- El usuario envía su solicitud con el token generado.
- El filtro de autenticación JWT valida el token.
- Se busca el usuario en la base de datos y se retorna su información.
- Se genera un nuevo token, reemplazando el anterior.
- Manejo de errores
- Si el usuario no existe, se retorna un mensaje JSON de error con un código HTTP 400.
- Si los datos son inválidos (email o password incorrectos), se devuelve un mensaje de error con detalles.

