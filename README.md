<pre><b>
                  ~         _______ ___ ___     _______           
       (o)--(o)            |       |   |   |   |       |           
      /.______.\     .     |    ___|   |   |   |    ___|           
      \________/   ~       |   |___|   |   |   |   |___            
     ./        \.          |    ___|   |   |___|    ___|           
    ( .        . )         |   |   |   |       |   |___            
     \ \_\\//_/ /          |___|   |___|_______|_______|           
   _ _##__##_ ##__ _______ ______   ___ __    _ _______            
  |       |  | |  |   _   |    _ | |   |  |  | |       |           
  |  _____|  |_|  |  |_|  |   | || |   |   |_| |    ___| __ _ _    
  | |_____|       |       |   |_||_|   |       |   | __  \ \ \ \   
  |_____  |       |       |    __  |   |  _    |   ||  |  \ \ \ \  
   _____| |   _   |   _   |   |  | |   | | |   |   |_| |   ) ) ) ) 
  |_______|__| |__|__| |__|___|  |_|___|_|  |__|_______|  / / / /  
 ========================================================/_/_/_/   
 </b></pre>
 
### Web-application which allow user save files in a server, have the ability to download, listen, view and share files with other users.

![File Sharing Service](https://raw.githubusercontent.com/srcmaxim/file-sharing-system/develop/src/main/resources/static/img/frog-icon-256.png)

[![Maintainability](https://api.codeclimate.com/v1/badges/c0feb1c4b2b504a5da9d/maintainability)](https://codeclimate.com/github/srcmaxim/file-sharing-system/maintainability)
[![Coverage](https://img.shields.io/badge/coverage-75%25-brightgreen.svg)](https://codeclimate.com/github/srcmaxim/file-sharing-system/maintainability)
[![Issue Count](https://img.shields.io/badge/issues-7-blue.svg)](https://codeclimate.com/github/srcmaxim/file-sharing-system)

## Wiki 

All project information and description can be found at [wiki page](https://github.com/srcmaxim/file-sharing-system/wiki)

## Scrum board 

All project issues and its current situation can be found at [scrum board page](https://github.com/srcmaxim/file-sharing-system/projects/1)

## Issues 

All project issues can be found at [isssues page](https://github.com/srcmaxim/file-sharing-system/issues)

## Class Diagramm

[![Class Diagramm](https://user-images.githubusercontent.com/11833383/28808158-8ff08e14-7682-11e7-8795-13aa2e3d5b4f.jpg)](https://github.com/srcmaxim/file-sharing-system/issues/2)

## Build and Run

First of all you need to configure the database. 

> By default application is using MySQL database.
> You can define params for DB in `application.properties` or entity variables.
```
DB_URL, db.default-url
DB_USER,db.default-user
DB_PWD, db.default-password
```

>Also you need to configure mail account with user and password.
> You can define params for DB in `application.properties` or entity variables.
```
MAIL_USER, mail.username
MAIL_PWD, mail.password
```

## Depelopment

- `gradlew bootRun` -- to start server for development.
- `gradlew build` -- to build server for production.

> By default server will be running on port `8080`.

## Testing

- `gradlew test` - to run unit tests.
- `gradlew itest` - to run integration tests.

## Technologies used

- [spring-boot](https://projects.spring.io/spring-boot/)
- [spring-mvc](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html)
- [spring-data-jpa](http://projects.spring.io/spring-data-jpa/)
- [spring-security](https://projects.spring.io/spring-security/)
- [lombok](https://projectlombok.org/)
- [junit](http://junit.org/junit4/)
- [gradle](https://gradle.org/)
- [mysql](https://www.mysql.org/)
- [thymeleaf](http://www.thymeleaf.org/)
- [spring-mail](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-email.html)

## License
spring-boot-angular4-boilerplate is released under the [MIT License](https://opensource.org/licenses/MIT).