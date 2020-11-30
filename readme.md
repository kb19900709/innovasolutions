# Innova solutions
[![Build Status](https://travis-ci.org/kb19900709/innovasolutions.svg?branch=master)](https://travis-ci.org/github/kb19900709/innovasolutions)  
**TBD**: travis CI integration with sonarqube and newman 

## Main Tech
- java8
- Spring Boot / Spring MVC

## Flow
![validation flow](validation_flow.png?raw=true)

## Core Class
- [PasswordValidationService](https://github.com/kb19900709/innovasolutions/blob/master/src/main/java/com/innova/service/impl/PasswordValidationService.java), [PasswordValidationServiceTest](https://github.com/kb19900709/innovasolutions/blob/master/src/test/java/com/innova/service/impl/PasswordValidationServiceTest.java)
- [ValidationController](https://github.com/kb19900709/innovasolutions/blob/master/src/main/java/com/innova/controller/ValidationController.java), [ValidationControllerTest](https://github.com/kb19900709/innovasolutions/blob/master/src/test/java/com/innova/controller/ValidationControllerTest.java)

## How to run and test
1. Make sure your device supports the following
   - git
   - maven
   - docker   
2. Clone the repository `git clone https://github.com/kb19900709/innovasolutions.git` 
3. cd to the repository, then execute `sh buildAndRun.sh var1`. **var1** is an optional var, denote a port which the application will run at. The default port is **8080** if **var1** is empty, and also it's the default port for postman collection test.
4. [buildAndRun.sh](https://github.com/kb19900709/innovasolutions/blob/master/buildAndRun.sh) will mainly do the following
   1. maven clean and package
   2. extract project information for building a new docker image
   3. build the docker image
   4. run the image
5. You can continuously execute the above shell, it'll automatically delete the old one.
6. If you have postman([collection runner](https://learning.postman.com/docs/running-collections/intro-to-collection-runs/)) or [newman](https://learning.postman.com/docs/running-collections/using-newman-cli/command-line-integration-with-newman/) in your device, import **Innova-validation.postman_collection.json** and run it on postman collection runner or execute `newman run Innova-validation.postman_collection.json`. Note that the test is running with `localhost:8080`, so if you change the port while running **buildAndRun.sh**, please test it manually.

## Specification
HTTP method | Host      |Port|Path                |Body                      |
------------| ----------|----|--------------------|--------------------------|
POST        | localhost |8080|validation/password |{"password":"password123"}|

```shell
curl --location --request POST 'localhost:8080/validation/password' \
--header 'Content-Type: application/json' \
--data-raw '{
	"password":"password123"
}'
```

## Test Case
You can see the following data in [ValidationControllerTest](https://github.com/kb19900709/innovasolutions/blob/master/src/test/java/com/innova/controller/ValidationControllerTest.java) for integration test.

password      | valid
--------------|---------
aaa123        | O
abcab123      | O
1abc2abc      | O
abcab122123   | O
bbbabbbc123   | O 
abab123       | X
abcab122122   | X
345aabaab678  | X
345baabaaa678 | X
aabbaabb123   | X
aaaaaa123 	  | X	
aaaa123       | X
123aaaaaa     | X
123aaaa       | X
ab12		  | X
ab12bc12cc12d | X
abcde         | X
12345         | X
11abcabc11    | X
