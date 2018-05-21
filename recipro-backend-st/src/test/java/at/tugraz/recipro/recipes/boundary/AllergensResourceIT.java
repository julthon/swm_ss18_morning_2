/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import javax.json.JsonArray;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author Richard
 */
public class AllergensResourceIT {
    @Rule
    public JAXRSClientProvider provider = JAXRSClientProvider.buildWithURI("http://localhost:8080/recipro-backend/api/allergens");
    
    @Test
    public void getAllAllergens() {
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("getAllAllergens payload " + payload);
        assertThat(payload.size(), is(14));
    }
}
