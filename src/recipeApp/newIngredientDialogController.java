package recipeApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.text.DecimalFormat;

public class newIngredientDialogController {

    @FXML
    private TextField ingredientName;

    @FXML
    private RadioButton vegButton;

    private Dialog<ButtonType> dialog;

    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    public void initialize() {

    }

    public void setupDataValidation() {
//        if (dialog == null) {
//            System.out.println("dialog is null");
//        } else if (dialog.getDialogPane() == null) {
//            System.out.println("dialog pane is null");
//        } else if (dialog.getDialogPane().lookupButton(ButtonType.NEXT) == null) {
//            System.out.println("lookup is null");
//        }

        System.out.println("data validation set up");
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        dialog.getDialogPane().lookupButton(ButtonType.APPLY).setDisable(true);

        ingredientName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0) {
                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                dialog.getDialogPane().lookupButton(ButtonType.APPLY).setDisable(false);
            } else {
                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                dialog.getDialogPane().lookupButton(ButtonType.APPLY).setDisable(true);
            }
        });
    }

    public void addIngredient() {

        Ingredient newIngredient = new Ingredient(vegButton.isSelected(), ingredientName.getText());

//        System.out.println(newIngredient);
        if (!IngredientData.getInstance().getIngredientNameList().contains(ingredientName.getText())) {
            IngredientData.getInstance().getIngredientList().add(newIngredient);
        } else {
            System.out.println("Duplicate ingredient was not added");
        }

    }

}

