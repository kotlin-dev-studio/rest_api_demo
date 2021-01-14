## Kotlin, Spring Boot, MySQL, JPA, Hibernate Rest API

Build a base MVC RESTful API using Kotlin, Spring Boot, Mysql, JPA and Hibernate.

### Requirements

1. Kotlin 1.4.21

2. [Java](https://tecadmin.net/install-oracle-java-8-ubuntu-via-ppa/) - 1.8.x

3. Gradle - 6.7.1

4. Spring Boot - 2.4.0

5. [Mysql](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-16-04) - 5.7

6. [Intellij 2020.3](https://download.jetbrains.com/idea/ideaIU-2020.3.tar.gz) or [VS Code](https://code.visualstudio.com/download)

### Steps to Setup

1. Clone the application
   
```shell
git clone git@github.com:kotlin-dev-studio/rest_api_demo.git
```

2. Spring boot configuration

```shell
cp src/main/resources/application-dev.yml.example src/main/resources/application-dev.yml
cp src/main/resources/application-test.yml.example src/main/resources/application-test.yml
```

- open src/main/resources/application-{dev,tes}.yml
- change `spring datasource`: `username`, `password` with your mysql installation.

3. Create Mysql database

```shell
create database kotlin_demo_app;
create database kotlin_demo_app_test;
```

4. Run the app

    4.1. With Intellij:

    - Open `build.gradle.kts` then with `gradle` tab click on reload button.
    - Find `RestApiDemoApplication.kt` then click run button on top right.

    4.2. With VS Code:

    - Build gradle tasks [link](https://code.visualstudio.com/docs/java/java-build#_gradle)
    - Run spring boot [link](https://code.visualstudio.com/docs/java/java-spring-boot)

The app will start running at http://localhost:8080.

5. Swagger API Document

- Available at http://localhost:8080/swagger-ui/

- Explore APIs:

```shell
POST /register      # Register with username and password

POST /authenticate  # Authentication to get access token

# Required Authorization Bearer header

GET /home           # Return string Home

GET /sample/health  # Return JSON object {status: "Alive"}
```
