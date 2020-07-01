package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {
    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private TextField addNameField;

    @FXML
    private TextField addAddressField;

    @FXML
    private TextField phoneNumberAddress;

    @FXML
    private TextField postalCodeField;
@FXML
private TextField address2ModifyTextField;
    @FXML
    private Button addButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private TextField addressModifyTextField;
    @FXML
    private TextField phoneNumberModifyTextField;

    @FXML
    private TextField postalCodeModifyField;
    @FXML
    private TextField nameModifyTextField;
    @FXML
    private ComboBox<String> cityModifyComboBox;
    @FXML
    private ComboBox<String> modifyComboBox;

    @FXML
    private TextField address2TextField;



    private static ObservableList<String> cities = FXCollections.observableArrayList(
            "Phoenix","New York", "London");

    public static ObservableList<String> getCities(){
        return cities;
    }
   @FXML
   private void addCustomer() throws SQLException, ClassNotFoundException {
       String name = addNameField.getText();
       String address = addAddressField.getText();
       int cityID = cityComboBox.getSelectionModel().getSelectedIndex() + 1;
       String phoneNumber = phoneNumberAddress.getText();
       String postalCode = postalCodeField.getText();
       String address2 = address2TextField.getText();
       cityModifyComboBox.getSelectionModel().getSelectedIndex();
       if (name.isEmpty() || address.isEmpty() ||phoneNumber.isEmpty() || postalCode.isEmpty()){
           Alert a = new Alert(Alert.AlertType.ERROR);
           a.setContentText("Invalid fields");
           a.show();
       }
       else{
           Database.addCustomer(name, address, address2, cityID, postalCode, phoneNumber, "Test");
           customerComboBox.setItems(Database.getAllCustomersNames());
           modifyComboBox.setItems(Database.getAllCustomersNames());
           Alert a = new Alert(Alert.AlertType.CONFIRMATION);
           a.setContentText("Customer has been added");
           a.show();
       }
   }



   @FXML
   private void deleteCustomer() throws SQLException, ClassNotFoundException {
        String name = customerComboBox.getSelectionModel().getSelectedItem();
       String name1 = name.substring(name.length() - 2);
       name1 = name1.replaceAll("\\D", "");

        int id = Integer.parseInt(name1);
       System.out.println(id);
           Database.deleteCustomer(id);
       customerComboBox.setItems(Database.getAllCustomersNames());
       modifyComboBox.setItems(Database.getAllCustomersNames());
       Alert a = new Alert(Alert.AlertType.CONFIRMATION);
       a.setContentText("Customer has been deleted");
       a.show();
   }

   @FXML
   private void modifyCustomer() throws SQLException, ClassNotFoundException {
      String customerName = modifyComboBox.getSelectionModel().getSelectedItem();
       String customerName1 = customerName.substring(customerName.length() - 2);
       customerName1 = customerName1.replaceAll("\\D", "");
       int id = Integer.parseInt(customerName1);
      String name = nameModifyTextField.getText();
      String address = addressModifyTextField.getText();
      int cityId = cityModifyComboBox.getSelectionModel().getSelectedIndex()+1;
      String phoneNumber = phoneNumberModifyTextField.getText();
      String postal = postalCodeModifyField.getText();
      String address2 = address2ModifyTextField.getText();
       if (name.isEmpty() || address.isEmpty() ||phoneNumber.isEmpty() || postal.isEmpty()){
           Alert a = new Alert(Alert.AlertType.ERROR);
           a.setContentText("Invalid fields");
           a.show();
       }
       else {
           Database.modifyCustomer(id, name, address, cityId, phoneNumber, postal, address2);
           modifyComboBox.setItems(Database.getAllCustomersNames());
           customerComboBox.setItems(Database.getAllCustomersNames());

           Alert a = new Alert(Alert.AlertType.CONFIRMATION);
           a.setContentText("Customer has been modified");
           a.show();
       }
   }

   @FXML
   private void appointmentScreen(ActionEvent event) throws IOException {
       Parent mainScreen = FXMLLoader.load(getClass().getResource("appointments.fxml"));
       Scene scene = new Scene(mainScreen);
       Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       appStage.setScene(scene);
       appStage.show();
   }

    @FXML
    private void switchReports(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("reports.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    @FXML
    private void populateCustomerModify() throws SQLException {
        if (modifyComboBox.getSelectionModel().getSelectedItem() != null) {
            String name = modifyComboBox.getSelectionModel().getSelectedItem();
            String name1 = name.substring(name.length() - 2);
            int custId = Integer.parseInt(name1.replaceAll("\\D", "").replaceAll("\\D", ""));
            for (Customer customer : Database.getAllCustomers()){
                if (customer.getId() == custId) {
                    addressModifyTextField.setText(customer.getAddress());
                    address2ModifyTextField.setText(customer.getAddress2());
                    cityModifyComboBox.setValue(customer.getCity());
                    postalCodeModifyField.setText(customer.getZipcode());
                    phoneNumberModifyTextField.setText(customer.getPhoneNumber());
                    nameModifyTextField.setText(customer.getName());
                }
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

      customerComboBox.setItems(Database.getAllCustomersNames());
        modifyComboBox.setItems(Database.getAllCustomersNames());

        cityComboBox.setItems(cities);
        cityModifyComboBox.setItems(cities);

    }
}

