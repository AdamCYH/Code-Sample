//Yihiu Chiu
//yihiuc
package hw3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NutriByte extends Application{
	static Model model = new Model();  	//made static to make accessible in the controller
	static View view = new View();		//made static to make accessible in the controller
	static Person person;				//made static to make accessible in the controller


	Controller controller = new Controller();	//all event handlers

	/**Uncomment the following three lines if you want to try out the full-size data files */
//	static final String PRODUCT_FILE = "data/Products.csv";
//	static final String NUTRIENT_FILE = "data/Nutrients.csv";
//	static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**The following constants refer to the data files to be used for this application */
	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; //Refers to the file holding NutriByte logo image

	static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;
	/**
	 * This method starts the Javafx application, and set stage and scene
	 * **/
	@Override
	public void start(Stage stage) {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE );
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus();  //this keeps focus on entire window and allows the textfield-prompt to be visible
		setupBindings();
		stage.setTitle("NutriByte 3.0");
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * main method that starts the application
	 * **/
	public static void main(String[] args) {
		launch(args);
	}
	/**
	 * This method sets all action listeners and bindings.
	 * **/
	void setupBindings() {
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());
		view.closeNutriProfileMenuItem.setOnAction(controller.new CloseMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());

		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
		//#############################################################################################################################################
		//#############################The Following parts set up the binding and change listener for <Recommend Nutrients>############################
		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);
		//The following part adds the change listener to combobox, if there is a change, correlated person object will be crated.
		view.genderComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (view.genderComboBox.getSelectionModel().getSelectedItem() == null || view.genderComboBox.getSelectionModel().getSelectedItem().isEmpty()){
				person=null;
			}else{
				controller.personCreater();
				if (controller.inputEmptyValidator()) NutriProfiler.createNutriProfile(person);
			}
		});
		//The following part adds the change listener to age text field, if there is a change in age, data will be refreshed
		view.ageTextField.textProperty().addListener((observable, oldValue, newValue) ->{
			if (controller.numberInputValidator(NutriByte.view.ageTextField.getText()) == 1){
				view.ageTextField.setStyle("-fx-text-fill: black");
				if(person != null && !view.ageTextField.getText().trim().isEmpty()) {
					person.age = Float.parseFloat(view.ageTextField.getText());
					person.findAgeGroup();
					if (controller.inputEmptyValidator()) NutriProfiler.createNutriProfile(person);
				}
			}else{
				view.ageTextField.setStyle("-fx-text-fill: red");
			}

		});
		//The following part adds the change listener to weight text field, if there is a change in weight, data will be refreshed
		view.weightTextField.textProperty().addListener((observable, oldValue, newValue) ->{
			if (controller.numberInputValidator(NutriByte.view.weightTextField.getText()) == 1){
				view.weightTextField.setStyle("-fx-text-fill: black");
				if(person != null && !view.weightTextField.getText().trim().isEmpty()) {
					person.weight = Float.parseFloat(view.weightTextField.getText());
					if (controller.inputEmptyValidator()) NutriProfiler.createNutriProfile(person);
				}
			}else{
				view.weightTextField.setStyle("-fx-text-fill: red");
			}
		});
		//The following part adds the change listener to height text field, if there is a change in height, data will be refreshed
		view.heightTextField.textProperty().addListener((observable, oldValue, newValue) ->{
			if (controller.numberInputValidator(NutriByte.view.heightTextField.getText()) == 1){
				view.heightTextField.setStyle("-fx-text-fill: black");
				if(person != null && !view.heightTextField.getText().trim().isEmpty()) {
					person.height = Float.parseFloat(view.heightTextField.getText());
					if (controller.inputEmptyValidator()) NutriProfiler.createNutriProfile(person);
				}
			}else{
				view.heightTextField.setStyle("-fx-text-fill: red");
			}
		});
		//The following part adds the change listener to activity combobox, if there is a change in activity, data will be refreshed
		view.physicalActivityComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(person != null) {
				person.physicalActivityLevel = controller.activityLevelGetter();
				if (controller.inputEmptyValidator()) NutriProfiler.createNutriProfile(person);
			}
		});
		//The following part adds the change listener to ingredient to watch, if there is a change in ingredient, it will be added to person object
		view.ingredientsToWatchTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
			if(person != null && !view.ingredientsToWatchTextArea.getText().trim().isEmpty()) {
				person.ingredientsToWatch = view.ingredientsToWatchTextArea.getText();
			}
		});
		//#############################################################################################################################################
		//#############################The Following parts set up the binding and change listener for <Product Nutrients>##############################
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());
		//Product Nutrient Search Binding
		view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productNutrientUOMCallback);

		view.productsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (view.productsComboBox.getSelectionModel().getSelectedItem() == null) {
				model.searchResultsList.clear();
				view.productNutrientsTableView.setItems(null);
				NutriByte.view.productIngredientsTextArea.setText("");
				NutriByte.view.servingSizeLabel.setText("");
				NutriByte.view.householdSizeLabel.setText("");
				NutriByte.view.dietServingUomLabel.setText("");
				NutriByte.view.dietHouseholdUomLabel.setText("");
			}else{
				Product selectedProduct = view.productsComboBox.getValue();
				ObservableList<Product.ProductNutrient> pnList = FXCollections.observableArrayList(selectedProduct.getProductNutrients().values());
				view.productNutrientsTableView.setItems(pnList);
				view.productIngredientsTextArea.setText(selectedProduct.getIngredients());
				NutriByte.view.servingSizeLabel.setText(selectedProduct.getServingSize() + " " + selectedProduct.getServingUom());
				NutriByte.view.householdSizeLabel.setText(selectedProduct.getHouseholdSize() + " " + selectedProduct.getHouseholdUom());
				NutriByte.view.dietServingUomLabel.setText(selectedProduct.getServingUom());
				NutriByte.view.dietHouseholdUomLabel.setText(selectedProduct.getHouseholdUom());
			}
		});
		//#############################################################################################################################################
		//#############################The Following parts set up the binding and change listener for <Diet Products>##############################
		view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());
		view.dietProductNameColumn.setCellValueFactory(productNameCallback);
		view.dietServingSizeColumn.setCellValueFactory(productServingSizeCallback);
		view.dietServingUomColumn.setCellValueFactory(productServingUOMCallback);
		view.dietHouseholdSizeColumn.setCellValueFactory(productHouseholdSizeCallback);
		view.dietHouseholdUomColumn.setCellValueFactory(productHouseholdUOMCallback);
	}


	/**######################################################################################**/
	/**########################Recommend Nutrients Table Callback############################**/
	/**
	 * this method binds table cell with observable value nutrient name
	 * **/
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};
	/**
	 * this method binds table cell with observable value nutrient quantity
	 * **/
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = arg0 -> new SimpleStringProperty(String.format("%.2f", arg0.getValue().getNutrientQuantity()));
	/**
	 * this method binds table cell with observable value nutrient uom
	 * **/
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = arg0 -> {
		Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
		return nutrient.nutrientUomProperty();
	};

	/**########################Search Result Table Callback############################**/
	/**
	 * this method binds table cell with observable value nutrient name
	 * **/
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback = arg0 -> Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientNameProperty();
	/**
	 * this method binds table cell with observable value nutrient name
	 * **/
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback = arg0 -> new SimpleStringProperty(String.format("%.2f",arg0.getValue().getNutrientQuantity()));
	/**
	 * this method binds table cell with observable value nutrient name
	 * **/
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientUOMCallback = arg0 -> Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientUomProperty();

	/**########################diet product Table Callback############################**/
	/**
	 * this method binds table cell with observable value product name
	 * **/
	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> productNameCallback = arg0 -> arg0.getValue().productNameProperty();
	/**
	 * this method binds table cell with observable value serving size
	 * **/
	Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> productServingSizeCallback = arg0 -> arg0.getValue().servingSizeProperty().asObject();
	/**
	 * this method binds table cell with observable value serving UOM
	 * **/
	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> productServingUOMCallback = arg0 -> arg0.getValue().servingUomProperty();
	/**
	 * this method binds table cell with observable value household size
	 * **/
	Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> productHouseholdSizeCallback = arg0 -> arg0.getValue().householdSizeProperty().asObject();
	/**
	 * this method binds table cell with observable value household UOM
	 * **/
	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> productHouseholdUOMCallback = arg0 -> arg0.getValue().householdUomProperty();
}
