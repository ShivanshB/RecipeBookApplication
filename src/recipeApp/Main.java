package recipeApp;

import javafx.application.Application;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

public class Main extends Application {

    public RecipeData recipeData;
    private static Main instance;
    private FilteredList<Recipe> filteredList;
    private Predicate<Recipe> none;

    public RecipeData getRecipeData() {
        return recipeData;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Recipe Manager");
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 300, 275));
        instance = this;
        primaryStage.show();
    }

    public static Main getInstance() {
        return instance;
    }

    public void setFilteredList(FilteredList<Recipe> filteredList) {
        this.filteredList = filteredList;
    }

    public void setRecipeData(RecipeData recipeData) {
        this.recipeData = recipeData;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        try {
            IngredientData.getInstance().loadIngredients();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        instance = this;
    }

    @Override
    public void stop() throws Exception {

        none = new Predicate<Recipe>() {
            @Override
            public boolean test(Recipe recipe) {
                return true;
            }
        };

        filteredList.setPredicate(none);

        try {
            Serializer serializer = new Persister();

            File result = new File("recipeData.xml");

            serializer.write(recipeData, result);
            IngredientData.getInstance().storeIngredients();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
