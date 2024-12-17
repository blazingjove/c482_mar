package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class addProductController implements Initializable {
    @FXML
    public AnchorPane addProductPane;
    private mainController mainController; // Reference to the main controller
    public void setMainControllerRef(mainController mainController) {
        this.mainController = mainController;
    }
    Stage stage;

    //text fields for user inputs
    @FXML private TextField productNameField;
    @FXML private TextField productInventoryField;
    @FXML private TextField productCostField;
    @FXML private TextField productMaxField;
    @FXML private TextField productMinField;
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableColumn<Part, Integer>  partName;
    @FXML private TableColumn<Part, Integer>  partInventory;
    @FXML private TableColumn<Part, Integer>  partCost;
    @FXML private TextField partSearchTextField;
    @FXML private TableView<Part> productPartTable;
    @FXML private TableColumn<Part, Integer> partID2;
    @FXML private TableColumn<Part, Integer>  partName2;
    @FXML private TableColumn<Part, Integer>  partInventory2;
    @FXML private TableColumn<Part, Integer>  partCost2;


    public void onProductSaveButtonClicked() {
        try {
            //error handling for blank sections
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            if (productCostField.getText().isEmpty() || productInventoryField.getText().isEmpty() || productMinField.getText().isEmpty() || productMaxField.getText().isEmpty() || productNameField.getText().isEmpty()) {
                errorAlert.setContentText("Please fill all the fields of the form");
                errorAlert.showAndWait();
                return;
            }

            String name = productNameField.getText();
            double price = Double.parseDouble(productCostField.getText());
            int stock = Integer.parseInt(productInventoryField.getText());
            int min = Integer.parseInt(productMinField.getText());
            int max = Integer.parseInt(productMaxField.getText());

            //checking valid max and mins are entered and stock is within those max and min limits
            if (min >= max || min < 0 || stock > max || stock < min) {
                errorAlert.setContentText("Inventory must be positive and between max and min/ max must be greater than min");
                errorAlert.showAndWait();
                return;
            }
            //verifying that the price is positive
            if (price < 0){
                errorAlert.setContentText("Price must be an integer greater than or equal to 0");
                errorAlert.showAndWait();
                return;
            }
            //limiting the price to 2 decimal places
            String priceAssString = Double.toString(price);
            int decimalIndex = priceAssString.indexOf(".");
            // Count characters after the decimal point and throw error if more than two found
            int digitsAfterDecimal = priceAssString.length() - decimalIndex - 1;
            if (digitsAfterDecimal > 2) {
                errorAlert.setContentText("Number of digits after decimal greater than 2, please enter a valid price");
                errorAlert.showAndWait();
                return;
            }

            Product newProduct = new Product(0, name, price, stock, min, max);
            // Call the addProduct method from the Inventory class
            Inventory.addProduct(newProduct);
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("Please verify that the values for inventory, price, max, and min are numbers");
            errorAlert.showAndWait();
            return;
        }

        //closes window once product is successfully added
        stage = (Stage) addProductPane.getScene().getWindow();
        System.out.println("Product Added");
        stage.close();
        // Show the main view after closing the "Add Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }

    private Product newProduct = new Product(0, "", 0.0, 0, 0, 0);

    public void onProductAddButtonClicked() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            newProduct.addAssociatedPart(selectedPart);
            productPartTable.setItems(newProduct.getAllAssociatedParts());
            System.out.println(selectedPart.getName() + " added to product's table");
        } else {
            System.out.println("No part selected to add.");
        }
    }

    public void onProductRemoveButtonClicked() {
        Part selectedPart = productPartTable.getSelectionModel().getSelectedItem();
        newProduct.deleteAssociatedPart(selectedPart);
    }

    public void onAddProductExitClicked (){
        stage = (Stage) addProductPane.getScene().getWindow();
        System.out.println("Add product closed");
        stage.close();
        if (mainController != null) {
            mainController.showMainView();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add product Initialized");
        //clear selected items from previous item modification or creation to create empty table upon view initialization
        newProduct.getAllAssociatedParts().clear();
        //method from mainController.java
        Controller.mainController.partTableMethod(partID, partName, partInventory, partCost, partTable);
        // Create a FilteredList and SortedList for the partTable
        var filteredPartList = new FilteredList<>(Inventory.getAllParts(), p -> true);
        // Bind the filtered list to the partSearchTextField text property
        partSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredPartList.setPredicate(part -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            // Compare all part attributes with the search text
            return part.getName().toLowerCase().contains(lowerCaseFilter)
                    || String.valueOf(part.getId()).contains(lowerCaseFilter);
        }));
        // Create a SortedList to display the filtered items in the table
        SortedList<Part> sortedPartList = new SortedList<>(filteredPartList);
        sortedPartList.comparatorProperty().bind(partTable.comparatorProperty());
        // Set the sorted list as the items of the partTable
        partTable.setItems(sortedPartList);
        //method from java main controller
        Controller.mainController.productPartAddMethod(newProduct, partID2, partName2, partInventory2, partCost2, productPartTable);
        productPartTable.setItems(newProduct.getAllAssociatedParts());
    }
}

