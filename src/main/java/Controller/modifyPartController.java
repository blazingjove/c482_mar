package Controller;

import Model.Inventory;
import Model.Part;
import Model.InHouse;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class "modifyPartController" provides logic for modifyPartView.
 */
public class modifyPartController implements Initializable {
    private int selectedIndex;
    /**radio button for inhouse selection.*/
    public RadioButton inHouseRadioButton;
    /**radio button for outsourced selection.*/
    public RadioButton outsourcedRadioButton;
    @FXML
    private ToggleGroup toggleGroup;
    /**modify part view identified.*/
    @FXML
    public AnchorPane modifyPartPane;
    private mainController mainController; // Reference to the main controller
    /**Identifies the main controller*/
    public void setMainControllerRef(mainController mainController) {
        this.mainController = mainController;
    }
    Stage stage;
    /** takes information from selected part in mainController and displays in modifyPart view
     */
    // Other FXML elements
    @FXML private TextField partIdField;
    @FXML private TextField partNameField;
    @FXML private TextField partInventoryField;
    @FXML private TextField partCostField;
    @FXML private TextField partMaxField;
    @FXML private TextField partMinField;
    @FXML private TextField partLastField;
    @FXML private Part selectedPart;

    /** lastLabel used to change test in accordance with user selection of inohuse or outsourced.*/
    public Label lastLabel;
    /** This method will change the text of the in correspondence with the radio buttons
     */
    @FXML
    private void onRadioButtonSelected() {
        if (inHouseRadioButton.isSelected()) {
            lastLabel.setText("Machine ID");
        } else if (outsourcedRadioButton.isSelected()) {
            lastLabel.setText("Company Name");
        }
    }
    /**retains the index of the selected part being modified so that if the part is modified it replaced the information of that part*/
    public void setSelectedPart(Part selectedPart, int selectedIndex) {
        this.selectedPart = selectedPart;
        this.selectedIndex = selectedIndex; // Set the index of the selected part
        // Call a method to display the selected part's data in the fields
        displaySelectedPartData();
    }

    /** takes the information from the selected part in the main view and displays that information in the appropriate modiftpartcontroller field.
     */
    private void displaySelectedPartData() {
        // Set the appropriate label based on the part being in house or outsourced
        if (selectedPart instanceof InHouse) {
            lastLabel.setText("Machine ID");
            partLastField.setText(Integer.toString(((InHouse) selectedPart).getMachineId()));
            inHouseRadioButton.setSelected(true);
        } else if (selectedPart instanceof Outsourced) {
            lastLabel.setText("Company Name");
            partLastField.setText(((Outsourced) selectedPart).getCompanyName());
            outsourcedRadioButton.setSelected(true);
        }
        // Populate the fields with the selected part's data
        partIdField.setText(Integer.toString(selectedPart.getId()));
        partNameField.setText(selectedPart.getName());
        partInventoryField.setText(Integer.toString(selectedPart.getStock()));
        partCostField.setText(Double.toString(selectedPart.getPrice()));
        partMaxField.setText(Integer.toString(selectedPart.getMax()));
        partMinField.setText(Integer.toString(selectedPart.getMin()));

        System.out.println(selectedPart.getId());
    }

    /** takes the information the user selected and saves said information after the information has been parsed for errors*/
    @FXML
    public void onModifyPartSaveClicked() {
        try {
            //error handling for blank sections
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            if (partNameField.getText().isEmpty() || partInventoryField.getText().isEmpty() || partCostField.getText().isEmpty() || partMaxField.getText().isEmpty() || partMinField.getText().isEmpty() || partLastField.getText().isEmpty()) {
                errorAlert.setContentText("Please fill all the fields of the form");
                errorAlert.showAndWait();
                return;
            }
            // parsing data in the window and assigning it to variables for storage
            int partId = selectedPart.getId();
            String name = partNameField.getText();
            double price = Double.parseDouble(partCostField.getText());
            int stock = Integer.parseInt(partInventoryField.getText());
            int min = Integer.parseInt(partMinField.getText());
            int max = Integer.parseInt(partMaxField.getText());
            String lastFieldText = partLastField.getText();

            //making sure the inventory is between max and min, and that min is lower than max
            if (min >= max || stock < 0 || min < 0 || max < 0 || stock > max || stock < min) {
                errorAlert.setContentText("Inventory must be positive and between max and min & max must be greater than min");
                errorAlert.showAndWait();
                return;
            }
            //verifying that the price is positive
            if (price < 0) {
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
            Part modPart;
            if (inHouseRadioButton.isSelected()) {
                try {
                    int machineId = Integer.parseInt(lastFieldText);
                    modPart = new InHouse(partId, name, price, stock, min, max, machineId);
                } catch (Exception e) {
                    errorAlert.setContentText("Please input an Int for machine ID");
                    errorAlert.showAndWait();
                    return;
                }
            } else if (outsourcedRadioButton.isSelected()) {
                modPart = new Outsourced(partId, name, price, stock, min, max, lastFieldText);
            } else {
                // Handle the case where neither radio button is selected
                // show an error message or take appropriate action
                return;
            }
            modPart.setId(partId);
            Inventory.updatePart(selectedIndex, modPart);
            //closes window once part is successfully added
            stage = (Stage) modifyPartPane.getScene().getWindow();
            System.out.println("Part modified");
            stage.close();
        }
        catch (Exception e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("Please verify that the values for inventory, price, max, and min, machine ID are numbers");
            errorAlert.showAndWait();
            return;
        }
        // Show the main view after closing the "Add Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }
        /** closes the modifypart view and shows main view*/
        public void onModifyPartExitClicked(ActionEvent actionEvent) {
        stage = (Stage) modifyPartPane.getScene().getWindow();
        System.out.println("Add part closed");
        stage.close();
        // Show the main view after closing the "modify Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }
    /** most the initialize logic dedicated to the radio buttons on the modify part view*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Toggle group so only 1 radio button is selected at 1 time
        toggleGroup = new ToggleGroup();
        inHouseRadioButton.setToggleGroup(toggleGroup);
        outsourcedRadioButton.setToggleGroup(toggleGroup);
        // Add event listeners for radio buttons
        inHouseRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> onRadioButtonSelected());
        outsourcedRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> onRadioButtonSelected());
        //notifies user when of modify part view being opened
        System.out.println("modify part Initialized");
    }
}

