package recipeApp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.util.Optional;

public class ingredientsDialogController {

    @FXML
    private ListView<Ingredient> ingredientListView;

    public void initialize() {
        ingredientListView.setItems(IngredientData.getInstance().getIngredientList());
    }
}
