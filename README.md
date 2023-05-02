# Vacation-pay-calculator

## Обзор
Тестовый проект **RESTfull API service** представляет собой "Калькулятор отпускных"

## Использованные технологии
- Java 11
- Spring Boot
- Maven
- Lombok
- Swagger (Open API)

### API
    GET api/v1/calculate/{avgSalary}/{numDays}/{startDate}

avgSalary - среднюя зарплата за 12 месяцев  
numDays - количество дней отпуска  
startDate (необязательный) - дата начала отпуска  

Ответ: сумма отпускных за {numDays} дней.  
Если указать {startDate} дату, то при расчете не учитываются праздники

Для определения выходных и праздничных дней сервис использует сторонний сервис  
https://www.isdayoff.ru


### Swagger (Open API):
http://localhost:8080/swagger-ui.html