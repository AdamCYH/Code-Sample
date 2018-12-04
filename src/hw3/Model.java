//Yihiu Chiu
//yihiuc
package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Model {
    static ObservableMap<String, Product> productsMap = FXCollections.observableHashMap();
    static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableHashMap();
    ObservableList<Product> searchResultsList = FXCollections.observableArrayList();

    CSVFormat csvFormat;
    /**
     * Read products file
     **/
    void readProducts(String filename) {
        csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try {
            CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
            for (CSVRecord csvRecord : csvParser) {
                productsMap.put(csvRecord.get(0), new Product(csvRecord.get(0), csvRecord.get(1), csvRecord.get(4), csvRecord.get(7)));
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read Nutrients file
     **/
    void readNutrients(String filename) {
        csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try {
            CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
            for (CSVRecord csvRecord : csvParser) {
                //check if duplicate, if not add to the map
                if (!nutrientsMap.containsKey(csvRecord.get(1)))
                    nutrientsMap.put(csvRecord.get(1), new Nutrient(csvRecord.get(1), csvRecord.get(2), csvRecord.get(5)));
                //add product nutrients to product.
                if (Float.parseFloat(csvRecord.get(4)) != 0)
                    productsMap.get(csvRecord.get(0)).getProductNutrients().put(csvRecord.get(1), productsMap.get(csvRecord.get(0)).new ProductNutrient(csvRecord.get(1), Float.parseFloat(csvRecord.get(4))));
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read ServingSize file
     **/
    void readServingSizes(String filename) {
        csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        Product targetProduct;
        try {
            CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
            for (CSVRecord csvRecord : csvParser) {
                //get target product and set all serving size related information
                targetProduct = productsMap.get(csvRecord.get(0));
                if (csvRecord.get(1) != null && !csvRecord.get(1).trim().isEmpty())
                    targetProduct.setServingSize(Float.parseFloat(csvRecord.get(1)));
                if (csvRecord.get(2) != null && !csvRecord.get(2).trim().isEmpty()) targetProduct.setServingUom(csvRecord.get(2));
                if (csvRecord.get(3) != null && !csvRecord.get(3).trim().isEmpty())
                    targetProduct.setHouseholdSize(Float.parseFloat(csvRecord.get(3)));
                if (csvRecord.get(4) != null && !csvRecord.get(4).trim().isEmpty()) targetProduct.setHouseholdUom(csvRecord.get(4));
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Read profiles based on extension.
     * **/
    void readProfiles(String filename) {
        DataFiler df = null;
        if (filename.substring(filename.length() - 3).equals("csv")) {
            df = new CSVFiler();
        } else if (filename.substring(filename.length() - 3).equals("xml")) {
            df = new XMLFiler();
        }
        df.readFile(filename);
    }

    /**
     * Writes profile into the system
     * **/
    void writeProfile(String filename){
        DataFiler df = new CSVFiler();
        df.writeFile(filename);
    }
}
