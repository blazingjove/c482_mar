package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class "modifyProductController" provides logic for modifyProductView */
public class modifyProductController implements Initializable {
@FXML
public AnchorPane modifyProductPane;
private Controller.mainController mainController; // Reference to the main controller
//text fields for user inputs
@FXML private TextField productIdField;
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
@FXML private Product selectedProduct;
@FXML TableView<Part> productPartTable;
@FXML private TableColumn<Part, Integer> partID2;
@FXML private TableColumn<Part, Integer>  partName2;
@FXML private TableColumn<Part, Integer>  partInventory2;
@FXML private TableColumn<Part, Integer>  partCost2;
private int productIndex;

/** sets the main view*/
public void setMainControllerRef(mainController mainController) {
    this.mainController = mainController;
}
Stage stage;
    /** identifies product and product index, populates the part table as well*/
    public void setSelectedProduct(Product selectedProduct, int productIndex) {
        this.selectedProduct = selectedProduct;
        this.productIndex = productIndex; // Set the index of the selected product
        displaySelectedProductData();

        // Set cell value factories for the productPartTable
        partID2.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost2.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Populate the table with associated parts
        productPartTable.setItems(selectedProduct.getAllAssociatedParts());
    }

/** displays the selected product data*/
private void displaySelectedProductData() {
    // Populate the fields with the selected product data

    productIdField.setText(Integer.toString(selectedProduct.getId()));
    productNameField.setText(selectedProduct.getName());
    productInventoryField.setText(Integer.toString(selectedProduct.getStock()));
    productCostField.setText(Double.toString(selectedProduct.getPrice()));
    productMaxField.setText(Integer.toString(selectedProduct.getMax()));
    productMinField.setText(Integer.toString(selectedProduct.getMin()));

    System.out.println(selectedProduct.getId());

}
    /**adds selected part to associated part table, if not part selected user is alerted.*/
    public void onProductAddButtonClicked() {
        try {
            Part selectedPart = partTable.getSelectionModel().getSelectedItem();
            System.out.println(selectedPart.getName() + " added to product's table");
            selectedProduct.addAssociatedPart(selectedPart);
            System.out.println(selectedProduct.getAllAssociatedParts());
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("Please select a part to add.");
            errorAlert.showAndWait();
        }
    }

    /**logic to removed associated part table, user is prompted to follow through with the command and if not part is selcted user
     * is alerted.*/
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
                selectedProduct.deleteAssociatedPart(selectedPart);
                productPartTable.setItems(selectedProduct.getAllAssociatedParts());
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


    /** closes the modiftPorduct view and opens the main view*/
    public void modifyProductExitClicked (ActionEvent actionEvent){
    stage = (Stage) modifyProductPane.getScene().getWindow();
    System.out.println("modify product closed");
    stage.close();
    // Show the main view after closing the "Add Part" window
    if (mainController != null) {
        mainController.showMainView();
    }
}

    /** On button click the inputs are validated before being saved, user is alerted if any discrepancies are detected.*/
    @FXML
    public void onModifySaveButtonClicked(ActionEvent actionEvent) {
        try {
            // Parse input fields
            String name = productNameField.getText();
            double price = Double.parseDouble(productCostField.getText());
            int stock = Integer.parseInt(productInventoryField.getText());
            int min = Integer.parseInt(productMinField.getText());
            int max = Integer.parseInt(productMaxField.getText());

            // Validate inputs
            if (min >= max || stock < min || stock > max) {
                throw new IllegalArgumentException("Inventory must be between min and max, and min must be less than max.");
            }

            // Update the selectedProduct with new values
            selectedProduct.setName(name);
            selectedProduct.setPrice(price);
            selectedProduct.setStock(stock);
            selectedProduct.setMin(min);
            selectedProduct.setMax(max);

            // Call the updateProduct method to replace the existing product in the inventory
            Inventory.updateProduct(productIndex, selectedProduct);

            // Close the modify product window
            stage = (Stage) modifyProductPane.getScene().getWindow();
            System.out.println("Product updated: " + selectedProduct.getName());
            stage.close();

            if (mainController != null) {
                mainController.showMainView();
            }

        } catch (IllegalArgumentException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Please verify that all fields are filled correctly.");
            errorAlert.showAndWait();
        }
    }

    /**more or lesss the same as the add product view, initializing the tables and the search function.*/
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    System.out.println("modify product Initialized");

    // Set up partTable filtering
    Controller.mainController.partTableMethod(partID, partName, partInventory, partCost, partTable);

    //same as addPartController search
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
}
}