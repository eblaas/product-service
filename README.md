# Product service

### API Dokumentation
http://localhost:8080/swagger-ui.html

### How to run application
```
./gradlew bootRun 
```

### Build a docker image
```
./gradlew bootBuildImage
docker run -p 8080:8080 product-service
```

### Profiles
* `dev` (default) : uses in-memory db and lucene  
* `prod` : uses postgresql and elasticsearch ( -e spring_profiles_active=prod)
