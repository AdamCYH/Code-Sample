//Yihiu Chiu
//yihiuc
package hw3;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Map;

public abstract class Person {
	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

	NutriProfiler.AgeGroupEnum ageGroup;

	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();


	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();
	ObservableList<Product> dietProductsList = FXCollections.observableArrayList();
	ObservableMap<String, RecommendedNutrient> dietNutrientsMap = FXCollections.observableHashMap();

	Person() {
		this.ingredientsToWatch = "";
		findAgeGroup();
	}

	Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;
		findAgeGroup();
	}

	//returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT. 
	//Each value is calculated as follows:
	//For Protein, it multiples the constant with the person's weight.
	//For Carb and Fiber, it simply takes the constant from the 
	//nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	//For others, it multiples the constant with the person's weight and divides by 1000.
	//Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc.
	float[] calculateNutriRequirement() {
		int ageIndex = ageGroup.getAgeGroupIndex();
		float[] nutriRuqirement = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];

		for (int i = 0; i < NutriProfiler.RECOMMENDED_NUTRI_COUNT; i++){
			if (i == NutriProfiler.NutriEnum.PROTEIN.getNutriIndex()){
				nutriRuqirement[i] = nutriConstantsTable[i][ageIndex] * weight;
			}
			else if(i == NutriProfiler.NutriEnum.CARBOHYDRATE.getNutriIndex() || i == NutriProfiler.NutriEnum.FIBER.getNutriIndex()){
				nutriRuqirement[i] = nutriConstantsTable[i][ageIndex];
			}else{
				nutriRuqirement[i] = nutriConstantsTable[i][ageIndex] * weight / 1000;
			}
		}
		return nutriRuqirement;
	}
	/**
	 * this is a helper method that helps find age group.
	 * **/
	void findAgeGroup() {
		for (NutriProfiler.AgeGroupEnum e : NutriProfiler.AgeGroupEnum.values()){
			if (age <= e.getAge()){
				ageGroup = e;
				break;
			}
		}
	}

	void populateDietNutrientMap(){
		dietNutrientsMap.clear();
		for (Product product : dietProductsList){
			for (Map.Entry<String, Product.ProductNutrient> entry : product.getProductNutrients().entrySet()){
				if (!dietNutrientsMap.containsKey(entry.getKey())){
					dietNutrientsMap.put(entry.getKey(), new RecommendedNutrient(entry.getKey(), entry.getValue().getNutrientQuantity()));
				}
				else{
					dietNutrientsMap.get(entry.getKey()).setNutrientQuantity(dietNutrientsMap.get(entry.getKey()).getNutrientQuantity() + entry.getValue().getNutrientQuantity());
				}
			}
		}
	}
}
