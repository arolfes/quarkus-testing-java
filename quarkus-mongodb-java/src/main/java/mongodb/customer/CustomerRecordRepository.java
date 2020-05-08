package mongodb.customer;


import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CustomerRecordRepository implements PanacheMongoRepository<CustomerRecordDocument> {

    public List<CustomerRecordDocument> findByEmail(String email) {
        return list("email", email);
    }
}
