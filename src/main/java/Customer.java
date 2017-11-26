
import java.util.List;



public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private List<Apartment> apartments;

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<Apartment> getApartments() { return apartments; }

    public void setApartments (List<Apartment> apartments) { this.apartments = apartments; }
}
