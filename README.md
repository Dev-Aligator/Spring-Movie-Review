
Welcome to the GitHub repository for the Spring Boot **Movie Review Application**! This project is built with the Spring Boot framework, which provides a powerful and flexible foundation for creating web applications.

The purpose of this application is to provide users with a platform for reading and writing reviews of their favorite movies. Users can browse a database of movies, read reviews written by other users, and write their own reviews. The application also allows users to rate movies and search for movies based on specific criteria.

The front-end of the application is built using HTML, CSS, and JavaScript, while the back-end is built using Spring Boot and PostgreSQL. The application is designed to be scalable and easily extensible, making it suitable for a wide range of use cases.


## Dependency
– If you want to use PostgreSQL:
```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```
- For Thymeleaf template engine
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

## Configure Spring Datasource, JPA, App properties
Open `src/main/resources/application.properties`
- For PostgreSQL:
```
spring.datasource.url= jdbc:postgresql://localhost:5432/dsi
spring.datasource.username= postgres
spring.datasource.password= root

spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect
```

# Hibernate ddl auto (create, create-drop, validate, update)
```
spring.jpa.hibernate.ddl-auto= update
```
## Run Spring Boot application
```
mvn spring-boot:run
```
## Follow the below steps
```
1. First install postgresql 
2. Postgesql  default usename is : postgres
3. Postgresql database password should be : root
4. Need to create a database named `dsi`
5. Run the code, that will create all tables in `dsi` database.
6. Then signin as an admin using username `admin` and password `admin` similarly for `user`, `editor` and `creator`
```
