package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class "addProductController" provides logic for addProductView */
public class addProductController implements Initializable {
    @FXML
    public AnchorPane addProductPane;
    private mainController mainController; // Reference to the main controller
    /**identifies the main controller in the view*/
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

    private Product newProduct = new Product(0, "", 0.0, 0, 0, 0);

    /** Validates user field inputs for errors and passes data into a new product object to be saved and added in the product list
     * <p>
     *     <b>Future Improvements</b></n>
     *     I would like to make the associated part table to combine like items for example if i have two wheels
     *     it will not take two lines but one line that shows the value two under a column called "no. of items" or something of the like
     * </p>*/
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

            // set values of new product to the user input values
            newProduct.setName(name);
            newProduct.setPrice(price);
            newProduct.setStock(stock);
            newProduct.setMin(min);
            newProduct.setMax(max);

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

        stage = (Stage) addProductPane.getScene().getWindow();
        System.out.println("Product Added");
        stage.close();
        // Show the main view after closing the "Add Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }

    /**Adds selected part from the parts table to the associated parts table, shows error if no part is selected*/
    public void onProductAddButtonClicked() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            newProduct.addAssociatedPart(selectedPart);
            productPartTable.setItems(newProduct.getAllAssociatedParts());
            System.out.println(selectedPart.getName() + " added to product's table");
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("No part selected.");
            errorAlert.showAndWait();
        }
    }

    /** removes associated part after user is prompted, if no part is selected user is notified through alert.*/
    @FXML
    public void onProductRemoveButtonClicked() {
        // Get the selected part from the associated parts table
        Part selectedPart = productPartTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            // Show confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Remove Associated Part");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to remove this associated part?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove the part if the user confirms
                newProduct.deleteAssociatedPart(selectedPart);
                productPartTable.setItems(newProduct.getAllAssociatedParts());
                System.out.println("Associated part removed: " + selectedPart.getName());
            }
        } else {
            // Show an error alert if no part is selected
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("No Part Selected");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please select a part to remove.");
            errorAlert.showAndWait();
        }
    }

    /**Closes the addProduct view and opens the main view*/
    public void onAddProductExitClicked (){
        stage = (Stage) addProductPane.getScene().getWindow();
        System.out.println("Add product closed");
        stage.close();
        if (mainController != null) {
            mainController.showMainView();
        }
    }

    /** Initialize code mainly used to set up the part tables and table search function.
     * <p>
     *     <b> Runtime Error</b></n>
     *     I had a problem when creating or modifying an existing product the associated parts from a previous product would persist on the view.
     *     The problem was that after the product was saved the newProduct variable I used to hold the information and add to the new product list was
     *     retaining the associated parts even though it did not hold another information so I manually added a command to clear associated parts in the
     *     initialize method so upon opening either of the product views the associated parts table would be empty.
     * </p>*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add product Initialized");
        //clear selected items from previous item modification or creation to create empty table upon view initialization
        newProduct.getAllAssociatedParts().clear();
        //method from mainController.java
        Controller.mainController.partTableMethod(partID, partName, partInventory, partCost, partTable);

        // part search for table, execute search on enter
        partSearchTextField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                String searchText = partSearchTextField.getText().toLowerCase();

                // Create a FilteredList based on the search query
                FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), part -> {
                    if (searchText == null || searchText.isEmpty()) {
                        return true; // Show all parts if the search query is empty
                    }
                    return part.getName().toLowerCase().contains(searchText)
                            || String.valueOf(part.getId()).contains(searchText);
                });

                // Update the part table with the filtered list
                SortedList<Part> sortedPartList = new SortedList<>(filteredPartList);
                sortedPartList.comparatorProperty().bind(partTable.comparatorProperty());
                partTable.setItems(sortedPartList);

                // Show an alert if no matches are found
                if (filteredPartList.isEmpty() && !searchText.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Matches Found");
                    alert.setHeaderText(null);
                    alert.setContentText("No parts match your search query.");
                    alert.showAndWait();
                }
            }
        });

        //method from java main controller
        Controller.mainController.productPartAddMethod(newProduct, partID2, partName2, partInventory2, partCost2, productPartTable);
        productPartTable.setItems(newProduct.getAllAssociatedParts());
    }
}

