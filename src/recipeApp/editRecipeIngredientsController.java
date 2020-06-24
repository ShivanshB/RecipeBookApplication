package recipeApp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;

public class editRecipeIngredientsController {

    @FXML
    private ToggleGroup instructionToggleGroup;

    @FXML
    private RadioButton deleteButton;

    private Recipe recipe;

    @FXML
    private Button addButton;

    @FXML
    private RadioButton endButton;

    @FXML
    private RadioButton middleButton;

    @FXML
    private RadioButton replaceButton;

    @FXML
    private TextField number;

    @FXML
    private ChoiceBox<String> unit;

    @FXML
    private TextField replaceNumber;

    @FXML
    private ChoiceBox<Ingredient> ingredientChoiceBox;

    private Dialog<ButtonType> dialog;

    @FXML
    private ListView<String> ingredientListView;

    public boolean numberCheck = false;
    public boolean checkIngredientChoiceBox = false;
    public boolean checkUnitChoiceBox = false;

    public void initialize() {
        addButton.setDisable(true);

        replaceNumber.setDisable(true);
        number.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Numeric.checkNumericLower(newValue,0.01)) {
                numberCheck = true;
            } else {
                numberCheck = false;
            }
            checkValidation();
        });

        ingredientChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            checkIngredientChoiceBox = newValue != null;
            checkValidation();
        });

        unit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            checkUnitChoiceBox = newValue != null;
            checkValidation();
        });

        replaceNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            checkValidation();
        });

        instructionToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            checkValidation();
        });

        deleteButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                number.setDisable(true);
                unit.setDisable(true);
                ingredientChoiceBox.setDisable(true);
            } else {
                number.setDisable(false);
                unit.setDisable(false);
                ingredientChoiceBox.setDisable(false);
            }
        });
    }

    public void checkValidation() {
        boolean validIndex = false;
        if (Numeric.checkNumericInt(replaceNumber.getText(),1,recipe.getIngredientStringList().size())) {
            validIndex = true;
        }

        if (endButton.isSelected()) {
            if (numberCheck && checkUnitChoiceBox && checkIngredientChoiceBox) {
                addButton.setDisable(false);
            } else {
                addButton.setDisable(true);
            }
        } else if (deleteButton.isSelected()) {
            if (validIndex) {
                addButton.setDisable(false);
            } else {
                addButton.setDisable(true);
            }
        } else {
            if (numberCheck && checkUnitChoiceBox && checkIngredientChoiceBox && validIndex) {
                addButton.setDisable(false);
            } else {
                addButton.setDisable(true);
            }
        }

    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setRecipeDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    public void loadEditContents() {
        ingredientListView.setItems(recipe.getIngredientStringList());
        ingredientChoiceBox.setItems(IngredientData.getInstance().getIngredientList());

        ingredientListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        }
                        else {
                            setText(String.valueOf(recipe.getIngredientStringList().indexOf(item) + 1) + ". " + item);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void disableNumberField() {
        replaceNumber.setDisable(true);
    }

    public void enableNumberField() {
        replaceNumber.setDisable(false);
    }

    public void ingredientUpdate() {
        String ingredient = number.getText() + " " + unit.getValue() + " of " + ingredientChoiceBox.getValue();

        if (recipe.getIngredientStringList().contains(ingredient)) {
            int counter = 1;

            for (String s: recipe.getIngredientStringList()) {
                if (s.equals(ingredient)) {
                    counter++;
                }
            }
            ingredient += "   (#" + counter + ")";

        }

        RadioButton button = (RadioButton) instructionToggleGroup.getSelectedToggle();
        if (button.getText().equals("Add to End")) {
            recipe.getIngredientStringList().add(ingredient);
            ingredientChoiceBox.getSelectionModel().clearSelection();
            number.clear();
            unit.getSelectionModel().clearSelection();
        } else if (button.getText().equals("Replace Step")) {
            int number2 = Integer.parseInt(replaceNumber.getText());
            recipe.getIngredientStringList().set(number2-1, ingredient);
            ingredientChoiceBox.getSelectionModel().clearSelection();
            replaceNumber.clear();
            number.clear();
            unit.getSelectionModel().clearSelection();
        } else if (button.getText().equals("Add to Middle")) {
            int number2 = Integer.parseInt(replaceNumber.getText());
            recipe.getIngredientStringList().add(number2-1, ingredient);
            ingredientChoiceBox.getSelectionModel().clearSelection();
            replaceNumber.clear();
            number.clear();
            unit.getSelectionModel().clearSelection();
        } else if (button.getText().equals("Delete")) {
            int number2 = Integer.parseInt(replaceNumber.getText());
            recipe.getIngredientStringList().remove(number2-1);
            replaceNumber.clear();
        }
    }
}
