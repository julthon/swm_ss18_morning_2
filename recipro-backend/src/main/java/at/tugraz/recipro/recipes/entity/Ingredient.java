package at.tugraz.recipro.recipes.entity;

import java.util.ArrayList;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Edith, Julian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Ingredient.FIND_ALL, query = "SELECT i FROM Ingredient i")
})
public class Ingredient {
    static final String PREFIX = "recipes.entity.Ingredient.";
    public static final String FIND_ALL = PREFIX + "findAll";
    
    private String name;
    
    @Id
    @GeneratedValue
    @JsonbTransient
    private long id;
    
    private List<Allergen> allergens;

    public Ingredient() {
    }
  
    public Ingredient(String name) {
        this.name = name;
        this.allergens = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Allergen> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<Allergen> allergens) {
        this.allergens = allergens;
    }

    public long getId() {
        return id;
    }
}
