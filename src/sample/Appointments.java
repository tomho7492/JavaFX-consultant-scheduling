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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class Appointments implements Initializable {

    private ObservableList<String> timeOptions = FXCollections.observableArrayList();
    private ObservableList<String> types =  FXCollections.observableArrayList(
            "Client Registration",
            "Customer Request",
            "Follow Up",
            "Expedited");

    private DateTimeFormatter time1 = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    @FXML
    private ComboBox<String> modifyAppointmentComboBox;
    @FXML
    private ComboBox<String> appointmentTypeComboBox;
    @FXML
    private TextField contactTextField;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private ComboBox<String> endTimeComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField addAppointmentURL;
    @FXML
    private TextField addAppointmentDescription;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField addAppointmentLocation;
    @FXML
    private ComboBox<String> appointmentTypeComboBox1;
    @FXML
    private ComboBox<String> addCustomerIdComboBox;
    @FXML
    private TextField contactTextField1;
    @FXML
    private DatePicker datePicker1;
    @FXML
    private ComboBox<String> startTimeComboBox1;
    @FXML
    private ComboBox<String> deleteAppointmentComboBox;
    @FXML
    private ComboBox<String> endTimeComboBox1;
    @FXML
    private TextField addAppointmentDescription1;
    @FXML
    private TextField addAppointmentURL1;
    @FXML
    private TextField addAppointmentLocation1;
    @FXML
    private TextField titleTextField1;


    //add appointment
    public static LocalDateTime startDateTime;
    public static LocalDateTime endDateTime;
    //modify appointment startDateTime
    public static LocalDateTime startDateTime1;
    public static LocalDateTime endDateTime1;

    //check if two time periods are overlapping
    public boolean isOverlappingAppointment(LocalTime start2, LocalTime end2, LocalDate date2) throws SQLException {

        //Uses Java streams and lambdas (predicate). Using lambdas means it doesn't create anonoymous inner classes anymore
        return Database.getAllAppointments().stream().anyMatch(appointment -> {
            LocalTime start1 = appointment.getAptStartConverted().toLocalTime();
            LocalTime end1 = appointment.getAptEndConverted().toLocalTime();
            LocalDate date1 = appointment.getAptStartConverted().toLocalDate();
            return start1.isBefore(end2) && start2.isBefore(end1) && date1.compareTo(date2) == 0;
        });
        }



    public void addAppointment() throws SQLException, ClassNotFoundException {

        //retrieve user input
        LocalDate date = datePicker.getValue();
        String startTime1 = startTimeComboBox.getSelectionModel().getSelectedItem();
        String endTime1 = endTimeComboBox.getSelectionModel().getSelectedItem();
       String name =  addCustomerIdComboBox.getSelectionModel().getSelectedItem();
        String type = appointmentTypeComboBox.getSelectionModel().getSelectedItem();
        int id = Integer.parseInt(name.replaceAll("\\D", ""));
        String title = titleTextField.getText();
        String description = addAppointmentDescription.getText();
        String location = addAppointmentLocation.getText();
        String contact = contactTextField.getText();
        String url = addAppointmentURL.getText();


        DateTimeFormatter parser = DateTimeFormatter.ofPattern("h:mm a");
      LocalTime startTime = LocalTime.parse(startTime1, parser);
        LocalTime endTime = LocalTime.parse(endTime1, parser);
        startDateTime = LocalDateTime.of(date, startTime);
        endDateTime = LocalDateTime.of(date, endTime);
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zdtUTC = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtUTC2 = zdtUTC.withZoneSameInstant(zoneId);

        ZonedDateTime zdtUTC3 = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtUTC4 = zdtUTC3.withZoneSameInstant(zoneId);

        startDateTime = zdtUTC2.toLocalDateTime();
        endDateTime = zdtUTC4.toLocalDateTime();
        if  (isOverlappingAppointment(startTime, endTime, date)){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Appointment can not be added due to overlapping appointment");
                a.show();
            }
        else if (startTime1.isEmpty() || endTime1.isEmpty() || name.isEmpty() || type.isEmpty() || title.isEmpty() || description.isEmpty() || location.isEmpty() || contact.isEmpty() || url.isEmpty())
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Empty Fields");
            a.show();
        }
          else {
              Database.addAppointment(id, title, description, location, contact, type, url);
              System.out.println("appt added");
            System.out.println(zoneId);
            System.out.println(zdtUTC2);
            System.out.println(zdtUTC2.toLocalDateTime());

              modifyAppointmentComboBox.setItems(Database.getAllAppointmentTitles());
              deleteAppointmentComboBox.setItems(Database.getAllAppointmentTitles());
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Appointment added");
                a.show();
          }
        }

        @FXML
        public void populateModifyApptFields() throws SQLException {
        if (modifyAppointmentComboBox.getSelectionModel().getSelectedItem() != null){
           String appointment = modifyAppointmentComboBox.getSelectionModel().getSelectedItem();
           int apptId = Integer.parseInt(appointment.replaceAll("\\D", ""));
           ///Uses Java streams and lambdas (predicate). Using lambdas means it doesn't create anonoymous inner classes anymore
            Appointment a = Database.getAllAppointments().stream().filter(appt -> appt.getAppointmentId() == apptId).findFirst().orElse(null);

                datePicker1.setValue(a.getAppointmentStart().toLocalDate());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                LocalDateTime templt = a.getAptStartConverted();
                LocalDateTime templt2 = a.getAptEndConverted();

            String ltStart = templt.format(formatter);
            String ltEnd = templt2.format(formatter);
               startTimeComboBox1.setValue(ltStart);
               endTimeComboBox1.setValue(ltEnd);
                titleTextField1.setText(a.getAppointmentTitle());

                   addAppointmentDescription1.setText(a.getAppointmentDescription());
                   addAppointmentLocation1.setText(a.getAppointmentLocation());
                   contactTextField1.setText(a.getAppointmentContact());
                   appointmentTypeComboBox1.setValue(a.getAppointmentDescription());
               }
           }
