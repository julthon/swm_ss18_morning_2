/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Dominik
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecipesResourceIT {
    
    @Rule
    public JAXRSClientProvider provider = JAXRSClientProvider.buildWithURI("https://aughray.com/recipro-backend/api/recipes");
    
    
    @Test
    public void createAndFindRecipeById(){
        
        
        String title = "Bananenkuchen";
        String description = "Best recipe ever.";
        double rating = 3.7; 
        int preparationTime = 120;
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .add("SNACK")
                .build();
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject milk = recipeBuilder
                .add("name", "Milk")
                .build();
        
        JsonObject flour = recipeBuilder
                .add("name", "Flour")
                .build();
        
        JsonObject ingredient_milk = recipeBuilder
                .add("ingredient", milk)
                .add("quantity", "200ml")
                .build();
        
        JsonObject ingredient_flour = recipeBuilder
                .add("ingredient", flour)
                .add("quantity", "500g")
                .build();
        
        JsonArrayBuilder ingredientsListBuilder = Json.createArrayBuilder();
        JsonArray ingredients = ingredientsListBuilder
                .add(ingredient_milk)
                .add(ingredient_flour)
                .build();
        
        recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", title)
                .add("preparationTime", preparationTime)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", description)
                .add("ingredients", ingredients)
                .add("rating", rating)
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        String location = response.getHeaderString("Location");
        System.out.println("findRecipeById location " + location);
        
        response = this.provider
                .target(location)
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));

        JsonObject payload = response.readEntity(JsonObject.class);
        System.out.println("findRecipeById payload " + payload);

        int id = Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));
        
        assertThat(payload.getInt("id"), is(id));
        assertThat(payload.getString("title"), is(title));
        assertThat(payload.getInt("preparationTime"), is(preparationTime));
        assertThat(payload.getString("description"), is(description));
        assertThat(payload.getJsonArray("recipeTypes").size(), is(2));
        assertThat(payload.getJsonArray("recipeTypes"), is(recipeTypesToCreate));
        assertThat(payload.getJsonArray("ingredients"), is(ingredients));
        assertThat(payload.getJsonNumber("rating").doubleValue(), is(rating));
    }
    
    @Test
    public void findAllRecipes() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Gulasch")
                .add("preparationTime", 235)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Schnell und einfach")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("findAllRecipes payload " + payload);
        
        assert(payload.size() > 0);
    }
    
    @Test
    public void filterByTitle() {
        String title = "Torte";
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Sachertorte")
                .add("preparationTime", 120)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Chocolate cake.")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Erdbeertorte")
                .add("preparationTime", 180)
                .add("recipeTypes", recipeTypesToCreate)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Kaffeetorte")
                .add("preparationTime", 140)
                .add("recipeTypes", recipeTypesToCreate)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        
        response = this.provider.target()
                .queryParam("title", title.toLowerCase())
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByTitle payload " + payload);
        
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getString("title").contains(title.toLowerCase()) 
                                                || ((JsonObject) x).getString("title").contains(title.toUpperCase())
                                                || ((JsonObject) x).getString("title").contains(title)));
    }
    
    @Test
    public void filterPrepTime() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("SNACK")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Butterbrot")
                .add("preparationTime", 5)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Chocolate cake.")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));


        int minpreptime = 50;
        int maxpreptime = 200;
        response = this.provider.target()
                .queryParam("minpreptime", Integer.toString(minpreptime))
                .queryParam("maxpreptime", Integer.toString(maxpreptime))
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterPrepTime payload " + payload);
         
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getInt("preparationTime") >= minpreptime
                                            && ((JsonObject) x).getInt("preparationTime") <= maxpreptime));
    }
    
    @Test
    public void filterByType() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Schnitzel")
                .add("preparationTime", 100)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Very good.")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Auflauf")
                .add("preparationTime", 40)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Nice.")
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        response = this.provider.target()
                .queryParam("types", "MAIN_COURSE")
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByType payload " + payload);
        
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getJsonArray("recipeTypes").contains(recipeTypesToCreate.get(0))));
    }
    
}
