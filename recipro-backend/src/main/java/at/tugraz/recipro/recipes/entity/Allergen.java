package at.tugraz.recipro.recipes.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Julian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Allergen.FIND_ALL, query = "SELECT a FROM Allergen a"),
    @NamedQuery(name = Allergen.FIND_BY_SHORT_NAME, query = "SELECT a FROM Allergen a WHERE lower(a.shortName) like :shortName")
})
public class Allergen {
    static final String PREFIX = "recipes.entity.Allergen.";
    public static final String FIND_ALL = PREFIX + "findAll";
    public static final String FIND_BY_SHORT_NAME = PREFIX + "findByShortName";
    
    @Id
    private String shortName;
    private String name;
    
    public Allergen() {
    }

    public Allergen(String shortName, String name) {
        this.shortName = shortName;
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
