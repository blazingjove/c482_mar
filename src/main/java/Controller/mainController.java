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

/** Controller class "mainController" provides logic for the main view of the application. */
public class mainController implements Initializable {
    // blow is the logic to exit main view when exit button is hit
    @FXML
    private AnchorPane mainPane;
    Stage stage;

    /**
     * OnExitClicked closes the main view.
     * Prompts User to confirm before closing window through an alert.
     */
    public void onExitClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        alert.setTitle("Exit");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    //variables for parts
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partID;
    @FXML
    private TableColumn<Part, Integer> partName;
    @FXML
    private TableColumn<Part, Integer> partInventory;
    @FXML
    private TableColumn<Part, Integer> partCost;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productID;
    @FXML
    private TableColumn<Product, Integer> productName;
    @FXML
    private TableColumn<Product, Integer> productInventory;
    @FXML
    private TableColumn<Product, Integer> productCost;

    /**
     * 0nPartAdd opens the addPartView.
     * Hides main view if no error detected.
     * throws error if addPartView can not be opened. */
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

    /**
     * showMainView opens and refreshes the main view.
     * used by other views to open the main view after closing said view. */
    public void showMainView() {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.show();
        productTable.refresh();
    }

    /**
     * onPartModify opens the modifyPartView.
     * Prompts user to select a view if none is selected at the time of pressing the modify part button. Hides main view if no error detected.
     */
    @FXML
    public void onPartModify() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        int selectedIndex = Inventory.getAllParts().indexOf(selectedPart);
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
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("Please select part to modify");
            errorAlert.showAndWait();
        }
    }

    /**
     * onPartDelete deletes the selected part.
     * If no part is selected alert is shown that alerts user that no part is selected.
     * will also prompt user to confirm the removal of selected part if part is chosen.
     */
    @FXML
    public void onPartDelete() {
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

    /**
     * onProductDelete deletes the selected part.
     * If no part is selected alert is shown that alerts user that no part is selected.
     * will also prompt user to confirm the removal of selected part if part is chosen.
     */
    @FXML
    public void onProductDelete() {
        // Get the selected product
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            // Alert if no product is selected
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please select a product to delete.");
            errorAlert.showAndWait();
            return;
        }

        // Check if the product has associated parts
        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            // Alert if the product has associated parts
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Cannot Delete Product");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("This product has associated parts and cannot be deleted. Remove the associated parts first.");
            errorAlert.showAndWait();
            return;
        }

        // Confirmation alert before deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Product");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the product
            Inventory.deleteProduct(selectedProduct);
            System.out.println("Product deleted: " + selectedProduct.getName());
        }
    }


    /**
     * Opens the addProduct view and closes main view.
     * Hides main view if no error detected.
     */
    @FXML
    public void onProductAdd() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens modifyProduct view and closes the main view.
     * will alert user that no product is selected.
     * Hides main view if no error detected.
     */
    @FXML
    public void onProductModify() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        int productIndex = Inventory.getAllProducts().indexOf(selectedProduct);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("Please select product to modify");
            errorAlert.showAndWait();
        }
    }

    //Listener for part text field search
//calling FXML elements
    @FXML
    private TextField partSearchTextField;
    @FXML
    private TextField productSearchTextField;
//initialized command when code is ran

    /**
     * creates and populates part and product table with a search bar.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTableMethod(partID, partName, partInventory, partCost, partTable);

        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTable.setItems(Inventory.getAllProducts());

        // Bind the filtered list to the partSearchTextField text property
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

                // Update the table with the filtered list
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

/*
 same as  above to filter product text area input in product table
*/
        // Bind the filtered list to the productSearchTextField text property
        productSearchTextField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                String searchText = productSearchTextField.getText().toLowerCase();

                // Create a FilteredList based on the search query
                FilteredList<Product> filteredProductList = new FilteredList<>(Inventory.getAllProducts(), product -> {
                    if (searchText == null || searchText.isEmpty()) {
                        return true; // Show all products if the search query is empty
                    }
                    return product.getName().toLowerCase().contains(searchText)
                            || String.valueOf(product.getId()).contains(searchText);
                });

                // Update the table with the filtered list
                SortedList<Product> sortedProductList = new SortedList<>(filteredProductList);
                sortedProductList.comparatorProperty().bind(productTable.comparatorProperty());
                productTable.setItems(sortedProductList);

                // Show an alert if no matches are found
                if (filteredProductList.isEmpty() && !searchText.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Matches Found");
                    alert.setHeaderText(null);
                    alert.setContentText("No products match your search query.");
                    alert.showAndWait();
                }
            }
        });


        System.out.println("program started");
    }

    /**
     * partTableMethod creates and populates the part table in the main view.
     */
    public static void partTableMethod(TableColumn<Part, Integer> partID, TableColumn<Part, Integer> partName, TableColumn<Part, Integer> partInventory, TableColumn<Part, Integer> partCost, TableView<Part> partTable) {
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(Inventory.getAllParts());
    }

    /**
     * productPartAddMethod  creates and populates the product associated parts table.
     * @param product is the product
     */
    public static void productPartAddMethod(Product product, TableColumn<Part, Integer> partID2, TableColumn<Part, Integer> partName2, TableColumn<Part, Integer> partInventory2, TableColumn<Part, Integer> partCost2, TableView<Part> productPartTable) {
        partID2.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost2.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPartTable.setItems(product.getAllAssociatedParts());
    }
}