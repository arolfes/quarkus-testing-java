# quarkus-mongodb-java project

Demonstrate how easy is testing a mongodb entity.

Implementation for BookRecord uses [active record pattern](https://www.martinfowler.com/eaaCatalog/activeRecord.html)

Implementation for CustomerRecord uses [repository pattern](https://martinfowler.com/eaaCatalog/repository.html)

_I personally likes the repository pattern more. maybe it's just I'm more used to it_

_I used the ObjectId from mongodb driver instead of java.util.UUID to avoid register a converter for it._

For more details:
* [mongodb with panache](https://quarkus.io/guides/mongodb-panache)
* [flapdoodle mongodb testcontainer](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)


### generate project structure

```
mvn io.quarkus:quarkus-maven-plugin:1.4.2.Final:create \
    -DprojectGroupId=de.novatec.aqe.cloud \
    -DprojectArtifactId=quarkus-mongodb-kotlin \
    -DprojectVersion=1.0.0-SNAPSHOT \
    -DclassName="mongodb.Application" \
    -Dextensions="mongodb-panache" \
    -DbuildTool=gradle
```