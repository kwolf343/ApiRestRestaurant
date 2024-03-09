--- INSTRUCCIONES PARA BACKEND
1- Cargar la base de datos del script "Prueba_restauranteBD.sql" para poder hacer las pruebas
2- Modificar el archivo de propiedades en ruta \to-restaurant-app\backendRestaurant\src\main\resources\application.properties

--- Modificar usuario y password ---
spring.datasource.username=root
spring.datasource.password=admin

--- Modificar URL de conexion ---
cambiar "localhost" por la ruta del servidor donde está publicada la base de datos

spring.datasource.url=jdbc:mysql://localhost/Prueba_Restaurante?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

--- Modificar el puerto de arranque si se desea ---

server.port = 8080
El proyecto se correrá en localhost:8080

-- Modificar ruta de consumo --

El caso de consumirse desde postman, no es necesario modificar nada mas
En caso de consumirse desde un servidor externo, modificar el archivo "CorsConfig" en la ruta to-restaurant-app\backendRestaurant\src\main\java\com\example\config

.allowedOrigins("http://localhost:4200")

Se consumirá desde http://localhost:4200
