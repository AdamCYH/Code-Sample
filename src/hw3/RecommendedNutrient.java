//Yihiu Chiu
//yihiuc
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecommendedNutrient {
    private StringProperty nutrientCode = new SimpleStringProperty();
    private FloatProperty nutrientQuantity = new SimpleFloatProperty();

    RecommendedNutrient(){
        this.nutrientCode.set("");
    }

    RecommendedNutrient(String nutrientCode, float nutrientQuantity){
        this.nutrientCode.setValue(nutrientCode);
        this.nutrientQuantity.setValue(nutrientQuantity);
    }
    /**Getter & Setter for nutrient code**/
    public String getNutrientCode() {
        return nutrientCode.get();
    }

    public StringProperty nutrientCodeProperty() {
        return nutrientCode;
    }

    public void setNutrientCode(String nutrientCode) {
        this.nutrientCode.set(nutrientCode);
    }
    /**Getter & Setter for nutrient quantity**/
    public float getNutrientQuantity() {
        return nutrientQuantity.get();
    }

    public FloatProperty nutrientQuantityProperty() {
        return nutrientQuantity;
    }

    public void setNutrientQuantity(float nutrientQuantity) {
        this.nutrientQuantity.set(nutrientQuantity);
    }
}
