# Run Library migration in Docker

To run Library migration in Docker both `library` and `library-migration` folders must be placed in the same parent folder, referred to as `otus-spring-hw` (which is already true if you git cloned the repository).

1\. Create docker network for communication between library services

> docker network create otus-library-network

2\. Bring up Library app and its Mongo database  

> cd otus-spring-hw/library

_[optional] Place custom Maven settings XML at path `otus-spring-hw/library/maven-settings/settings.xml` and it will be used during build process.

> docker-compose up --build

3\. Bring up Library Migration app and its Postgresql database  

> cd otus-spring-hw/library-migration

_[optional] Place custom Maven settings XML at path `otus-spring-hw/library-migration/maven-settings/settings.xml` and it will be used during build process._

> docker-compose up --build

4\. Attach terminal to Library Migration shell

> docker attach library-migration

See current statistics

> shell:>stats

```
Source DB
---------------
Authors:        3
Genres:         4
Books:          5
Comments:       2

Target DB
---------------
Authors:        table doesn't exist
Genres:         table doesn't exist
Books:          table doesn't exist
Comments:       table doesn't exist
```

Start migration

> shell:>migrate

```
Migration is completed

Post migration statistics:
-------------------------
Source DB
---------------
Authors:        3
Genres:         4
Books:          5
Comments:       2

Target DB
---------------
Authors:        3
Genres:         4
Books:          5
Comments:       2
```