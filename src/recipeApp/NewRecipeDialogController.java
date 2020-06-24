package recipeApp;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;

public class NewRecipeDialogController {

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

    private boolean nameFieldCheck;
    private boolean makingFrequencyCheck;
    private boolean prepTimeCheck;
    private boolean servingSizeCheck;
    private boolean ratingCheck;

    private Dialog<ButtonType> recipeDialog;

    public void setRecipeDialog(Dialog<ButtonType> recipeDialog) {
        this.recipeDialog = recipeDialog;
    }

    public void initialize() {
    }

    public void setupDataValidation() {
        recipeDialog.getDialogPane().lookupButton(ButtonType.NEXT).setDisable(true);


        recipeName.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0) {
                nameFieldCheck = true;
                checkAllData();
            } else {
                nameFieldCheck= false;
                checkAllData();
            }
        }));

        makingFrequency.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumeric(newValue, 0, 10)) {
                makingFrequencyCheck = true;
                checkAllData();
            } else {
                makingFrequencyCheck = false;
                checkAllData();
            }
        }));

        prepTime.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumericLower(newValue, 0)) {
                prepTimeCheck = true;
                checkAllData();
            } else {
                prepTimeCheck = false;
                checkAllData();
            }
        }));

        servingSize.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumericLower(newValue, 0)) {
                servingSizeCheck = true;
                checkAllData();
            } else {
                servingSizeCheck = false;
                checkAllData();
            }
        }));

        rating.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0 && Numeric.checkNumeric(newValue,0 ,100)) {
                ratingCheck = true;
                checkAllData();
            } else {
                ratingCheck= false;
                checkAllData();
            }
        }));
    }

    public void checkAllData() {
        if (nameFieldCheck && makingFrequencyCheck && prepTimeCheck && prepTimeCheck && servingSizeCheck && ratingCheck) {
            recipeDialog.getDialogPane().lookupButton(ButtonType.NEXT).setDisable(false);
        } else {
            recipeDialog.getDialogPane().lookupButton(ButtonType.NEXT).setDisable(true);
        }
    }
    public Recipe createRecipe() {

        String name = recipeName.getText().toString();
        Double frequency = Double.valueOf(makingFrequency.getText());
        int prepTimeMinutes = Integer.valueOf(prepTime.getText());
        Double servingsize = Double.valueOf(servingSize.getText());

        RadioButton mealTypeRadioButton = (RadioButton) mealToggleGroup.getSelectedToggle();
        Recipe.mealTypes mealTypes = Recipe.mealTypes.valueOf(mealTypeRadioButton.getText());

        Double tasteRating = Double.valueOf(rating.getText());

        Recipe recipe = new Recipe(name, frequency, prepTimeMinutes, servingsize, mealTypes, tasteRating);
        return recipe;
    }

}
