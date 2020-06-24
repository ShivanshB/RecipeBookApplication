package recipeApp;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class IngredientData {

    private static IngredientData ingredientInstance = new IngredientData();

    private ObservableList<Ingredient> ingredientList;
    private ObservableList<String> ingredientNameList;
    private String filename = "ingredients.txt";

    private IngredientData() {
        ingredientNameList = FXCollections.observableArrayList();
    }

    public void addListener() {
        ingredientList.addListener(new ListChangeListener<Ingredient>() {
            @Override
            public void onChanged(Change<? extends Ingredient> c) {
                IngredientData.getInstance().setIngredientNameList();
            }
        });
    }

    public static IngredientData getInstance() {
        return ingredientInstance;
    }

    public ObservableList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public ObservableList<String> getIngredientNameList() {
        return ingredientNameList;
    }

    public void setIngredientNameList() {
        for (Ingredient i: ingredientList) {
            if (!ingredientNameList.contains(i.getName())) {
                ingredientNameList.add(i.getName());
            }

        }
    }

    public void loadIngredients() throws IOException {
        ingredientList = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");


                String name = itemPieces[0];
                Boolean isVeg = Boolean.valueOf(itemPieces[1]);

                Ingredient ingredient = new Ingredient(isVeg, name);
                ingredientList.add(ingredient);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

    }



    public void storeIngredients() throws IOException {

        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {

            Iterator<Ingredient> iter = ingredientList.iterator();
            while (iter.hasNext()) {
                Ingredient item = iter.next();
                bw.write(String.format("%s\t%s", item.getName(), item.isVegIngredient()));
                bw.newLine();
            }

        } finally {
            if (bw != null) {
                bw.close();
            }
        }

    }
}
