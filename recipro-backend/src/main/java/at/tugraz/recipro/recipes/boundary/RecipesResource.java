/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.boundary;

import at.tugraz.recipro.recipes.control.RecipesManager;
import at.tugraz.recipro.recipes.entity.Recipe;
import at.tugraz.recipro.recipes.entity.RecipeType;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Dominik
 */
@Path("recipes")
@Stateless
@Api(value = "RecipesResource")
public class RecipesResource {
    
    public static final String FILTER_TITLE = "title";
    public static final String FILTER_MIN_PREPARATION_TIME = "minpreptime";
    public static final String FILTER_MAX_PREPARATION_TIME = "maxpreptime";
    public static final String FILTER_TYPES = "types";
    
    @Inject
    RecipesManager recipesManager;
    
    @Context
    ServletContext servletContext;
    
    @POST
    public Response create(Recipe recipe, @Context UriInfo uriInfo) {
        Recipe savedRecipe = recipesManager.save(recipe);
        long id = savedRecipe.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + id).build();
        return Response.created(uri).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Recipe findById(@PathParam("id") long id) {
        return recipesManager.findById(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Recipe> filter(@DefaultValue("") @QueryParam(FILTER_TITLE) String title,
                               @DefaultValue("0") @QueryParam(FILTER_MIN_PREPARATION_TIME) int minpreptime,
                               @DefaultValue("999999") @QueryParam(FILTER_MAX_PREPARATION_TIME) int maxpreptime,
                               @QueryParam(FILTER_TYPES) List<String> types) {
        ArrayList<RecipeType> typeList = new ArrayList<>();
        if(types != null)
            typeList.addAll(types.stream()
                                 .map((String s) -> (RecipeType.fromString(s)))
                                 .collect(Collectors.toList()));
        return recipesManager
                .findAll()
                .stream()
                .filter((Recipe r) -> (
                        (title.isEmpty() || r.getTitle().toLowerCase().contains(title.toLowerCase())) && 
                        (r.getPreparationTime() > minpreptime && 
                         r.getPreparationTime() < maxpreptime)) && 
                        (typeList.size() == 0 || r.getRecipeTypes()
                                                  .stream()
                                                  .allMatch((RecipeType t) -> typeList.contains(t))))
                .collect(Collectors.toList());
    }
    
    @POST
    @Consumes("image/jpeg")
    @Path("{id}/image")
    public Response storeImage(@PathParam("id") long id, @Context UriInfo uriInfo, InputStream in, @HeaderParam("Content-Type") String fileType, @HeaderParam("Content-Length") long fileSize) throws IOException {
        //InputStream in, @HeaderParam("Content-Type") String fileType, @HeaderParam("Content-Length") long fileSize
        
        String fileName = id + ".jpg";
        String fullPath = servletContext.getRealPath("WEB-INF") + fileName;
        Files.copy(in, Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path("").build();
        return Response.created(uri).build();
    }
    
    @GET
    @Produces("image/jpeg")
    @Path("{id}/image")
    public InputStream  getImage(@PathParam("id") long id) throws IOException {
        
        String fileName = id + ".jpg";
        String fullPath = servletContext.getRealPath("WEB-INF") + fileName;
        java.nio.file.Path dest = Paths.get(fullPath);
        
        return Files.newInputStream(dest);
    }
}
