package recipeApp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.BreakIterator;

public class editRecipeController {

    @FXML
    private TextField recipeName;

    @FXML
    private TextField makingFrequency;

    @FXML
    private TextField prepTime;

    @FXML
    private TextField servingSize;

    @FXML
    private TextField rating;

    @FXML
    private ToggleGroup mealToggleGroup;

    @FXML
    private RadioButton breakfastButton;

    @FXML
    private RadioButton lunchButton;

    @FXML
    private RadioButton dinnerButton;

    @FXML
    private RadioButton snackButton;

    @FXML
    private Recipe recipe;

    private Dialog<ButtonType> recipeDialog;

    private boolean nameFieldCheck;
    private boolean makingFrequencyCheck;
    private boolean prepTimeCheck;
    private boolean servingSizeCheck;
    private boolean ratingCheck;


    public void initialize() {

    }

    public void loadEditContents() {
        recipeName.setText(recipe.getRecipeName());
        makingFrequency.setText(String.valueOf(recipe.getMakingFrequency()));
        prepTime.setText(String.valueOf(recipe.getPrepTimeMinutes()));
        servingSize.setText(String.valueOf(recipe.getServingSize()));
        rating.setText(String.valueOf(recipe.getTasteRating()));

        switch (recipe.getMealType().toString()) {
            case ("BREAKFAST"):
                breakfastButton.selectedProperty().set(true);

            case ("LUNCH"):
                lunchButton.selectedProperty().set(true);

            case ("DINNER"):
                dinnerButton.selectedProperty().set(true);

            default:
                snackButton.selectedProperty().set(true);
        }
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setRecipeDialog(Dialog<ButtonType> recipeDialog) {
        this.recipeDialog = recipeDialog;
    }

    public void setupDataValidation() {
        recipeDialog.getDialogPane().lookupButton(ButtonType.NEXT).setDisable(true);


        recipeName.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0) {
                nameFieldCheck = true;
            } else {
                nameFieldCheck= false;
            }
            checkAllData();
        }));

        makingFrequency.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumeric(newValue, 0, 10)) {
                makingFrequencyCheck = true;
            } else {
                makingFrequencyCheck = false;
            }
            checkAllData();
        }));

        prepTime.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumericLower(newValue, 0)) {
                prepTimeCheck = true;
            } else {
                prepTimeCheck = false;
            }
            checkAllData();
        }));

        servingSize.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumericLower(newValue, 0)) {
                servingSizeCheck = true;
            } else {
                servingSizeCheck = false;
            }
            checkAllData();
        }));

        rating.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumeric(newValue,0 ,100)) {
                ratingCheck = true;
            } else {
                ratingCheck= false;
            }
            checkAllData();
        }));
    }

    public void checkAllData() {
        if (nameFieldCheck && makingFrequencyCheck && prepTimeCheck && prepTimeCheck && servingSizeCheck && ratingCheck) {
            recipeDialog.getDialogPane().lookupButton(ButtonType.NEXT).setDisable(false);
        } else {
            recipeDialog.getDialogPane().lookupButton(ButtonType.NEXT).setDisable(true);
        }
    }

    public void setNewValues() {
        recipe.setRecipeName(recipeName.getText());
        recipe.setMakingFrequency(Double.parseDouble(makingFrequency.getText()));
        recipe.setPrepTimeMinutes(Integer.parseInt(prepTime.getText()));
        recipe.setServingSize(Double.parseDouble(servingSize.getText()));
        recipe.setTasteRating(Double.parseDouble(rating.getText()));
    }
}
