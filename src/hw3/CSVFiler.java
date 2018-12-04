//Yihiu Chiu
//yihiuc
package hw3;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVFiler extends DataFiler {
    CSVFormat csvFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames();
    @Override
    public void writeFile(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            String gender = null;
            if (NutriByte.person instanceof Male) {
                gender = "Male";
            }else{
                gender = "Female";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(gender).append(", ").append(NutriByte.person.age).append(", ").append(NutriByte.person.weight).append(", ").append(NutriByte.person.height).append(", ").append(NutriByte.person.physicalActivityLevel).append(", ").append(NutriByte.person.ingredientsToWatch).append("\n");
            for (Product product : NutriByte.person.dietProductsList){
                sb.append(product.getNdbNumber()).append(", ").append(product.getServingSize()).append(", ").append(product.getHouseholdSize()).append("\n");
            }
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads CSV files, and create person based on gender.
     * **/
    @Override
    public boolean readFile(String filename) throws InvalidProfileException{
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            try{
                validatePersonData(br.readLine());
            }catch (InvalidProfileException e){
                throw new InvalidProfileException("Could not read profile data");
            }
            String productData = "";
            List<Product> readDietProducts = new ArrayList<>();
            while((productData = br.readLine())!=null){
                try{
                    readDietProducts.add(validateProductData(productData));
                }catch (InvalidProfileException ie){
                    System.out.println("Warning:::please validate data file.");
                }

            }
            for(Product product : readDietProducts){
                Product targetProductinMap = Model.productsMap.get(product.getNdbNumber());
                Product newDietProduct = new Product(targetProductinMap.getNdbNumber(), targetProductinMap.getProductName(), targetProductinMap.getManufacturer(), targetProductinMap.getIngredients());
                newDietProduct.setServingSize(product.getServingSize());
                newDietProduct.setServingUom(targetProductinMap.getServingUom());
                newDietProduct.setHouseholdSize(product.getHouseholdSize());
                newDietProduct.setHouseholdUom(targetProductinMap.getHouseholdUom());
                NutriByte.person.dietProductsList.add(newDietProduct);
                NutriByte.model.searchResultsList.add(targetProductinMap);

                for (Map.Entry<String,Product.ProductNutrient> pn : targetProductinMap.getProductNutrients().entrySet()){
                    newDietProduct.getProductNutrients().put(pn.getKey(), newDietProduct.new ProductNutrient(targetProductinMap.getNdbNumber(), targetProductinMap.getProductNutrients().get(pn.getKey()).getNutrientQuantity() * newDietProduct.getServingSize() /100));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    Person validatePersonData(String data) throws IOException {
        StringBuilder nutriToAvoid = new StringBuilder();
        CSVParser csvParser = CSVParser.parse(data, csvFormat);
        List<CSVRecord> userInfoRecord = csvParser.getRecords();
        if (!userInfoRecord.get(0).get(0).equalsIgnoreCase("male") && !userInfoRecord.get(0).get(0).equalsIgnoreCase("female") ) throw new InvalidProfileException("The profile must have gender. Female or Male as first word");
        switch (userInfoRecord.get(0).get(0).toUpperCase()) {
            case "FEMALE":
                NutriByte.view.genderComboBox.getSelectionModel().select("Female");
                break;
            case "MALE":
                NutriByte.view.genderComboBox.getSelectionModel().select("Male");
                break;
        }
        try{
            NutriByte.person.age = Float.parseFloat(userInfoRecord.get(0).get(1).trim());
        }catch (NumberFormatException e){
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid data for age: " + userInfoRecord.get(0).get(1).trim() + "\n" + "Age must be a number");
        }
        try{
            NutriByte.person.weight = Float.parseFloat(userInfoRecord.get(0).get(2).trim());
        }catch (NumberFormatException e){
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid data for weight: " + userInfoRecord.get(0).get(2).trim() + "\n" + "Weight must be a number");
        }
        try{
            NutriByte.person.height = Float.parseFloat(userInfoRecord.get(0).get(3).trim());
        }catch (NumberFormatException e){
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid data for height: " + userInfoRecord.get(0).get(3).trim() + "\n" + "Height must be a number");
        }
        try{
            NutriByte.person.physicalActivityLevel = Float.parseFloat(userInfoRecord.get(0).get(4).trim());
        }catch (NumberFormatException e){
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid data for activity level: " + userInfoRecord.get(0).get(4).trim() + "\n" + "Activity level must be a number");
        }
        if (NutriByte.person.age < 0) {
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid age:" + userInfoRecord.get(0).get(1).trim());
        }
        if (NutriByte.person.weight < 0) {
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid weight:" + userInfoRecord.get(0).get(2).trim());
        }
        if (NutriByte.person.height < 0) {
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid height:" + userInfoRecord.get(0).get(3).trim());
        }
        if (!(NutriByte.person.physicalActivityLevel == 1.0f || NutriByte.person.physicalActivityLevel == 1.1f || NutriByte.person.physicalActivityLevel == 1.25f || NutriByte.person.physicalActivityLevel == 1.48f)) {
            NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
            throw new InvalidProfileException("Invalid physical activity level:" + userInfoRecord.get(0).get(4).trim() + "\n" + "Must be 1.0, 1.1, 1.25, 1.48");
        }

        for (int i = 5; i < userInfoRecord.get(0).size(); i++) {
                nutriToAvoid.append(userInfoRecord.get(0).get(i));
                if (i != userInfoRecord.get(0).size() - 1) nutriToAvoid.append(",");
            }
        NutriByte.person.ingredientsToWatch = nutriToAvoid.toString();

        return NutriByte.person;
    }

    Product validateProductData(String data) throws IOException, InvalidProfileException {
        Product loadProduct = new Product();
        CSVParser csvParser = CSVParser.parse(data, csvFormat);
        List<CSVRecord> userInfoRecord = csvParser.getRecords();
        if (!Model.productsMap.containsKey(userInfoRecord.get(0).get(0))) throw new InvalidProfileException("No product found with this code: " + userInfoRecord.get(0).get(0));
        loadProduct.setNdbNumber(userInfoRecord.get(0).get(0));
        float servingSize;
        float householdSize;
        StringBuilder sb = new StringBuilder();
        try{
            if (userInfoRecord.get(0).size() != 3) {
                for (int i = 0; i < userInfoRecord.get(0).size(); i++){
                    sb.append(userInfoRecord.get(0).get(i));
                    if (i != userInfoRecord.get(0).size()-1) sb.append(", ");
                }
                throw new InvalidProfileException("Cannot read: " + sb.toString() + "\n" + "The data must be - String, Number, Number - for ndb number, \nserving size, household size");
            }
            servingSize = Float.parseFloat(userInfoRecord.get(0).get(1));

            loadProduct.setServingSize(servingSize);
            householdSize = Float.parseFloat(userInfoRecord.get(0).get(2));
            loadProduct.setHouseholdSize(householdSize);
        }catch (NumberFormatException e){
            throw new InvalidProfileException("Cannot read: " + userInfoRecord.get(0).get(0) + ", " + userInfoRecord.get(0).get(1) + ", " + userInfoRecord.get(0).get(2) + "\n" + "The data must be - String, Number, Number - for ndb number, serving size, household size");
        }
        return loadProduct;
    }

}
