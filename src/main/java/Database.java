

import java.util.ArrayList;
import java.util.List;

//
public class Database {

    private static List<Apartment> apartments = new ArrayList<>();

    public static List<Apartment> getApartments() { return apartments; }


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
