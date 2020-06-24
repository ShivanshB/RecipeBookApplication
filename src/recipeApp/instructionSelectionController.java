package recipeApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.simpleframework.xml.Attribute;


public class instructionSelectionController {

    @Attribute
    private int counter = 1;

    private Recipe recipe;

    @FXML
    private TextArea chosenInstructions;

    @FXML
    private Button addButton;

    @FXML
    private TextField instruction;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setupDataValidation() {
        addButton.setDisable(true);
        instruction.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.strip().length() != 0) {
                addButton.setDisable(false);
            } else {
                addButton.setDisable(true);
            }
        });
    }

    @FXML
    public void handleAddInstruction() {
        chosenInstructions.appendText(String.valueOf(counter) + ". " + String.valueOf(instruction.getText().toString() + "\n"));
        counter++;
        recipe.getRecipeInstructions().add(String.valueOf(instruction.getText().toString()));

        instruction.clear();
        addButton.setDisable(true);

    }
}
