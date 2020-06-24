package recipeApp;

import javafx.collections.*;
import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Root
public class Recipe {

    private ObservableMap<Ingredient,Value> ingredientValueMap;

    @ElementMap
    private Map<Ingredient, Value> XMLingredientValueMap = new HashMap<>();

    @Element
    private String recipeName;

    @Attribute
    private int counter = 1;

//    double from 0.0-1.0, where 0.0 is never made and 1.0 is very commonly made
    @Attribute
    private double makingFrequency;

//    integer value of minutes required to make meal
    @Attribute
    private int prepTimeMinutes;

    @Attribute
    private double servingSize;

    @Attribute
    private mealTypes mealType;

    @Element
    private boolean isVegRecipe;

//      double from 0.0-1.0, where 0.0 is disgusting and 1.0 is very yum

    @Element
    private double tasteRating;

    @Element
    private double costPerServing;

    private ObservableList<String> recipeInstructions;

    @ElementList
    private List<String> XMLrecipeInstructions = new ArrayList<>();

//    preptime per serving in minutes
    @Element
    private double preptimePerServing;

    @Element
    private double recipeCost;

    @Element
    private int timesMade;

    private ObservableList<String> ingredientStringList;

    @ElementList
    private List<String> XMLingredientStringList = new ArrayList<>();

    public enum mealTypes {
        BREAKFAST,
        LUNCH,
        DINNER,
        SNACK
    }

    public Recipe() {

    }

    public void incrementTimesMade() {
        timesMade++;

    }

    public Recipe(ObservableMap<Ingredient, Value> ingredientValueMap, String recipeName, double makingFrequency, int prepTimeMinutes, double servingSize, mealTypes mealType, boolean isVegRecipe, double tasteRating, ObservableList<String> recipeInstructions) {
        this.ingredientValueMap = ingredientValueMap;
        this.recipeName = recipeName;
        this.makingFrequency = makingFrequency;
        this.prepTimeMinutes = prepTimeMinutes;
        this.servingSize = servingSize;
        this.mealType = mealType;
        this.isVegRecipe = isVegRecipe;
        this.tasteRating = tasteRating;
        this.recipeInstructions = recipeInstructions;
        this.preptimePerServing = (double) prepTimeMinutes/servingSize;
        this.timesMade = 0;

        this.costPerServing = (double) recipeCost/servingSize;
        ingredientStringList = FXCollections.observableArrayList();
        addListener();
        addInstructionListener();
    }

    public Recipe(String recipeName, double makingFrequency, int prepTimeMinutes, double servingSize, mealTypes mealType, double tasteRating) {
        this.ingredientValueMap = FXCollections.observableHashMap();
        this.recipeName = recipeName;
        this.makingFrequency = makingFrequency;
        this.prepTimeMinutes = prepTimeMinutes;
        this.servingSize = servingSize;
        this.mealType = mealType;
        this.isVegRecipe = isVegRecipe;
        this.tasteRating = tasteRating;
        this.recipeInstructions = recipeInstructions;
        this.preptimePerServing = (double) prepTimeMinutes/servingSize;
        this.timesMade = 0;

        this.costPerServing = (double) recipeCost/servingSize;
        this.recipeInstructions = FXCollections.observableArrayList();
        ingredientStringList = FXCollections.observableArrayList();
        addListener();
        addInstructionListener();
    }


    public ObservableMap<Ingredient, Value> getIngredientValueMap() {
        return ingredientValueMap;
    }

    public void setVegRecipe(boolean vegRecipe) {
        isVegRecipe = vegRecipe;
    }

    public String getRecipeName() {
        return recipeName;
    }

    @Override
    public String toString() {
        return this.getRecipeName();
    }

    public ObservableList<String> getRecipeInstructions() {
        return recipeInstructions;
    }

    public void addListener() {
        ingredientValueMap.addListener(new MapChangeListener<Ingredient, Value>() {
            @Override
            public void onChanged(Change<? extends Ingredient, ? extends Value> change) {
                loadIngredientStringList();
                XMLingredientValueMap.clear();
                for (Map.Entry<Ingredient, Value> e: ingredientValueMap.entrySet()) {
                    XMLingredientValueMap.put(e.getKey(), e.getValue());
                }
                Controller.updateFilteredList();
            }
        });
    }

