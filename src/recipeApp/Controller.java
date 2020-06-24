package recipeApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    private static Predicate<Recipe> currentPredicate;

    private static FilteredList<Recipe> filteredList;

    @FXML
    private RadioMenuItem vegFilter;

    @FXML
    private RadioMenuItem noFilter;

    @FXML
    private RadioMenuItem nonVegFilter;

    @FXML
    private ToggleGroup filterToggleGroup;

    @FXML
    private Button buttonTest;

    @FXML
    private MenuItem incrementMenuItem;

    @FXML
    private MenuItem editMenuItem;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private Label bottomLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> ingredientsListView;

    @FXML
    private ListView<String> instructionsListView;

    private static RecipeData recipeData;

    private Predicate<Recipe> vegPredicate;
    private Predicate<Recipe> nonVegPredicate;
    private Predicate<Recipe> wantAllItems;


    public Controller() {
    }

    public void initialize() throws Exception {
        addBottomLabel();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");
        contextMenu.getItems().addAll(delete, edit);
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteRecipe();
            }
        });
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editRecipe();
            }
        });


        Serializer serializer = new Persister();
        File result = new File("recipeData.xml");
        if (result.length() == 0) {
            recipeData = new RecipeData();

        } else {
            recipeData = serializer.read(RecipeData.class, result);
            recipeData.loadObservable();
            for (Recipe r: recipeData.getRecipeList()) {
                r.loadObservables();
            }
        }

        for (Recipe r: recipeData.getRecipeList()) {
            r.addInstructionListener();
            r.addListener();
        }

        recipeData.setListener();

        Main.getInstance().setRecipeData(recipeData);

        wantAllItems = new Predicate<Recipe>() {
            @Override
            public boolean test(Recipe recipe) {
                return true;
            }
        };

        filteredList = new FilteredList<Recipe>(recipeData.getRecipeList(), wantAllItems);

        recipeListView.setItems(recipeData.getRecipeList());
        IngredientData.getInstance().addListener();
        IngredientData.getInstance().setIngredientNameList();
        if (recipeData.getRecipeList().size() != 0) {
            recipeListView.getSelectionModel().selectFirst();
            instructionsListView.setItems(recipeListView.getSelectionModel().getSelectedItem().getRecipeInstructions());
            ingredientsListView.setItems(recipeListView.getSelectionModel().getSelectedItem().getIngredientStringList());
        }
        recipeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Recipe>() {
            @Override
            public void changed(ObservableValue<? extends Recipe> observable, Recipe oldValue, Recipe newValue) {
                if (newValue != null) {
                    System.out.println("views refreshed");
                    instructionsListView.refresh();
                    ingredientsListView.refresh();
                    instructionsListView.setItems(recipeListView.getSelectionModel().getSelectedItem().getRecipeInstructions());
                    ingredientsListView.setItems(recipeListView.getSelectionModel().getSelectedItem().getIngredientStringList());
                } else {
                    instructionsListView.getItems().clear();
                    ingredientsListView.getItems().clear();
                }

                if (recipeListView.getSelectionModel().selectedItemProperty().getValue() == null) {
                    delete.setDisable(true);
                    deleteMenuItem.setDisable(true);
                    incrementMenuItem.setDisable(true);
                    editMenuItem.setDisable(true);
                } else {
                    delete.setDisable(false);
                    deleteMenuItem.setDisable(false);
                    incrementMenuItem.setDisable(false);
                    editMenuItem.setDisable(false);

                }

            }
        });



        recipeListView.setCellFactory(new Callback<ListView<Recipe>, ListCell<Recipe>>() {
            @Override
            public ListCell<Recipe> call(ListView<Recipe> param) {
                ListCell<Recipe> cell = new ListCell<Recipe>() {
                    @Override
                    protected void updateItem(Recipe item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getRecipeName());
                        }
                        if (empty) {
                            setText(null);
                        }
                        else {
                            setText(String.valueOf(recipeData.getRecipeList().indexOf(item) + 1) + ". " + item);
                        }

                        setMinWidth(param.getWidth());
                        setMaxWidth(param.getWidth());
                        setPrefWidth(param.getWidth());

                        // allow wrapping
                        setWrapText(true);
                    }
                };
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(contextMenu);
                    }
                }) ;
                return cell;
            }
        });

        if (recipeListView.getSelectionModel().selectedItemProperty().getValue() == null) {
            delete.setDisable(true);
            deleteMenuItem.setDisable(true);
            incrementMenuItem.setDisable(true);
            editMenuItem.setDisable(true);
        }

        ingredientsListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            if (recipe == null) {
                                recipe = recipeData.getRecipeList().get(0);
                            }
                            setText(String.valueOf(recipe.getIngredientStringList().indexOf(item) + 1) + ". " + item);
                        }

                        setMinWidth(param.getWidth());
                        setMaxWidth(param.getWidth());
                        setPrefWidth(param.getWidth());

                        // allow wrapping
                        setWrapText(true);

                    }
                };
                return cell;
            }
        });

        instructionsListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            if (recipe == null) {
                                recipe = recipeData.getRecipeList().get(0);
                            }
                            setText(String.valueOf(recipe.getRecipeInstructions().indexOf(item)+ 1)+ ". " + item);
                        }

                        setMinWidth(param.getWidth());
                        setMaxWidth(param.getWidth());
                        setPrefWidth(param.getWidth());

                        // allow wrapping
                        setWrapText(true);
                    }
                }; return cell;
            }
        });

        nonVegFilter.selectedProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue) {
               filteredList.setPredicate(nonVegPredicate);
               currentPredicate = nonVegPredicate;
           } else {
               filteredList.setPredicate(wantAllItems);
               currentPredicate = wantAllItems;
           }
        });


        vegFilter.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                filteredList.setPredicate(vegPredicate);
                currentPredicate = vegPredicate;
            } else {
                filteredList.setPredicate(wantAllItems);
                currentPredicate = wantAllItems;
            }
        });

        noFilter.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                filteredList.setPredicate(wantAllItems);
                currentPredicate = wantAllItems;
            }
        });

        vegPredicate = new Predicate<Recipe>() {
            @Override
            public boolean test(Recipe recipe) {
                return recipe.isVegRecipe();
            }
        };

        nonVegPredicate = new Predicate<Recipe>() {
            @Override
            public boolean test(Recipe recipe) {
                return !recipe.isVegRecipe();
            }
        };

        Main.getInstance().setFilteredList(filteredList);

        currentPredicate = wantAllItems;

        recipeData.getRecipeList().addListener(new ListChangeListener<Recipe>() {
            @Override
            public void onChanged(Change<? extends Recipe> c) {
                filteredList = new FilteredList<Recipe>(recipeData.getRecipeList(), currentPredicate);
            }
        });

        bottomLabel.textProperty().addListener(observable -> {updateFilteredList();});
    }

    public static void updateFilteredList() {
        filteredList = new FilteredList<Recipe>(recipeData.getRecipeList(), currentPredicate);
    }

    public static FilteredList<Recipe> getFilteredList() {
        return filteredList;
    }

    @FXML
    public void viewAllIngredients() {
        Dialog<ButtonType> ingredientsDialog = new Dialog<>();
        ingredientsDialog.initOwner(mainBorderPane.getScene().getWindow());
        ingredientsDialog.setTitle("All Ingredients");

        FXMLLoader newIngredientDialogLoader = new FXMLLoader();
        newIngredientDialogLoader.setLocation(getClass().getResource("ingredientsDialog.fxml"));

        try {
            ingredientsDialog.getDialogPane().setContent(newIngredientDialogLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        ingredientsDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        ingredientsDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        ingredientsDialog.showAndWait();
    }

    @FXML
    public void deleteRecipe() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Recipe");
        alert.setHeaderText("Delete Recipe: " + recipe.getRecipeName());
        alert.setContentText("Are you sure? Press OK to confirm and CANCEL to back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            recipeData.getRecipeList().remove(recipe);
            if (recipeListView.getItems().size() != 0) {
                recipeListView.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    public void handleNewIngredient() {
        Dialog<ButtonType> newIngredientDialog = new Dialog<>();
        newIngredientDialog.initOwner(mainBorderPane.getScene().getWindow());
        newIngredientDialog.setTitle("Create A New Ingredient");
        newIngredientDialog.setHeaderText("Enter the attributes of your desired ingredient.");

        FXMLLoader newIngredientDialogLoader = new FXMLLoader();
        newIngredientDialogLoader.setLocation(getClass().getResource("newIngredientDialog.fxml"));

        try {
            newIngredientDialog.getDialogPane().setContent(newIngredientDialogLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        newIngredientDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        newIngredientDialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        newIngredientDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        newIngredientDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        newIngredientDialogController controller = newIngredientDialogLoader.getController();
        controller.setDialog(newIngredientDialog);
        controller.setupDataValidation();
        Optional<ButtonType> result = newIngredientDialog.showAndWait();


        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.addIngredient();
        } else if (result.isPresent() && result.get() == ButtonType.APPLY) {
            controller.addIngredient();
            handleNewIngredient();
        }
    }

    public static RecipeData getRecipeData() {
        return recipeData;

    }

    @FXML
    public void handleNewRecipe() {
        Dialog<ButtonType> newRecipeDialog = new Dialog<ButtonType>();
        newRecipeDialog.initOwner(mainBorderPane.getScene().getWindow());
        newRecipeDialog.setTitle("Create A New Recipe");
        newRecipeDialog.setHeaderText("Enter the attributes of your desired recipe.");

        FXMLLoader newRecipeDialogLoader = new FXMLLoader();
        newRecipeDialogLoader.setLocation(getClass().getResource("newRecipeDialog.fxml"));


        try {
            newRecipeDialog.getDialogPane().setContent(newRecipeDialogLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        newRecipeDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        newRecipeDialog.getDialogPane().getButtonTypes().add(ButtonType.NEXT);

        newRecipeDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        NewRecipeDialogController controller = newRecipeDialogLoader.getController();
        controller.setRecipeDialog(newRecipeDialog);
        controller.setupDataValidation();
        Optional<ButtonType> result = newRecipeDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.NEXT) {
            Recipe recipe = controller.createRecipe();
            openIngredientsDialog(recipe);
//            load all data for new recipe
//            open new stage with ingredients listed
//            controller.addRecipe();
        }

    }

    public void openIngredientsDialog(Recipe recipe) {
        Dialog<ButtonType> ingredientDialog = new Dialog<ButtonType>();
        ingredientDialog.initOwner(mainBorderPane.getScene().getWindow());
        ingredientDialog.setTitle("Choose your ingredients.");
        ingredientDialog.setHeaderText("Enter the attributes of your desired recipe.");

        FXMLLoader ingredientDialogLoader = new FXMLLoader();
        ingredientDialogLoader.setLocation(getClass().getResource("ingredientSelection.fxml"));


        try {
            ingredientDialog.getDialogPane().setContent(ingredientDialogLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        ingredientDialog.getDialogPane().getButtonTypes().add(ButtonType.NEXT);
        ingredientDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ingredientDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        ingredientSelectionController controller = ingredientDialogLoader.getController();
        controller.setRecipe(recipe);
        controller.loadIngredients();
        controller.setupDataValidation();

        Optional<ButtonType> result = ingredientDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.NEXT) {
            controller.calculateIsVeg();
            addRecipeInstructions(recipe);
        }

    }
    
    public void addRecipeInstructions(Recipe recipe) {
        Dialog<ButtonType> instructionsDialog = new Dialog<ButtonType>();
        instructionsDialog.initOwner(mainBorderPane.getScene().getWindow());
        instructionsDialog.setTitle("Type your instructions.");
        instructionsDialog.setHeaderText("Enter the attributes of your desired recipe.");

        FXMLLoader instructionsDialogLoader = new FXMLLoader();
        instructionsDialogLoader.setLocation(getClass().getResource("instructionSelection.fxml"));


        try {
            instructionsDialog.getDialogPane().setContent(instructionsDialogLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        instructionsDialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        instructionsDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        instructionsDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        instructionSelectionController controller = instructionsDialogLoader.getController();
        controller.setRecipe(recipe);
        controller.setupDataValidation();

        Optional<ButtonType> result = instructionsDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.FINISH) {
            boolean checkSameName = true;
            for (Recipe r: recipeData.getRecipeList()) {
                if (r.getRecipeName().equals(recipe.getRecipeName())) {
                    checkSameName = false;
                }
            }
            if (checkSameName) {
                recipeData.getRecipeList().add(recipe);
                recipeListView.getSelectionModel().select(recipe);
            }
        }
    }

    @FXML
    public void editRecipe() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> recipeEditDialog = new Dialog<ButtonType>();
        recipeEditDialog.initOwner(mainBorderPane.getScene().getWindow());
        recipeEditDialog.setTitle("Edit Recipe: " + recipe.getRecipeName());
        recipeEditDialog.setHeaderText("Enter the attributes of your desired recipe.");

        FXMLLoader editRecipeLoader = new FXMLLoader();
        editRecipeLoader.setLocation(getClass().getResource("editRecipe.fxml"));


        try {
            recipeEditDialog.getDialogPane().setContent(editRecipeLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.NEXT);
        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        recipeEditDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        editRecipeController controller = editRecipeLoader.getController();
        controller.setRecipeDialog(recipeEditDialog);
        controller.setRecipe(recipe);
        controller.setupDataValidation();
        controller.loadEditContents();

        Optional<ButtonType> result = recipeEditDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.NEXT) {
            controller.setNewValues();
            recipeListView.refresh();
            recipeIngredientEditDialog();
//           go to next dialog
        }
    }

    public void recipeIngredientEditDialog() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> recipeEditDialog = new Dialog<ButtonType>();
        recipeEditDialog.initOwner(mainBorderPane.getScene().getWindow());
        recipeEditDialog.setTitle("Edit Recipe Ingredients: " + recipe.getRecipeName());
        recipeEditDialog.setHeaderText("Enter the attributes of your desired recipe.");

        FXMLLoader editRecipeLoader = new FXMLLoader();
        editRecipeLoader.setLocation(getClass().getResource("editRecipeIngredients.fxml"));


        try {
            recipeEditDialog.getDialogPane().setContent(editRecipeLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.NEXT);
        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.PREVIOUS);
        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        recipeEditDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        editRecipeIngredientsController controller = editRecipeLoader.getController();
        controller.setRecipeDialog(recipeEditDialog);
        controller.setRecipe(recipe);
//        controller.setupDataValidation();
        controller.loadEditContents();

        Optional<ButtonType> result = recipeEditDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.NEXT) {
            ingredientsListView.refresh();
            recipeInstructionsDialog();
        }
        if (result.isPresent() && result.get() == ButtonType.PREVIOUS) {
            editRecipe();
        }
    }

    public void recipeInstructionsDialog() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> recipeEditDialog = new Dialog<ButtonType>();
        recipeEditDialog.initOwner(mainBorderPane.getScene().getWindow());
        recipeEditDialog.setTitle("Edit Recipe Ingredients: " + recipe.getRecipeName());
        recipeEditDialog.setHeaderText("Enter the attributes of your desired recipe.");

        FXMLLoader editRecipeLoader = new FXMLLoader();
        editRecipeLoader.setLocation(getClass().getResource("editRecipeInstructions.fxml"));


        try {
            recipeEditDialog.getDialogPane().setContent(editRecipeLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.PREVIOUS);
        recipeEditDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        recipeEditDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        editRecipeInstructionsController controller = editRecipeLoader.getController();
        controller.setDialog(recipeEditDialog);
        controller.setRecipe(recipe);
//        controller.setupDataValidation();
        controller.loadEditContents();

        Optional<ButtonType> result = recipeEditDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.FINISH) {
            instructionsListView.refresh();
        }
        if (result.isPresent() && result.get() == ButtonType.PREVIOUS) {
            recipeIngredientEditDialog();
        }
        updateBottomLabel(recipeListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void editIngredient() {
        Dialog<ButtonType> ingredientDialog = new Dialog<ButtonType>();
        ingredientDialog.initOwner(mainBorderPane.getScene().getWindow());
        ingredientDialog.setTitle("Choose your ingredient to edit.");
        ingredientDialog.setHeaderText("Enter the attributes of your desired ingredient.");

        FXMLLoader ingredientDialogLoader = new FXMLLoader();
        ingredientDialogLoader.setLocation(getClass().getResource("ingredientSelector.fxml"));


        try {
            ingredientDialog.getDialogPane().setContent(ingredientDialogLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
        }

        ingredientDialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        ingredientDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);

        ingredientDialog.showAndWait();
        ingredientsListView.refresh();

        for (Recipe r: recipeData.getRecipeList()) {
            System.out.println(r.getIngredientStringList());
        }
    }

    public void addBottomLabel() {
        bottomLabel.setWrapText(true);
        recipeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                bottomLabel.setText("Making Frequency: " + newValue.getMakingFrequency() + "/10" + "\t" + "Prep Time: " + newValue.getPrepTimeMinutes() + " minutes \t" + "Serving Size: " + newValue.getServingSize() + " servings \t" + "Meal Type: " + newValue.getMealType().toString().toLowerCase().substring(0,1).toUpperCase() + newValue.getMealType().toString().toLowerCase().substring(1) + (newValue.isVegRecipe() ? "\t Vegetarian: Yes" : "\t Vegetarian: No") + "\t Taste Rating: " + newValue.getTasteRating() + "/100" +  "\t Prep Time per Serving: " + newValue.getPreptimePerServing() + "min/serving" + "\t Times Made: " + newValue.getTimesMade());
            } else {
                bottomLabel.setText("");
            }

        });
    }

    public void updateBottomLabel(Recipe newValue) {
        newValue.reCalculateVeg();
        bottomLabel.setText("Making Frequency: " + newValue.getMakingFrequency() + "/10" + "\t" + "Prep Time: " + newValue.getPrepTimeMinutes() + " minutes \t" + "Serving Size: " + newValue.getServingSize() + " servings \t" + "Meal Type: " + newValue.getMealType().toString().toLowerCase().substring(0,1).toUpperCase() + newValue.getMealType().toString().toLowerCase().substring(1) + (newValue.isVegRecipe() ? "\t Vegetarian: Yes" : "\t Vegetarian: No") + "\t Taste Rating: " + newValue.getTasteRating() + "/100" +  "\t Prep Time per Serving: " + newValue.getPreptimePerServing() + "min/serving" + "\t Times Made: " + newValue.getTimesMade());
    }

    @FXML
    public void incrementMade() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Increment Times Recipe Made");
        alert.setHeaderText("Increment Recipe: " + recipe.getRecipeName());
        alert.setContentText("Are you sure? Press OK to confirm and CANCEL to back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            recipe.incrementTimesMade();
            updateBottomLabel(recipe);
        }
    }

    public static Predicate<Recipe> getCurrentPredicate() {
        return currentPredicate;
    }
}

