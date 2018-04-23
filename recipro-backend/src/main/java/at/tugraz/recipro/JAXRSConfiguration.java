package at.tugraz.recipro;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*
 * /register
 * /login
 *
 * /recipes (POST)
 *   takes: {"description": "Best recipe ever.", "preparationTime": 120, "recipeTypes": ["DESSERT","SNACK"], "title": "Bananenkuchen"}
 *   returns: 201 and location header for created recipe
 *
 * /recipes (GET)
 *   returns: 200 and [{"description": "Schnell und einfach" ,"id": 3, "preparationTime": 235, "recipeTypes": ["MAIN_COURSE"], "title": "Gulasch"}]
 * 
 * /recipes/{id} (GET)
 *   returns: 200 and {"id": {id}, "description": "Best recipe ever.", "preparationTime": 120, "recipeTypes": ["DESSERT","SNACK"], "title": "Bananenkuchen"}
 *
 * /recipes?title=kuchen&minpreptime=1&maxpreptime=999 (GET)
 *   returns: 200 and [{"preparationTime": 10, "recipeTypes": ["DESSERT"], "title": "Kuchen"}]
 *
 * /recipes/delete
 * /recipes/update
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

}
