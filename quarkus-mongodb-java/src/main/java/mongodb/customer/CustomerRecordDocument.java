package mongodb.customer;

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.types.ObjectId;

import java.util.Objects;

@MongoEntity(database = "customer", collection = "person_record")
public class CustomerRecordDocument {

    public ObjectId id;
    public String name;
    public String email;

    public CustomerRecordDocument() {
    }

    public CustomerRecordDocument(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public CustomerRecordDocument(ObjectId id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerRecordDocument that = (CustomerRecordDocument) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return "CustomerRecordDocument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
