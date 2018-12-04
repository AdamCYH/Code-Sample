//Yihiu Chiu
//yihiuc
package hw3;


import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.Objects;


public class Product {
    private StringProperty ndbNumber = new SimpleStringProperty();
    private StringProperty productName = new SimpleStringProperty();
    private StringProperty manufacturer = new SimpleStringProperty();
    private StringProperty ingredients = new SimpleStringProperty();
    private FloatProperty servingSize = new SimpleFloatProperty();
    private StringProperty servingUom = new SimpleStringProperty();
    private FloatProperty householdSize = new SimpleFloatProperty();
    private StringProperty householdUom = new SimpleStringProperty();
    private ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();


    Product(){
        ndbNumber.set("");
        productName.set("");
        manufacturer.set("");
        ingredients.set("");
        servingUom.set("");
        householdUom.set("");
    }

    Product(String ndbNumber, String productName, String manufacturer, String ingredients){
        this.ndbNumber.set(ndbNumber);
        this.productName.set(productName);
        this.manufacturer.set(manufacturer);
        this.ingredients.set(ingredients);
    }

    @Override
    public String toString(){
        return this.productName.get() + " by " + this.manufacturer.get();
    }

    @Override
    public int hashCode(){return Objects.hash(productName);}

    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        return productName.equals(((Product)o).productName);
    }

    /**Getter & Setter for ndbNumber**/
    public final String getNdbNumber() {
        return ndbNumber.get();
    }

    public final StringProperty ndbNumberProperty() {
        return ndbNumber;
    }

    public final void setNdbNumber(String ndbNumber) {
        this.ndbNumber.set(ndbNumber);
    }

    /**Getter & Setter for ProductName**/
    public final String getProductName() {
        return productName.get();
    }

    public final StringProperty productNameProperty() {
        return productName;
    }

    public final void setProductName(String productName) {
        this.productName.set(productName);
    }

    /**Getter & Setter for Manufacturer**/
    public final String getManufacturer() {
        return manufacturer.get();
    }

    public final StringProperty manufacturerProperty() {
        return manufacturer;
    }

    public final void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    /**Getter & Setter for Ingredients**/
    public final String getIngredients() {
        return ingredients.get();
    }

    public final StringProperty ingredientsProperty() {
        return ingredients;
    }

    public final void setIngredients(String ingredients) {
        this.ingredients.set(ingredients);
    }

    /**Getter & Setter for Serving Size**/
    public final float getServingSize() {
        return servingSize.get();
    }

    public final FloatProperty servingSizeProperty() {
        return servingSize;
    }

    public final void setServingSize(float servingSize) {
        this.servingSize.set(servingSize);
    }

    /**Getter & Setter for Serving Uom**/
    public final String getServingUom() {
        return servingUom.get();
    }

    public final StringProperty servingUomProperty() {
        return servingUom;
    }

    public final void setServingUom(String servingUom) {
        this.servingUom.set(servingUom);
    }

    /**Getter & Setter for Household Size**/
    public final float getHouseholdSize() {
        return householdSize.get();
    }

    public final FloatProperty householdSizeProperty() {
        return householdSize;
    }

    public final void setHouseholdSize(float householdSize) {
        this.householdSize.set(householdSize);
    }

    /**Getter & Setter for Household Uom**/
    public final String getHouseholdUom() {
        return householdUom.get();
    }

    public final StringProperty householdUomProperty() {
        return householdUom;
    }

    public final void setHouseholdUom(String householdUom) {
        this.householdUom.set(householdUom);
    }

    /**Getter & Setter for ProductNutrients**/
    public final ObservableMap<String, ProductNutrient> getProductNutrients() {
        return productNutrients;
    }

    public final void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
        this.productNutrients = productNutrients;
    }

    public class ProductNutrient {
        private StringProperty nutrientCode = new SimpleStringProperty();
        private FloatProperty nutrientQuantity = new SimpleFloatProperty();

        ProductNutrient(){
            this.nutrientCode.set("");
        }
        ProductNutrient(String nutrientCode,float nutrientQuantity){
            this.nutrientCode.set(nutrientCode);
            this.nutrientQuantity.set(nutrientQuantity);
        }

        /**Getter & Setter for nutrient code**/
        public final String getNutrientCode() {
            return nutrientCode.get();
        }

        public final StringProperty nutrientCodeProperty() {
            return nutrientCode;
        }

        public final void setNutrientCode(String nutrientCode) {
            this.nutrientCode.set(nutrientCode);
        }

        /**Getter & Setter for nutrient quantity**/
        public final float getNutrientQuantity() {
            return nutrientQuantity.get();
        }

        public final FloatProperty nutrientQuantityProperty() {
            return nutrientQuantity;
        }

        public final void setNutrientQuantity(float nutrientQuantity) {
            this.nutrientQuantity.set(nutrientQuantity);
        }
    }
}
