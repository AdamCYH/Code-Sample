//Yihiu Chiu
//yihiuc
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nutrient {
    private StringProperty nutrientCode = new SimpleStringProperty();
    private StringProperty nutrientName = new SimpleStringProperty();
    private StringProperty nutrientUom = new SimpleStringProperty();

    Nutrient(){
        this.nutrientCode.set("");
        this.nutrientName.set("");
        this.nutrientUom.set("");
    }

    Nutrient(String nutrientCode, String nutrientName, String nutrientUom){
        this.nutrientCode.set(nutrientCode);
        this.nutrientName.set(nutrientName);
        this.nutrientUom.set(nutrientUom);

    }
    /**getter & setter for NutrientCode*/
    public final String getNutrientCode() {
        return nutrientCode.get();
    }

    public final StringProperty nutrientCodeProperty() {
        return nutrientCode;
    }

    public final void setNutrientCode(String nutrientCode) {
        this.nutrientCode.set(nutrientCode);
    }

    /**getter & setter for NutrientName**/
    public final String getNutrientName() {
        return nutrientName.get();
    }

    public final StringProperty nutrientNameProperty() {
        return nutrientName;
    }

    public final void setNutrientName(String nutrientName) {
        this.nutrientName.set(nutrientName);
    }

    /**getter & setter for NutrientUom**/
    public final String getNutrientUom() {
        return nutrientUom.get();
    }

    public final StringProperty nutrientUomProperty() {
        return nutrientUom;
    }

    public final void setNutrientUom(String nutrientUom) {
        this.nutrientUom.set(nutrientUom);
    }
}
