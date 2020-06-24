package recipeApp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ingredientSelectorController {

    @FXML
    private ListView<Ingredient> ingredientListView;

    @FXML
    private TextField ingredientName;

    @FXML
    private RadioButton vegButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button editButton;

    @FXML
    private RadioButton nonVegButton;

    public void initialize() {
        ingredientListView.setItems(IngredientData.getInstance().getIngredientList());

        ingredientListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
                ingredientName.setText(ingredient.getName());
                if (ingredient.isVegIngredient()) {
                    vegButton.selectedProperty().setValue(true);
                } else {
                    nonVegButton.selectedProperty().setValue(true);
                }
            } else {
                ingredientName.clear();
            }
        });

        if (ingredientListView.getItems().size() != 0) {
            ingredientListView.getSelectionModel().selectFirst();
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }

        ingredientName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0) {
                saveButton.setDisable(false);
            } else {
                saveButton.setDisable(true);
            }
        });
    }

    @FXML
    public void loadEditContents() {
        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        ingredientName.setText(ingredient.getName());
        if (ingredient.isVegIngredient()) {
            vegButton.selectedProperty().setValue(true);
        } else {
            nonVegButton.selectedProperty().setValue(true);
        }
    }

    @FXML
    public void saveEditContents() {
        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        System.out.println(ingredient.hashCode());
        ingredient.setName(ingredientName.getText());
        ingredient.setVegIngredient(vegButton.isSelected());
        System.out.println("Called");
        for (Recipe r: Controller.getRecipeData().getRecipeList()) {
            r.loadIngredientStringList();
        }
        Controller.updateFilteredList();
    }
}
