

import java.util.ArrayList;
import java.util.List;

//
public class Database {

    private static List<Customer> customers = new ArrayList<>();
    private static List<Apartment> apartments = new ArrayList<>();

    public static List<Customer> getCustomers() {
        return customers;
    }
    public static List<Apartment> getApartments() { return apartments; }

    //customers
    public static Customer getCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId))
                return customer;
        }
        return null;
    }

    public static void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public static void deleteCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                customers.remove(customer);
                break;
            }
        }
    }

    //apartments
    public static void deleteApartment(String apartmentId) {
        for (Apartment apartment : apartments) {
            if (apartment.getId().equals(apartmentId)) {
                apartments.remove(apartment);
                break;
            }
        }
    }

    public static Apartment getApartment(String apartmentId) {
        for (Apartment apartment : apartments) {
            if (apartment.getId().equals(apartmentId))
                return apartment;
        }
        return null;
    }

    public static void addApartment(Apartment apartment) {
        apartments.add(apartment);
    }

    public static List<Apartment> getApartmentByCustomerId(String customerId) {
        List<Apartment> filteredApartments = new ArrayList<Apartment>();

        for (Apartment apartment : apartments) {
            if (apartment.getCustomerId().equals(customerId)) {
                System.out.println(apartment.toString());
                filteredApartments.add(apartment);
            }
        }
        return filteredApartments;
    }

}
