# quarkus-jpa-java project

demonstrate quarkus panache with flyway and 2 different test containers.

`jpa.books.BookRepositoryTest` starts a PostgreSQL Container before the test is run and runs flyway when container is started. 

`jpa.books.BookRepositoryWithH2Test` starts a H2 in memory Container before the test is run and runs flyway when in memory db is ready .
