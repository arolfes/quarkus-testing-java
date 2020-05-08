package mongodb;

import io.quarkus.runtime.annotations.QuarkusMain;

import static io.quarkus.runtime.Quarkus.run;

@QuarkusMain(name = "QuarkusHibernatePanacheApp")
public class Application {

    public static void main(String... args) {
        run(args);
    }

}