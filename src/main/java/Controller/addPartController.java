package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class addPartController implements Initializable {
    @FXML
    public AnchorPane addPartPane;
    private mainController mainController; // Reference to the main controller
    //FXML for toggleGroup so that one radio button  selected at a time
    @FXML
    private ToggleGroup toggleGroup;
    public RadioButton inHouseRadioButton;
    public RadioButton outsourcedRadioButton;

    public void setMainControllerRef(mainController mainController) {
        this.mainController = mainController;
    }
    Stage stage;
    //text fields for user input
    @FXML private TextField partNameField;
    @FXML private TextField partInventoryField;
    @FXML private TextField partCostField;
    @FXML private TextField partMaxField;
    @FXML private TextField partMinField;
    @FXML private TextField partLastField;
    public Label lastLabel;

    // This method will change the text of the in correspondence with the radio buttons
    @FXML
    private void onRadioButtonSelected() {
        if (inHouseRadioButton.isSelected()) {
            lastLabel.setText("Machine ID");
        } else if (outsourcedRadioButton.isSelected()) {
            lastLabel.setText("Company Name");
        }
    }

    //this method will save the data and make the appropriate item to be saved in the inventory
    @FXML
    public void onPartSaveButtonClicked() {
        try {
            //error handling for blank sections
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            if (partNameField.getText().isEmpty() || partInventoryField.getText().isEmpty() || partCostField.getText().isEmpty() || partMaxField.getText().isEmpty() || partMinField.getText().isEmpty() || partLastField.getText().isEmpty()) {
                errorAlert.setContentText("Please fill all the fields of the form");
                errorAlert.showAndWait();
                return;
            }
            String name = partNameField.getText();
            double price = Double.parseDouble(partCostField.getText());
            int stock = Integer.parseInt(partInventoryField.getText());
            int min = Integer.parseInt(partMinField.getText());
            int max = Integer.parseInt(partMaxField.getText());
            String lastFieldText = partLastField.getText();
            //error handling making sure correct data is input into the form
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
            // Create an instance of the appropriate subclass based on the selected radio button
            Part newPart;
            if (inHouseRadioButton.isSelected()) {
                try {
                    int machineId = Integer.parseInt(lastFieldText);
                    newPart = new InHouse(0, name, price, stock, min, max, machineId);
                } catch (Exception e) {
                    errorAlert.setContentText("Please input an Int for machine ID");
                    errorAlert.showAndWait();
                    return;
                }
            } else {
                newPart = new Outsourced(0, name, price, stock, min, max, lastFieldText);
            }
            // Call the addPart method from the Inventory class
            Inventory.addPart(newPart);
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("Please verify that the values for inventory, price, max, and min are numbers");
            errorAlert.showAndWait();
            return;
        }

        //closes window once part is successfully added
        stage = (Stage) addPartPane.getScene().getWindow();
        System.out.println("Part Added");
        stage.close();
        // Show the main view after closing the "Add Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }

    // when add part window closed main view will be displayed
    @FXML
    public void onAddPartExitClicked() {
        stage = (Stage) addPartPane.getScene().getWindow();
        System.out.println("Add part closed");
        stage.close();
        // Show the main view after closing the "Add Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Toggle group so only 1 radio button is selected at 1 time
        toggleGroup = new ToggleGroup();
        inHouseRadioButton.setToggleGroup(toggleGroup);
        outsourcedRadioButton.setToggleGroup(toggleGroup);
        // Select the "In-House" radio button by default
        inHouseRadioButton.setSelected(true);
        // Add event listeners for radio buttons
        inHouseRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> onRadioButtonSelected());
        outsourcedRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> onRadioButtonSelected());

        System.out.println("Add part Initialized");
    }
}