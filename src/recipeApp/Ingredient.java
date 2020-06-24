package recipeApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Ingredient {



    @Element
    private boolean isVegIngredient;

    @Attribute
    private String name;

    public Ingredient() {

    }


    public Ingredient(boolean isVegIngredient, String name) {
        this.isVegIngredient = isVegIngredient;
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }



    public boolean isVegIngredient() {
        return isVegIngredient;
    }


    public void setVegIngredient(boolean vegIngredient) {
        isVegIngredient = vegIngredient;
    }

    public void setName(String name) {
        this.name = name;
    }
}
