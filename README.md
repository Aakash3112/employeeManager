# Employee Manager

## Requirements

For building and running the application you need:

- [JDK 17](https://jdk.java.net/java-se-ri/17-MR1)
- [Maven 3](https://dlcdn.apache.org/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz)

## Swagger Documentation

- Swagger Documentation available at http://[domain/ip]:[port]/swagger-ui/index.html

## Postgres SQL setup

- Install psql
- Use the following command in cmd to create db and user in postgres
  - psql -U postgres (to start psql command line tool)
  - CREATE DATABASE employeedb WITH ENCODING 'UTF8;
  - CREATE USER employee WITH LOGIN PASSWORD 'employee@123';
  - GRANT TEMP ON DATABASE employeedb TO employee;
  - GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO employee;
  - GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO employee;
  - GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO employee;
- Login to postgres using the created the user and password
  - psql -U employee -d employeedb
  - Enter the password given and run the scripts present in src\main\resources\schema.sql