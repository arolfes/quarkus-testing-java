package mongodb;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MongoDbTestResource implements QuarkusTestResourceLifecycleManager {

    private MongodExecutable mongodb;

    @Override
    public Map<String, String> start() {
        Net net = null;
        try {
            Version.Main version = Version.Main.V4_0;
            // define explicitly localhost do run on VMs
            net = new Net("localhost", Network.getFreeServerPort(), Network.localhostIsIPv6());
            IMongodConfig config = new MongodConfigBuilder()
                    .version(version)
                    .net(net)
                    .build();
            mongodb = MongodStarter.getDefaultInstance().prepare(config);
            mongodb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> map = Stream.of(
                new AbstractMap.SimpleEntry<>("%test.quarkus.mongodb.connection-string", "mongodb://" + net.getBindIp() + ":" + net.getPort()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return map;
    }

    @Override
    public void stop() {
        if (mongodb != null) {
            mongodb.stop();
        }
    }
}
