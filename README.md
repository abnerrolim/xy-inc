# XY_INC
This is a api generator to simple CRUD operations. Builded into play!framework 2.5 and mongo, allows to create auto CRUD operations only defining field names, types and validations. It's a beta version and a work in progress.

## Running
You need [mongodb](https://www.mongodb.com/download-center?jmp=nav#community) installed. The default installation instructions will be fine to testing the application, but if you have specific configurations, they can be changed into application.conf file:
```
playjongo.uri="mongodb://127.0.0.1:27017/xyinc"
playjongo.gridfs.enabled=false
```
To run the application, you just need run the follow command into terminal, inside the project folder. The first run will download all dependecies, so can be slow.
```
sbt run
```

And then go to http://localhost:9000 to see the running.

## Concepts
Basically, the application has two main entities: APIMetada, responsible to hold the api configuration and APIObject, that will manage the object values of CRUD. When you create a APIMetada, you define a unique name and a Json Schema. This schema is used to validate any PUT or POST requests for that API.
MongoDB was choose because is very flexible, but was wrapped by a Repository interface, so can be modified latter (if necessarY)

At this time, only the beta version of UI to create an API is implemented. Is the first screen on system, but you can call any url at routes file, passing json. The json need to bind with the APIObject, but the object any object that has been validated by Json Schema from current api.

## Forthcoming work
- [ ] better covarage of integration test at APIController (this version only shows the concept of testing)
- [ ] Autorization and autentication
- [ ] Multi-tenancy
- [ ] Full UI to allow edit api and test CRUD operations
- [ ] Docker image on travis
- [ ] Full Json Schema support on UI
- [ ] Auto-generated API documentation
- [ ] Clean unused sbt plugins (added by default)
