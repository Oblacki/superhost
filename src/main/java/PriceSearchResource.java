import com.fasterxml.jackson.databind.ObjectMapper;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.enterprise.context.RequestScoped;
import com.kumuluz.ee.logs.cdi.Log;
import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("price-search")
@Log
public class PriceSearchResource {

    private Client httpClient;
    private ObjectMapper objectMapper;

    @Inject
    @DiscoverService(value = "apartments", version = "1.0.x", environment = "dev")
    private Optional<String> baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        objectMapper = new ObjectMapper();
    }

    @Inject
    private RestProperties restProperties;


    @GET
    public Response getApartmentsFilteredByPrice(
            @QueryParam("min") double minPrice,
            @QueryParam("max") double maxPrice) {

        System.out.println("minPrice: "+minPrice + " maxprice: "+maxPrice);
        List<Apartment> apartments = null;

        Optional<Boolean> apartmentServiceEnabled = ConfigurationUtil.getInstance().getBoolean("rest-properties.external-services.apartment-service.enabled");
        System.out.println("----------------------" +apartmentServiceEnabled.get()+ "-------------------------");


        System.out.println("---------------" + restProperties.isApartmentServiceEnabled() + "---------------------");
        if(restProperties.isApartmentServiceEnabled())
            apartments = filterApartmentsByPrice(minPrice, maxPrice);

        return apartments != null
                ? Response.ok(apartments).build()
                : Response.status(Response.Status.NOT_FOUND).build();

    }

    public List<Apartment> filterApartmentsByPrice(double minP, double maxP){

        if(baseUrl.isPresent()) {
            try {
                if (minP <= 0) minP = 0;
                if (maxP <= 0) maxP = Double.MAX_VALUE;
                System.out.println(baseUrl.get() + "/v1/apartments/price-search?minPrice=" + minP + "&maxPrice=" + maxP);
                return httpClient.target(baseUrl.get() + "/v1/apartments/price-search?minPrice=" + minP + "&maxPrice=" + maxP).request()
                        .get(new GenericType<List<Apartment>>() {});


            } catch (Exception e) {
                String msg = e.getClass().getName() + " occured: " + e.getMessage();
                throw new InternalServerErrorException(msg);
            }
        }
        else
            return new ArrayList<>();
    }

    private List<Apartment> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Apartment.class));
    }
}
