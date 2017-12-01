import com.fasterxml.jackson.databind.ObjectMapper;

import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.enterprise.context.RequestScoped;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.client.Client;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("customers")
public class CustomerResource {

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

    @GET
    public Response getAllCustomers() {
        List<Customer> customers = Database.getCustomers();
        return Response.ok(customers).build();
    }

    @GET
    @Path("{customerId}")
    public Response getCustomer(@PathParam("customerId") String customerId) {
        Customer customer = Database.getCustomer(customerId);
        List<Apartment> apartments = getApartments(customerId);

        customer.setApartments(apartments);

        return customer != null
                ? Response.ok(customer).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addNewCustomer(Customer customer) {
        Database.addCustomer(customer);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{customerId}")
    public Response deleteCustomer(@PathParam("customerId") String customerId) {
        Database.deleteCustomer(customerId);
        return Response.noContent().build();
    }

    public List<Apartment> getApartments(String customerId){

        if(baseUrl.isPresent()) {
            try {
                System.out.println(baseUrl.get() + "/v1/apartments/customer/" + customerId);
                return httpClient.target(baseUrl.get() + "/v1/apartments/customer/" + customerId).request()
                        .get(new GenericType<List<Apartment>>() {});


                //basePath = http://localhost:8080
                //HttpGet request = new HttpGet("http://localhost:8080" + "/v1/apartments/customer/" + customerId);
                //HttpGet request = new HttpGet(baseUrl + customerId);
                //HttpResponse response = httpClient.execute(request);

                /*int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();

                    if (entity != null)
                        return getObjects(EntityUtils.toString(entity));
                } else {
                    String msg = "Remote server '" + "http://localhost:8080" + "' is responded with status " + status + ".";
                    throw new InternalServerErrorException(msg);
                }*/

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
