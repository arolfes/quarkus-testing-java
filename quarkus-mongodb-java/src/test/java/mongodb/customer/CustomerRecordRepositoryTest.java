package mongodb.customer;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import mongodb.MongoDbTestResource;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(MongoDbTestResource.class)
class CustomerRecordRepositoryTest {

    @Inject
    private CustomerRecordRepository customerRecordRepository;

    @BeforeEach
    public void clearDb() {
        customerRecordRepository.deleteAll();
    }

    @Test
    public void customerCanBePersistedAndFound() {
        ObjectId id = ObjectId.get();
        CustomerRecordDocument cust = new CustomerRecordDocument(id, "customer1", "email@github.com");
        customerRecordRepository.persist(cust);

        CustomerRecordDocument found = customerRecordRepository.findById(id);
        assertThat(found).isEqualToComparingFieldByField(cust);
    }

    @Test
    public void customersCanBeFoundByName() {

        CustomerRecordDocument cust1 = new CustomerRecordDocument("customer1", "email@github.com");
        customerRecordRepository.persist(cust1);
        CustomerRecordDocument cust2 = new CustomerRecordDocument("customer2", "email2@github.com");
        customerRecordRepository.persist(cust2);
        CustomerRecordDocument cust3 = new CustomerRecordDocument("customer3", "email@github.com");
        customerRecordRepository.persist(cust3);

        List<CustomerRecordDocument> foundCustomers = customerRecordRepository.findByEmail("email@github.com");
        assertThat(foundCustomers).contains(cust1, cust3).doesNotContain(cust2);

    }
}