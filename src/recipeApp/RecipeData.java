package recipeApp;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Root
public class RecipeData {

    private String filename = "recipes.txt";

    private ObservableList<Recipe> recipeList;

    @ElementList (entry = "recipe")
    private List<Recipe> XMLList = new ArrayList<>();

    public RecipeData() {
        recipeList = FXCollections.observableArrayList();
    }

    public List<Recipe> getXMLList() {
        return XMLList;
    }

    public void setListener() {
        recipeList.addListener(new ListChangeListener<Recipe>() {
            @Override
            public void onChanged(Change<? extends Recipe> c) {
                updateXMLList();
            }
        });
    }

    public void loadObservable() {
        for (Recipe r: XMLList) {
            recipeList.add(r);
        }
    }

    public void updateXMLList() {
        XMLList.clear();
        for (Recipe r: recipeList) {
            XMLList.add(r);
        }
    }


    public ObservableList<Recipe> getRecipeList() {
        return recipeList;
    }

    public void loadRecipeData() {

    }

}
