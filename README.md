# Product service
The servie provides an endpoint that accepts product name and category as a search options.


### Run application
```
./gradlew bootRun 
```

### Build a docker image
```
./gradlew bootBuildImage
docker run -p 8080:8080 product-service
```

### API Dokumentation
[Swagger](http://localhost:8080/swagger-ui.html "http://localhost:8080/swagger-ui.html")

### Scalability

* Profile `dev` : (default) uses in-memory db and lucene (standalone only)  
* Profile `prod` : uses postgresql and elasticsearch (multiple instances) 

```
./gradlew -Dspring.profiles.active=prod bootRun
docker run -p 8080:8080 -e spring_profiles_active=prod product-service
```
