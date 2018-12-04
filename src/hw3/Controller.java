//Yihiu Chiu
//yihiuc
package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

    /**
     * This method handles click event of Recommend button. it creates a new person based on user's input
     * calculate this person's recommended nutrients, and display it to the table
     **/
    class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                dataValidator();
                personCreater();
                NutriProfiler.createNutriProfile(NutriByte.person);
            } catch (InvalidProfileException ie) {
                System.out.println("Warning:::Input validation failed");
            }
        }
    }

    /**
     * This method handles click event of open menu. It opens the file chooser and let user select target file
     * then system load the file, create person, and display all information on the screen.
     **/
    class OpenMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select File");
            fc.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Profile Files (only csv or xml)", "*.csv", "*.xml"), new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = null;
            if ((file = fc.showOpenDialog(NutriByte.view.root.getScene().getWindow())) != null) {
                clearPanel();
                try {
                    //The following parts reads the data to person object, set values to UI, and diet product list
                    NutriByte.model.readProfiles(file.getAbsolutePath());
                    NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
                    NutriByte.view.ageTextField.setText(String.valueOf(NutriByte.person.age));
                    NutriByte.view.weightTextField.setText(String.valueOf(NutriByte.person.weight));
                    NutriByte.view.heightTextField.setText(String.valueOf(NutriByte.person.height));
                    NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
                    NutriByte.view.ingredientsToWatchTextArea.appendText(NutriByte.person.ingredientsToWatch.trim());
                    for (NutriProfiler.PhysicalActivityEnum pae : NutriProfiler.PhysicalActivityEnum.values()) {
                        if (NutriByte.person.physicalActivityLevel <= pae.getPhysicalActivityLevel()) {
                            NutriByte.view.physicalActivityComboBox.getSelectionModel().select(pae.getName());
                            break;
                        }
                    }
                    NutriProfiler.createNutriProfile(NutriByte.person);
                    //The following parts pop diet product list to diet table view and product table, and also update the graph
                    if (NutriByte.model.searchResultsList.size() != 0){
                        NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultsList.size() + " product(s) found");
                        ObservableList<Product.ProductNutrient> pnList = FXCollections.observableArrayList(NutriByte.model.searchResultsList.get(0).getProductNutrients().values());
                        NutriByte.view.productNutrientsTableView.setItems(pnList);
                        NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
                        NutriByte.person.populateDietNutrientMap();
                        NutriByte.view.nutriChart.updateChart();
                        NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
                        NutriByte.view.productsComboBox.getSelectionModel().selectFirst();
                    }
                } catch (InvalidProfileException e) {
                    System.out.println("Warning:::Please verify your input.");
                }
            }
        }
    }

    /**
     * This method handles click event of open menu. It opens the file chooser and let user select target file
     * then system load the file, create person, and display all information on the screen.
     **/
    class SaveMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                dataValidator();

                FileChooser fc = new FileChooser();
                fc.setTitle("Save File");
                fc.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Profile Files (only csv)", "*.csv"), new FileChooser.ExtensionFilter("All Files", "*.*"));

                File file = fc.showSaveDialog(NutriByte.view.root.getScene().getWindow());
                if (file != null) {
                    NutriByte.model.writeProfile(file.getAbsolutePath());
                }
            } catch (InvalidProfileException ie) {
                System.out.println("Warning:::Input validation failed");
            }
        }
    }

    /**
     * This method handles click event of new menu. it remove all existing information and create a clear panel
     **/
    class NewMenuItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            clearPanel();
            NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
        }
    }

    /**
     * This method handles click event of new menu. it remove all existing information and create a clear panel
     **/
    class CloseMenuItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
        }
    }

    /**
     * this method handles click event on about menu. and display a description about this app.
     **/

    class AboutMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("NutriByte");
            alert.setContentText("Version 2.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
            Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            alert.setGraphic(imageView);
            alert.showAndWait();
        }
    }

    /**
     * This method handles click event on search button. and display search result in the result table
     **/
    class SearchButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(final ActionEvent event) {
            NutriByte.model.searchResultsList.clear();
            String productToSearch = NutriByte.view.productSearchTextField.getText().toLowerCase();
            String nutrientToSearch = NutriByte.view.nutrientSearchTextField.getText().toLowerCase();
            String ingredientToSearch = NutriByte.view.ingredientSearchTextField.getText().toLowerCase();
            //This part gets the target nutrient code from nutrient inputted by user
            List<String> nutriToSearchCodeList = new ArrayList<>();
            if (!nutrientToSearch.isEmpty()) {
                for (Map.Entry<String, Nutrient> entry : Model.nutrientsMap.entrySet()) {
                    if (entry.getValue().getNutrientName().toLowerCase().contains(nutrientToSearch.toLowerCase())) {
                        nutriToSearchCodeList.add(entry.getKey());
                    } else {
                        nutriToSearchCodeList.add(null);
                    }
                }
            }
            //This part finds search result
            for (Map.Entry<String, Product> entry : Model.productsMap.entrySet()) {
                boolean productContain = true;
                if (!productToSearch.isEmpty()) {
                    productContain = entry.getValue().getProductName().toLowerCase().contains(productToSearch);
                }
                boolean nutrientContain = true;
                if (!nutrientToSearch.isEmpty()) {
                    for (String code : nutriToSearchCodeList) {
                        nutrientContain = entry.getValue().getProductNutrients().containsKey(code);
                        if (nutrientContain) break;
                    }
                }
                boolean ingredientContain = true;
                if (!ingredientToSearch.isEmpty()) {
                    ingredientContain = entry.getValue().getIngredients().toLowerCase().contains(ingredientToSearch);
                }
                if (productContain && nutrientContain && ingredientContain) {
                    NutriByte.model.searchResultsList.add(entry.getValue());
                }
            }

            //the following parts set all results in label and columns
            NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
            NutriByte.view.productsComboBox.getSelectionModel().select(0);
            NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultsList.size() + " product(s) found");

            if (NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem() != null) {
                Product selectedProduct = NutriByte.view.productsComboBox.getValue();
                ObservableList<Product.ProductNutrient> pnList = FXCollections.observableArrayList(selectedProduct.getProductNutrients().values());
                NutriByte.view.productNutrientsTableView.setItems(pnList);
                NutriByte.view.productIngredientsTextArea.setText(selectedProduct.getIngredients());
                NutriByte.view.servingSizeLabel.setText(String.format("%.2f %s", selectedProduct.getServingSize(), selectedProduct.getServingUom()));
                NutriByte.view.householdSizeLabel.setText(String.format("%.2f %s", selectedProduct.getHouseholdSize(), selectedProduct.getHouseholdUom()));
                NutriByte.view.dietServingUomLabel.setText(selectedProduct.getServingUom());
                NutriByte.view.dietHouseholdUomLabel.setText(selectedProduct.getHouseholdUom());
            }
        }
    }

    /**
     * This method handles click event on clear button. and clear result in the table and text area
     **/
    class ClearButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(final ActionEvent event) {
            NutriByte.view.productSearchTextField.clear();
            NutriByte.view.nutrientSearchTextField.clear();
            NutriByte.view.ingredientSearchTextField.clear();
            NutriByte.view.searchResultSizeLabel.setText("");
            NutriByte.view.productsComboBox.getSelectionModel().clearSelection();
            NutriByte.view.productIngredientsTextArea.clear();
            NutriByte.view.servingSizeLabel.setText("0.00");
            NutriByte.view.householdSizeLabel.setText("0.00");
            NutriByte.view.dietServingUomLabel.setText("");
            NutriByte.view.dietHouseholdUomLabel.setText("");
        }
    }

    /**
     * This method handles click event on add diet button. and add target diet product in to diet plan
     **/
    class AddDietButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(final ActionEvent event) {
            float productQuantity = 1;
            Product selectedProduct = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                if (NutriByte.person == null) NutriByte.person=new Female();
                NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
                if ((!NutriByte.view.dietServingSizeTextField.getText().isEmpty() || !NutriByte.view.dietHouseholdSizeTextField.getText().isEmpty()) && selectedProduct.getServingSize() > 0 && selectedProduct.getHouseholdSize() > 0) {
                    try {
                        productQuantity = Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText()) / selectedProduct.getServingSize();
                        System.out.println(productQuantity);
                    } catch (Exception e) {
                        productQuantity = Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText()) / selectedProduct.getHouseholdSize();
                    }
                }
                //The following parts creates a copy of products for the table is calculated with quantity user inputted.
                Product addedProduct = new Product(selectedProduct.getNdbNumber(), selectedProduct.getProductName(), selectedProduct.getManufacturer(), selectedProduct.getIngredients());
                addedProduct.setServingSize(selectedProduct.getServingSize() * productQuantity);
                addedProduct.setServingUom(selectedProduct.getServingUom());
                if (selectedProduct.getHouseholdSize() == 0){
                    addedProduct.setHouseholdSize(productQuantity);
                }
                else addedProduct.setHouseholdSize(selectedProduct.getHouseholdSize() * productQuantity);
                addedProduct.setHouseholdUom(selectedProduct.getHouseholdUom());
                Map<String, Product.ProductNutrient> copyProductNutrients = new HashMap<>();
                for (Map.Entry<String, Product.ProductNutrient> pn : selectedProduct.getProductNutrients().entrySet()) {
                    copyProductNutrients.put(pn.getKey(), addedProduct.new ProductNutrient(selectedProduct.getNdbNumber(), selectedProduct.getProductNutrients().get(pn.getKey()).getNutrientQuantity() * addedProduct.getServingSize() / 100));
                }
                ObservableMap<String, Product.ProductNutrient> copiedProductNutrients = FXCollections.observableMap(copyProductNutrients);
                addedProduct.setProductNutrients(copiedProductNutrients);

                NutriByte.person.dietProductsList.add(addedProduct);
                NutriByte.person.populateDietNutrientMap();
                NutriByte.view.nutriChart.updateChart();
            } else {
                new InvalidProfileException("Please search product first.");
            }
        }
    }
    /**
     * This method handles click event on remove diet button. and remove target diet product in to diet plan
     **/
    class RemoveDietButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(final ActionEvent event) {
            if (NutriByte.person != null) {
                if (NutriByte.person.dietProductsList.size() > 0) {
                    NutriByte.person.dietProductsList.remove(NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedItem());
                }
                if (NutriByte.person.dietProductsList.size() == 0) {
                    NutriByte.view.nutriChart.clearChart();
                } else {
                    NutriByte.person.populateDietNutrientMap();
                    NutriByte.view.nutriChart.updateChart();
                }
            }else{
                new InvalidProfileException("Please input personal data.");
            }
        }
    }

    /**
     * ############The following are helper methods#########
     **/
    //this is a helper method to create the person as gender changes
    public void personCreater() {
        if (NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Female")) {
            NutriByte.person = new Female();
        } else if (NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Male")) {
            NutriByte.person = new Male();
        }
        if (!NutriByte.view.ageTextField.getText().trim().isEmpty()) {
            NutriByte.person.age = Float.parseFloat(NutriByte.view.ageTextField.getText());
            NutriByte.person.findAgeGroup();
        }
        if (!NutriByte.view.weightTextField.getText().trim().isEmpty())
            NutriByte.person.weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
        if (!NutriByte.view.heightTextField.getText().trim().isEmpty())
            NutriByte.person.height = Float.parseFloat(NutriByte.view.heightTextField.getText());
        if (!(NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem() == null || NutriByte.view.physicalActivityComboBox.getSelectionModel().isEmpty()))
            NutriByte.person.physicalActivityLevel = activityLevelGetter();
        else {
            NutriByte.person.physicalActivityLevel = 1;
        }
        NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
    }

    //this is a helper method to help get activity level from enum
    public float activityLevelGetter() {
        float physicalActivityLevel = 0;
        for (NutriProfiler.PhysicalActivityEnum pae : NutriProfiler.PhysicalActivityEnum.values()) {
            if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem().equals(pae.getName())) {
                physicalActivityLevel = pae.getPhysicalActivityLevel();
            }
        }
        return physicalActivityLevel;
    }

    //this is a helper method to help dynamic binding check if contains are all filled.
    public boolean inputEmptyValidator() {
        return NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem() != null && !NutriByte.view.ageTextField.getText().trim().isEmpty() && !NutriByte.view.weightTextField.getText().trim().isEmpty() && !NutriByte.view.heightTextField.getText().trim().isEmpty() && NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem() != null;
    }

    //this is a validator to check if inputs are valid
    //return -1 means it is a negative number, 0 means it is not a number, 1 means correct
    public int numberInputValidator(String input) {
        float data ;
        try {
            data = Float.parseFloat(input);
            if (data < 0) return -1;
        } catch (NumberFormatException e) {
            return 0;
        }
        return 1;
    }

    //this method validats all data and throw exception
    private void dataValidator() throws InvalidProfileException {
        //validate on gender combox
        if (NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem() == null || (!NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Female") && !NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Male"))) {
            throw new InvalidProfileException("Missing Gender Information");
        }
        //Validate on age text field
        if (NutriByte.view.ageTextField.getText().trim().isEmpty())
            throw new InvalidProfileException("Missing age information");
        else if (numberInputValidator(NutriByte.view.ageTextField.getText()) == 0)
            throw new InvalidProfileException("Incorrect age number, must be a number");
        else if (numberInputValidator(NutriByte.view.ageTextField.getText()) == -1)
            throw new InvalidProfileException("Age must be a positive number");
        //Validate on weight text field
        if (NutriByte.view.weightTextField.getText().trim().isEmpty())
            throw new InvalidProfileException("Missing weight information");
        else if (numberInputValidator(NutriByte.view.weightTextField.getText()) == 0)
            throw new InvalidProfileException("Incorrect weight number, must be a number");
        else if (numberInputValidator(NutriByte.view.weightTextField.getText()) == -1)
            throw new InvalidProfileException("Weight must be a positive number");
        //Validate on height text field
        if (NutriByte.view.heightTextField.getText().trim().isEmpty())
            throw new InvalidProfileException("Missing height information");
        else if (numberInputValidator(NutriByte.view.heightTextField.getText()) == 0)
            throw new InvalidProfileException("Incorrect height number, must be a number");
        else if (numberInputValidator(NutriByte.view.heightTextField.getText()) == -1)
            throw new InvalidProfileException("Height must be a positive number");
    }

    //this is a helper method to clear everything
    private void clearPanel() {
        NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
        NutriByte.view.productSearchTextField.clear();
        NutriByte.view.nutrientSearchTextField.clear();
        NutriByte.view.ingredientSearchTextField.clear();
        NutriByte.view.productsComboBox.getSelectionModel().clearSelection();
        NutriByte.view.dietServingSizeTextField.clear();
        NutriByte.view.dietHouseholdSizeTextField.clear();
        NutriByte.view.dietProductsTableView.setItems(null);
        NutriByte.view.recommendedNutrientsTableView.setItems(null);
        NutriByte.model.searchResultsList.clear();
        NutriByte.view.nutriChart.clearChart();
        NutriByte.person = null;
        NutriByte.view.initializePrompts();
    }
}
