package Controller;

import Model.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller class "mainController" provides logic for the main screen of the app
 * @author Marco Alvarez
 */

public class mainController implements Initializable{
// blow is the logic to exit main view when exit button is hit
@FXML
private AnchorPane mainPane;
Stage stage;

public void onExitClicked() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
    alert.setTitle("Exit");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        System.exit(0);
    }
}

//variables for parts
@FXML private TableView<Part> partTable;
@FXML private TableColumn<Part, Integer> partID;
@FXML private TableColumn<Part, Integer>  partName;
@FXML private TableColumn<Part, Integer>  partInventory;
@FXML private TableColumn<Part, Integer>  partCost;
@FXML private TableView<Product> productTable;
@FXML private TableColumn<Product, Integer>  productID;
@FXML private TableColumn<Product, Integer>  productName;
@FXML private TableColumn<Product, Integer>  productInventory;
@FXML private TableColumn<Product, Integer>  productCost;

    //add Part button opens add part window
public void onPartAdd() {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/c482/views/addPartView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        // Pass the mainController reference to addPartController
        addPartController addPartController = fxmlLoader.getController();
        addPartController.setMainControllerRef(this);

        // Hide the main view
        mainPane.getScene().getWindow().hide();

        stage.show();
    } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "part add error detected");
        alert.setTitle("part Add Error");

    }
}

// Method to show the main view
public void showMainView() {
    Stage stage = (Stage) mainPane.getScene().getWindow();
    stage.show();
}

// modify part button opens modify part window and closes main temp


@FXML
public void onPartModify() {
    Part selectedPart = partTable.getSelectionModel().getSelectedItem();
    int selectedIndex = partTable.getSelectionModel().getSelectedIndex();
    if (selectedPart != null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482/views/modifyPartView.fxml"));
            Parent root = loader.load();

            //pulls the data of the item that is selected
            modifyPartController modifyPartController = loader.getController();
            modifyPartController.setMainControllerRef(this);
            modifyPartController.setSelectedPart(selectedPart, selectedIndex);

            //open modify part scene
            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            System.out.println("index is:" + selectedIndex);
            stage.setScene(new Scene(root));

            //show main when part mod closed
            mainPane.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }else{
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setContentText("Please select part to modify");
        errorAlert.showAndWait();
    }
}

// onPartDelete button set to delete data selected
@FXML
public void onPartDelete(){
    Part selectedPart = partTable.getSelectionModel().getSelectedItem();

    if (selectedPart == null) {
        System.out.println("No part selected");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setContentText("Please select part to delete");
        errorAlert.showAndWait();
    } else {
        //alert will prompt user to confirm or cancel part deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
        alert.setTitle("Delete Part");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }
}

// onProductDelete button will delete selected product
@FXML
public void onProductDelete(){
    //alert will prompt user to confirm or cancel product deletion
    Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
    if (selectedProduct == null) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setContentText("Please select product to delete");
        errorAlert.showAndWait();
    }
    else{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
        alert.setTitle("Delete Product");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("test 1");
            Inventory.deleteProduct(selectedProduct);
        }
    }
}

//opens product add view and closes main temporarily
@FXML
public void onProductAdd(){
    try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/c482/views/addProductView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        // Pass the mainController reference to addPartController
        addProductController addProductController = fxmlLoader.getController();
        addProductController.setMainControllerRef(this);
        //hide the main view
        mainPane.getScene().getWindow().hide();
        // Show the add product view
        stage.show();
    }         catch (IOException e) {
        e.printStackTrace();
    }
}

@FXML
public void onProductModify(){
    Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
    int productIndex = productTable.getSelectionModel().getSelectedIndex();
    if (selectedProduct != null) {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/c482/views/modifyProductView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        // Pass the mainController reference to addProductcontroller

        modifyProductController modifyProductController = fxmlLoader.getController();
        modifyProductController.setMainControllerRef(this);
        modifyProductController.setSelectedProduct(selectedProduct, productIndex);
        //hide the main view
        mainPane.getScene().getWindow().hide();
        //show on product mod view
        stage.show();
    } catch (IOException e){
        e.printStackTrace();
    }} else{Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("Please select product to modify");
            errorAlert.showAndWait();}
    }

//Listener for part text field search
//calling FXML elements
@FXML
private TextField partSearchTextField;
@FXML
private TextField productSearchTextField;
//initialized command when code is ran
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    partTableMethod(partID, partName, partInventory, partCost, partTable);
    productID.setCellValueFactory(new PropertyValueFactory<>("id"));
    productName.setCellValueFactory(new PropertyValueFactory<>("name"));
    productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
    productCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    productTable.setItems(Inventory.getAllProducts());
    // Create a FilteredList and SortedList for the partTable
    FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), p -> true);
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
/*
 same as  above to filter product text area input in product table
*/
    // Create a FilteredList and SortedList for the ProductTable
    FilteredList<Product> filteredProductList = new FilteredList<>(Inventory.getAllProducts(), p -> true);
    // Bind the filtered list to the productSearchTextField text property
    productSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredProductList.setPredicate(product -> {
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        return product.getName().toLowerCase().contains(lowerCaseFilter)
                || String.valueOf(product.getId()).contains(lowerCaseFilter);
    }));
// Create a SortedList to display the filtered items in the table
    SortedList<Product> sortedProductList = new SortedList<>(filteredProductList);
    sortedProductList.comparatorProperty().bind(productTable.comparatorProperty());
// Set the sorted list as the items of the product table
    productTable.setItems(sortedProductList);
    System.out.println("program started");
}

public static void partTableMethod(TableColumn<Part, Integer> partID, TableColumn<Part, Integer> partName, TableColumn<Part, Integer> partInventory, TableColumn<Part, Integer> partCost, TableView<Part> partTable) {
    partID.setCellValueFactory(new PropertyValueFactory<>("id"));
    partName.setCellValueFactory(new PropertyValueFactory<>("name"));
    partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
    partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    partTable.setItems(Inventory.getAllParts());
}

public static void  productPartAddMethod(Product product, TableColumn<Part, Integer> partID2, TableColumn<Part, Integer> partName2, TableColumn<Part, Integer> partInventory2, TableColumn<Part, Integer> partCost2, TableView<Part> productPartTable) {
    partID2.setCellValueFactory(new PropertyValueFactory<>("id"));
    partName2.setCellValueFactory(new PropertyValueFactory<>("name"));
    partInventory2.setCellValueFactory(new PropertyValueFactory<>("stock"));
    partCost2.setCellValueFactory(new PropertyValueFactory<>("price"));
    productPartTable.setItems(product.getAllAssociatedParts());
}
}