    public void loadIngredientStringList() {
        if(ingredientValueMap.size() != 0) {
            ingredientStringList.clear();
            for (Ingredient i: ingredientValueMap.keySet()) {
                StringBuilder sb = new StringBuilder("");
                Value value = ingredientValueMap.get(i);
                sb.append(value.getNumberValue() + " " + value.getUnit() + ": " + i.getName());
                System.out.println("name: " + i.getName());
                System.out.println(i.hashCode());
                ingredientStringList.add(sb.toString());
                counter++;
            }
        }
    }

    public ObservableList<String> getIngredientStringList() {
        return ingredientStringList;
    }

    public void addInstructionListener() {

        recipeInstructions.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                XMLrecipeInstructions.clear();
                XMLrecipeInstructions.addAll(recipeInstructions);
                Controller.updateFilteredList();
            }
        });

        ingredientStringList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                XMLingredientStringList.clear();
                XMLingredientStringList.addAll(ingredientStringList);
                Controller.updateFilteredList();
                reCalculateVeg();
            }
        });


    }

    public void loadObservables() {
        recipeInstructions = FXCollections.observableArrayList();
        ingredientStringList = FXCollections.observableArrayList();
        ingredientValueMap = FXCollections.observableHashMap();
        recipeInstructions.addAll(XMLrecipeInstructions);
        ingredientStringList.addAll(XMLingredientStringList);
        for (Map.Entry<Ingredient, Value> e: XMLingredientValueMap.entrySet()) {
            ingredientValueMap.put(e.getKey(), e.getValue());
        }
    }

    public Map<Ingredient, Value> getXMLingredientValueMap() {
        return XMLingredientValueMap;
    }

    public List<String> getXMLrecipeInstructions() {
        return XMLrecipeInstructions;
    }

    public List<String> getXMLingredientStringList() {
        return XMLingredientStringList;
    }

    public int getCounter() {
        return counter;
    }

    public double getMakingFrequency() {
        return makingFrequency;
    }

    public int getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public double getServingSize() {
        return servingSize;
    }

    public mealTypes getMealType() {
        return mealType;
    }

    public boolean isVegRecipe() {
        return isVegRecipe;
    }

    public void reCalculateVeg() {
        boolean veg = true;

        for (Ingredient i: ingredientValueMap.keySet()) {
            if (!i.isVegIngredient()) {
                veg = false;
            }
        }
        this.isVegRecipe = veg;
    }

    public double getTasteRating() {
        return tasteRating;
    }


    public double getCostPerServing() {
        return costPerServing;
    }

    public double getPreptimePerServing() {
        return preptimePerServing;
    }

    public double getRecipeCost() {
        return recipeCost;
    }

    public int getTimesMade() {
        return timesMade;
    }

    public void setIngredientValueMap(ObservableMap<Ingredient, Value> ingredientValueMap) {
        this.ingredientValueMap = ingredientValueMap;
    }

    public void setXMLingredientValueMap(Map<Ingredient, Value> XMLingredientValueMap) {
        this.XMLingredientValueMap = XMLingredientValueMap;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setMakingFrequency(double makingFrequency) {
        this.makingFrequency = makingFrequency;
    }

    public void setPrepTimeMinutes(int prepTimeMinutes) {
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public void setMealType(mealTypes mealType) {
        this.mealType = mealType;
    }

    public void setTasteRating(double tasteRating) {
        this.tasteRating = tasteRating;
    }

    public void setCostPerServing(double costPerServing) {
        this.costPerServing = costPerServing;
    }

    public void setRecipeInstructions(ObservableList<String> recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public void setXMLrecipeInstructions(List<String> XMLrecipeInstructions) {
        this.XMLrecipeInstructions = XMLrecipeInstructions;
    }

    public void setPreptimePerServing(double preptimePerServing) {
        this.preptimePerServing = preptimePerServing;
    }

    public void setRecipeCost(double recipeCost) {
        this.recipeCost = recipeCost;
    }

    public void setTimesMade(int timesMade) {
        this.timesMade = timesMade;
    }

    public void setIngredientStringList(ObservableList<String> ingredientStringList) {
        this.ingredientStringList = ingredientStringList;
    }

    public void setXMLingredientStringList(List<String> XMLingredientStringList) {
        this.XMLingredientStringList = XMLingredientStringList;
    }
}