public void modifyAppointment()throws SQLException, ClassNotFoundException{
        //get user input
        String title = titleTextField1.getText();
        String description = addAppointmentDescription1.getText();
        String location = addAppointmentLocation1.getText();
        String contact = contactTextField1.getText();
        String type = appointmentTypeComboBox1.getSelectionModel().getSelectedItem();
        String url = addAppointmentURL1.getText();
        LocalDate date1 = datePicker1.getValue();
        String startTime1 = startTimeComboBox1.getSelectionModel().getSelectedItem();
        String endTime1 = endTimeComboBox1.getSelectionModel().getSelectedItem();
        String name = modifyAppointmentComboBox.getSelectionModel().getSelectedItem();
        //grab the appointment ID
        //String name1 = name.substring(name.length() - 2);
        String name1 = name.replaceAll("\\D", "");
        int appointmentId = Integer.parseInt(name1);

        //set the LocalDateTime for the database query to retrieve
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime startTime = LocalTime.parse(startTime1, parser);
        LocalTime endTime = LocalTime.parse(endTime1, parser);
        startDateTime1 = LocalDateTime.of(date1, startTime);
        endDateTime1 = LocalDateTime.of(date1, endTime);
    ZoneId zoneId = ZoneId.of("UTC");
    ZonedDateTime zdtUTC = startDateTime1.atZone(ZoneId.systemDefault());
    ZonedDateTime zdtUTC2 = zdtUTC.withZoneSameInstant(zoneId);

    ZonedDateTime zdtUTC3 = endDateTime1.atZone(ZoneId.systemDefault());
    ZonedDateTime zdtUTC4 = zdtUTC3.withZoneSameInstant(zoneId);

    startDateTime1 = zdtUTC2.toLocalDateTime();
    endDateTime1 = zdtUTC4.toLocalDateTime();
        //check for overlapping appointments
        if (isOverlappingAppointment(startTime, endTime, date1)){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Appointment can not be modified due to overlapping times with another appointment.");
            a.show();
        }
        else if (startTime1.isEmpty() || endTime1.isEmpty() || name.isEmpty() || type.isEmpty() || title.isEmpty() || description.isEmpty() || location.isEmpty() || contact.isEmpty() || url.isEmpty())
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Empty Fields");
            a.show();
        }
        else {

            //update the database and combobox
            Database.modifyAppointment(appointmentId, title, description, location, contact, type, url);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Appointment has been modified.");
            a.show();
            modifyAppointmentComboBox.setItems(Database.getAllAppointmentTitles());
            deleteAppointmentComboBox.setItems(Database.getAllAppointmentTitles());
        }
}

    @FXML
    private void backToMain(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    @FXML
    private void backToCalender(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("calender.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

public void deleteAppointment() throws SQLException, ClassNotFoundException {
    String name = deleteAppointmentComboBox.getSelectionModel().getSelectedItem();
   // String name1 = name.substring(name.length() - 2);
    String name1 = name.replaceAll("\\D", "");
    System.out.println(Integer.parseInt(name1));
    int appointmentId = Integer.parseInt(name1);
    Database.deleteAppointment(appointmentId);
    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
    a.setContentText("Appointment deleted.");
    a.show();
    deleteAppointmentComboBox.setItems(Database.getAllAppointmentTitles());
    modifyAppointmentComboBox.setItems(Database.getAllAppointmentTitles());

}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LocalTime time = LocalTime.MIDNIGHT.plusHours(8);
        for (int i = 0; i < 10; i++) {
            timeOptions.add(time.format(time1));
            time = time.plusMinutes(60);
        appointmentTypeComboBox.setItems(types);
        startTimeComboBox.setItems(timeOptions);
        endTimeComboBox.setItems(timeOptions);
            startTimeComboBox1.setItems(timeOptions);
            endTimeComboBox1.setItems(timeOptions);
            appointmentTypeComboBox1.setItems(types);
            addCustomerIdComboBox.setItems(Database.getAllCustomersNames());

            try {
                deleteAppointmentComboBox.setItems(Database.getAllAppointmentTitles());
                modifyAppointmentComboBox.setItems(Database.getAllAppointmentTitles());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        }
    }

