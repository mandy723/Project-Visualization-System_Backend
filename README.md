# RVS-springboot

### Reference

### Run App Step
1. set java home:  https://javatutorial.net/set-java-home-windows-10
2. install mysql: https://dev.mysql.com/downloads/mysql/
3. open cmd window
4. cd ./target
5. java -jar springboot-1.0-SNAPSHOT.jar
6. open Postman
7. test Get request : http://localhost:9100/commits/facebook/react
8. test Post request : http://localhost:9100/commits/facebook/react
9. rescan project with SonarQube 
```shell
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=cfbb3bf3789419b9999d9bac7e87876b86257771
```
