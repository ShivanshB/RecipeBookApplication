package recipeApp;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.simpleframework.xml.Attribute;


public class ingredientSelectionController {

    @Attribute
    private int counter = 1;

    @FXML
    private ChoiceBox<String> ingredientChoiceBox;

    @FXML
    private TextArea chosenIngredients;

    @FXML
    private Button addButton;

    @FXML
    private TextField number;

    @FXML
    private ChoiceBox<String> unit;

    private Recipe recipe;

    private boolean numberFieldCheck;
    private boolean unitFieldCheck;
    private boolean ingredientFieldCheck;

    public void initialize() {

    }


    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void loadIngredients() {
//        IngredientData.getInstance().setIngredientNameList();
        ingredientChoiceBox.setItems(IngredientData.getInstance().getIngredientNameList());
    }


    @FXML
    public void handleAddIngredient() {
        StringBuilder sb = new StringBuilder();

        String unitInput = unit.getSelectionModel().getSelectedItem();
        String numberInput = number.getText();
        String ingredientInput = ingredientChoiceBox.getSelectionModel().getSelectedItem();

        Ingredient ingredient = null;

        for (Ingredient i: IngredientData.getInstance().getIngredientList()) {
            if (i.getName().equals(ingredientInput)) {
                ingredient = i;
            }
        }

        String unitValue = unit.getValue();
        Value.units unitVar;

        switch (unitValue) {
            case "TSP":
                unitVar = Value.units.TSP;
                break;
            case "TBSP":
                unitVar = Value.units.TBSP;
                break;
            case "FL OZ":
                unitVar = Value.units.FL_OZ;
                break;
            case "NUMBER":
                unitVar = Value.units.NUMBER;
                break;
            case "CUP":
                unitVar = Value.units.CUP;
                break;
            case "PINT":
                unitVar = Value.units.PINT;
                break;
            case "QUART":
                unitVar = Value.units.QUART;
                break;
            case "GALLON":
                unitVar = Value.units.GALLON;
                break;
            case "LB":
                unitVar = Value.units.LB;
                break;
            case "OZ":
                unitVar = Value.units.OZ;
                break;
            case "G":
                unitVar = Value.units.G;
                break;
            default:
                unitVar = Value.units.KG;
                break;
        }

        double numberValue = Double.parseDouble(number.getText());

        Value newValue = new Value(numberValue, unitVar);
        sb.append(numberInput).append(" ").append(unitInput).append(" of ").append(ingredientInput);
        chosenIngredients.appendText(counter + ". " + sb.toString() + "\n");
        counter++;
        recipe.getIngredientValueMap().put(ingredient, newValue);

//        ObservableMap<Ingredient, Value> newMap = FXCollections.observableHashMap();
//        newMap.put(ingredient, newValue);

        number.clear();
        unit.getSelectionModel().clearSelection();
        ingredientChoiceBox.getSelectionModel().clearSelection();
        addButton.setDisable(true);

    }

    public void calculateIsVeg() {
        boolean isVeg = true;
        for (Ingredient i: recipe.getIngredientValueMap().keySet()) {
            if (!i.isVegIngredient()) {
                isVeg = false;
                break;
            }
        }
        recipe.setVegRecipe(isVeg);
    }

    public void setupDataValidation() {
        addButton.setDisable(true);

        number.textProperty().addListener(((observable, oldValue, newValue) -> {
            numberFieldCheck = newValue.strip().length() != 0 && Numeric.checkNumericLower(newValue, 0.001);
            checkAllData();
        }));

        unit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            unitFieldCheck = newValue != null;
            checkAllData();
        });

        ingredientChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ingredientFieldCheck = newValue != null;
            checkAllData();
        });

    }

    public void checkAllData() {
        addButton.setDisable(!unitFieldCheck || !ingredientFieldCheck || !numberFieldCheck);
    }
}
