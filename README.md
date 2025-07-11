# **Bank_s_recommendation**

Учебный проект: сервис рекомендаций кредитного продукта для пользователей банка.

### 1. Описание окружения.  
JDK 17,  
Project Maven,  
Spring Boot 3.4.3,  
встраиваемая база данных H2 (файл transaction.mv.db),
база данных PostgreSQL,  
телеграмбот

### 2. Шаги развертывания. 
#### *Установка зависимостей:*  
модуль Spring Web,  
JDBC-драйвер для работы с базой данных H2,  
зависимость для работы с postgresql,  
зависимость для работы со Swagger,  
зависимость для работы с springdoc-openapi,  
зависимости для тестирования приложения,  
зависимость для работы со Spring Cache,  
зависимости для работы с telegram bots,
зависимости для генерации информации о сборке

#### *Настройка конфигураций (application.properties)*

server.port=8080

#Подключение одной базы данных
spring.datasource.url=jdbc:postgresql://localhost:5433/bank
spring.datasource.username=student
spring.datasource.password=chocolatefrog
spring.datasource.driver-class-name=org.postgresql.Driver

#Подключение второй базы данных
application.recommendations-db.url=jdbc:h2:file:./transaction

#Настройка Hibernate  
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql = true



#Подключение телеграмбота  
telegram.bot.token=***  


### 3. Запуск приложения.
#### *Команды для сборки приложения*
mvn clean package
#### *Команда для запуска приложения*


### 4. Над проектом работали:  
Авдеев Виталий  
Антепа Андрей  
