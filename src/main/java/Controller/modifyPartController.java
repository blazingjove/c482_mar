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

public class modifyPartController implements Initializable {
    private int selectedIndex;
    public RadioButton inHouseRadioButton;
    public RadioButton outsourcedRadioButton;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    public AnchorPane modifyPartPane;
    private mainController mainController; // Reference to the main controller

    public void setMainControllerRef(mainController mainController) {
        this.mainController = mainController;
    }
    Stage stage;

    /** takes information from selected part in mainController and displays in modifyPart view
     */

    // Other FXML elements
    @FXML
    private TextField partIdField;
    @FXML
    private TextField partNameField;
    @FXML
    private TextField partInventoryField;
    @FXML
    private TextField partCostField;
    @FXML
    private TextField partMaxField;
    @FXML
    private TextField partMinField;
    @FXML
    private TextField partLastField;
    @FXML
    private Part selectedPart;

    /** This method will change the text of the in correspondence with the radio buttons
     */
    public Label lastLabel;
    @FXML
    private void onRadioButtonSelected() {
        if (inHouseRadioButton.isSelected()) {
            lastLabel.setText("Machine ID");
        } else if (outsourcedRadioButton.isSelected()) {
            lastLabel.setText("Company Name");
        }
    }
    public void setSelectedPart(Part selectedPart, int selectedIndex) {
        this.selectedPart = selectedPart;
        this.selectedIndex = selectedIndex; // Set the index of the selected part
        // Call a method to display the selected part's data in the fields
        displaySelectedPartData();
    }

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


    @FXML
    public void onModifyPartSaveClicked() {

        //error handling for blank sections
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        if( partNameField.getText().isEmpty() || partInventoryField.getText().isEmpty() || partCostField.getText().isEmpty() || partMaxField.getText().isEmpty() || partMinField.getText().isEmpty() || partLastField.getText().isEmpty()) {
            errorAlert.setContentText("Please fill all the fields of the form");
            errorAlert.showAndWait();
            return;
        }

        int partId = selectedPart.getId();
        String name = partNameField.getText();
        double price = Double.parseDouble(partCostField.getText());
        int stock = Integer.parseInt(partInventoryField.getText());
        int min = Integer.parseInt(partMinField.getText());
        int max = Integer.parseInt(partMaxField.getText());
        String lastFieldText = partLastField.getText();

        // Create an instance of the appropriate subclass based on the selected radio button
        Part modPart;

        if (inHouseRadioButton.isSelected()) {
            int machineId = Integer.parseInt(lastFieldText);
            modPart = new InHouse(partId, name, price, stock, min, max, machineId);
        } else if (outsourcedRadioButton.isSelected()) {
            modPart = new Outsourced(partId, name, price, stock, min, max, lastFieldText);
        } else {
            // Handle the case where neither radio button is selected
            // show an error message or take appropriate action
            return;
        }

        modPart.setId(partId);
        Inventory.updatePart(selectedIndex ,modPart);

        //closes window once part is successfully added
        stage = (Stage) modifyPartPane.getScene().getWindow();
        System.out.println("Part modified");
        stage.close();

        // Show the main view after closing the "Add Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }

    // when add part window closed main view will be displayed
    public void onModifyPartExitClicked(ActionEvent actionEvent) {
        stage = (Stage) modifyPartPane.getScene().getWindow();
        System.out.println("Add part closed");
        stage.close();

        // Show the main view after closing the "modify Part" window
        if (mainController != null) {
            mainController.showMainView();
        }
    }

    //save data and pass to main controller to display on table

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

