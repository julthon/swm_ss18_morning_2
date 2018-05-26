package at.tugraz.recipro;

import at.tugraz.recipro.recipes.entity.Allergen;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author julian
 */
@Startup
@Singleton
public class StaticDataInitializer {
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        initAllergens();
    }
    
    private void initAllergens() {
        Allergen allergens[] = {new Allergen("P", "Peanuts"),
                                new Allergen("N", "Nuts"),
                                new Allergen("Mi", "Milk"),
                                new Allergen("So", "Soya"),
                                new Allergen("Mu", "Mustard"),
                                new Allergen("L", "Lupin"),
                                new Allergen("E", "Eggs"),
                                new Allergen("F", "Fish"),
                                new Allergen("Sh", "Shellfish"),
                                new Allergen("Mo", "Molluscs"),
                                new Allergen("G", "Cerials containing gluten"),
                                new Allergen("Se", "Sesame"),
                                new Allergen("C", "Celery"),
                                new Allergen("Su", "Sulphur dioxide")};
        Arrays.stream(allergens).forEach((Allergen a) -> em.persist(a));
    }
}
