package recipeApp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class editRecipeInstructionsController {

    private Dialog<ButtonType> dialog;

    private Recipe recipe;

    @FXML
    private TextField replaceNumber;

    @FXML
    private RadioButton deleteButton;

    @FXML
    private ListView<String> instructionListView;

    @FXML
    private TextField instructionField;

    @FXML
    private ToggleGroup instructionToggleGroup;

    @FXML
    private Button addButton;

    @FXML
    private RadioButton endButton;

    @FXML
    private RadioButton middleButton;

    @FXML
    private RadioButton replaceButton;

    private boolean checkInstructionField = false;

    public void initialize() {
        replaceNumber.setDisable(true);
        addButton.setDisable(true);

        replaceNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            checkValidation();
        });

        instructionToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            checkValidation();
        });

        deleteButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
               instructionField.setDisable(true);
            } else {
                instructionField.setDisable(false);
            }
            checkValidation();
        });

        instructionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0) {
                checkInstructionField = true;
            } else {
                checkInstructionField = false;
            }
            checkValidation();
        });
    }

    public void checkValidation() {
        boolean validIndex = false;
        if (Numeric.checkNumericInt(replaceNumber.getText(),1,recipe.getRecipeInstructions().size())) {
            validIndex = true;
        }

        if (endButton.isSelected()) {
            if (checkInstructionField) {
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
            if (checkInstructionField && validIndex) {
                addButton.setDisable(false);
            } else {
                addButton.setDisable(true);
            }
        }
    }

    public void instructionUpdate() {
        String instruction = instructionField.getText();

        if (recipe.getRecipeInstructions().contains(instruction)) {
            int counter = 1;

            for (String s: recipe.getRecipeInstructions()) {
                if (s.equals(instruction)) {
                    counter++;
                }
            }
             instruction += "   (#" + counter + ")";

        }

        RadioButton button = (RadioButton) instructionToggleGroup.getSelectedToggle();
        if (button.getText().equals("Add to End")) {
            recipe.getRecipeInstructions().add(instruction);
            instructionField.clear();
        } else if (button.getText().equals("Replace Step")) {
            int number2 = Integer.parseInt(replaceNumber.getText());
            recipe.getRecipeInstructions().set(number2-1, instruction);
            instructionField.clear();
            replaceNumber.clear();
        } else if (button.getText().equals("Add to Middle")) {
            int number2 = Integer.parseInt(replaceNumber.getText());
            recipe.getRecipeInstructions().add(number2-1, instruction);
            instructionField.clear();
            replaceNumber.clear();
        } else if (button.getText().equals("Delete")) {
            int number2 = Integer.parseInt(replaceNumber.getText());
            recipe.getRecipeInstructions().remove(number2-1);
            replaceNumber.clear();
        }
    }

    public void disableNumberField() {
        replaceNumber.setDisable(true);
    }

    public void enableNumberField() {
        replaceNumber.setDisable(false);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    public void loadEditContents() {
        instructionListView.setItems(recipe.getRecipeInstructions());

        instructionListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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
                            setText(String.valueOf(recipe.getRecipeInstructions().indexOf(item) + 1) + ". " + item);
                        }
                    }
                };
                return cell;
            }
        });


    }
}
