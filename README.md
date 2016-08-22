# frisk2
> A modern webapp for tracking frisk consumption

## Prerequisites

- Postgres
- Maven
- Java 8

## Install and run application

### Database

1. `psql postgres`
2. `CREATE USER frisk;`
3. `CREATE DATABASE frisk2;`
4. `ALTER DATABASE frisk2 OWNER TO frisk;`
5. `\q` (to exit psql)
5. Start server to run first migration which creates the tables (see below)
5. `psql frisk2 < test_data.sql` (this will fill the database with test data)

### Server 

#### jar

1. `mvn clean install`
2. `java -jar target/frisk.jar server frisk.yml`

#### IntelliJ

1. Edit configurations
2. Add new configuration with type Application
3. Select `FriskApplication` as main class
4. Enter `server frisk.yml` as program arguments
