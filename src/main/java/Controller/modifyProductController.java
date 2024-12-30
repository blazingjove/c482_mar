package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
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


public void setMainControllerRef(mainController mainController) {
    this.mainController = mainController;
}
Stage stage;

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
    //initial value if a new part is assigned to product it will begin at index 1 and increment by one for every item added
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
    public void onProductRemoveButtonClicked() {
        Part selectedPart = productPartTable.getSelectionModel().getSelectedItem();
        selectedProduct.deleteAssociatedPart(selectedPart);
    }

//code closes the modify part view and opens main when clicked
public void modifyProductExitClicked (ActionEvent actionEvent){
    stage = (Stage) modifyProductPane.getScene().getWindow();
    System.out.println("modify product closed");
    stage.close();
    // Show the main view after closing the "Add Part" window
    if (mainController != null) {
        mainController.showMainView();
    }
}

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


@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    System.out.println("modify product Initialized");

    // Set up partTable filtering
    Controller.mainController.partTableMethod(partID, partName, partInventory, partCost, partTable);
    var filteredPartList = new FilteredList<>(Inventory.getAllParts(), p -> true);

    partSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredPartList.setPredicate(part -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return part.getName().toLowerCase().contains(lowerCaseFilter)
                    || String.valueOf(part.getId()).contains(lowerCaseFilter);
        });
    });

    SortedList<Part> sortedPartList = new SortedList<>(filteredPartList);
    sortedPartList.comparatorProperty().bind(partTable.comparatorProperty());
    partTable.setItems(sortedPartList);
}
}