package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

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
    this.productIndex = productIndex; // Set the index of the selected part
    // Call a method to display the selected part's data in the fields
    displaySelectedProductData();
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
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedPart.getName()+ " added to product's table");
        Product.addAssociatedPart(selectedPart);
        System.out.println(Product.getAllAssociatedParts());
    }

    public void onProductRemoveButtonClicked() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        Product.deleteAssociatedPart(selectedPart);
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
    String name = productNameField.getText();
    double price = Double.parseDouble(productCostField.getText());
    int stock = Integer.parseInt(productInventoryField.getText());
    int min = Integer.parseInt(productMinField.getText());
    int max = Integer.parseInt(productMaxField.getText());

    //update all sections of part with the values in the text-fields
    selectedProduct.setName(name);
    selectedProduct.setPrice(price);
    selectedProduct.setStock(stock);
    selectedProduct.setMin(min);
    selectedProduct.setMax(max);

    // Call the addProduct method from the Inventory class
    Inventory.updateProduct(productIndex, selectedProduct);

    //closes window once product is successfully added
    stage = (Stage) modifyProductPane.getScene().getWindow();
    System.out.println("Product Added");
    stage.close();

    // Show the main view after closing the "Add Part" window
    if (mainController != null) {
        mainController.showMainView();
    }
}

@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    System.out.println("modify product Initialized /n");
    //method from mainController.java
    Controller.mainController.partTableMethod(partID, partName, partInventory, partCost, partTable);
    //clear selected items from previous item modification or creation
    Product.getAllAssociatedParts().clear();
    // Create a FilteredList and SortedList for the partTable
    var filteredPartList = new FilteredList<Part>(Inventory.getAllParts(), p -> true);

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
    Controller.mainController.productPartAddMethod(partID2, partName2, partInventory2, partCost2, productPartTable);
    productPartTable.setItems(Product.getAllAssociatedParts());
}
}