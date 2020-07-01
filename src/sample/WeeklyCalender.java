package sample;

import javafx.beans.Observable;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Appointment;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WeeklyCalender implements Initializable {
  private static ObservableList<String> weeks1thru5 = FXCollections.observableArrayList("1", "2", "3", "4", "5");
    private static ObservableList<String> months = FXCollections.observableArrayList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");

    @FXML
    private ListView<String> tuesdayList;

    @FXML
    private ListView<String> wednesdayList;

    @FXML
    private ListView<String> thursdayList;

    @FXML
    private ListView<String> fridayList;
    @FXML
    private ListView<String> saturdayList;

    @FXML
    private ListView<String> mondayList;
    @FXML
    private ListView<String> sundayList;

    @FXML
    private ComboBox<String> weekChoiceBox;
    @FXML
    private ComboBox<String> monthChoiceBox;



    @FXML
    private void back(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("calender.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }
    @FXML
    private int daysInMonth(LocalDateTime month) {
        int daysInMonth;

        int monthValue = month.getMonthValue();
        if (monthValue == 4 || monthValue == 6 || monthValue == 9 || monthValue == 11) {
            daysInMonth = 31;
        } else if (monthValue == 2) {
            if (month.toLocalDate().isLeapYear()) {
                daysInMonth = 29;
            } else daysInMonth = 28;
        }
        else
            daysInMonth = 31;
        return daysInMonth;
    }
    @FXML
    private boolean selectionCheck() throws SQLException, ClassNotFoundException {
        if (weekChoiceBox.getSelectionModel().getSelectedItem() == null || monthChoiceBox.getSelectionModel().getSelectedItem() == null){
            return false;
        }
        else {
            populateTable();
            return true;
        }
    }
    @FXML
    private void populateTable() throws SQLException, ClassNotFoundException {
        Database.connectDatabase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        mondayList.getItems().clear();  tuesdayList.getItems().clear();; wednesdayList.getItems().clear(); thursdayList.getItems().clear(); fridayList.getItems().clear();
        saturdayList.getItems().clear(); saturdayList.getItems().clear(); sundayList.getItems().clear();
        System.out.println("clearing");
        for (Appointment appointment : Database.getAllAppointments()) {
            LocalDateTime lt = appointment.getAptStartConverted();
            LocalDateTime lt2 = appointment.getAptEndConverted();
            String ltString = lt.format(formatter) + "-" + lt2.format(formatter);
            int week = appointment.getWeek();
            int selectedWeek = Integer.parseInt(weekChoiceBox.getSelectionModel().getSelectedItem());
            String month = lt.getMonth().name();
            String selectedMonth = monthChoiceBox.getSelectionModel().getSelectedItem();
            String data =  ltString + " " + appointment.getAppointmentTitle();

            if (week == selectedWeek && selectedMonth == month) {

                int dayOfWeek = lt.getDayOfWeek().getValue();
                System.out.println("inside if statement and day of week is " + dayOfWeek);
                switch (dayOfWeek) {
                    case 1:
                        System.out.println("adding appointment");
                        mondayList.getItems().add(data);
                        break;
                    case 2:
                        tuesdayList.getItems().add(data);
                        break;
                    case 3:
                        wednesdayList.getItems().add(data);
                        break;
                    case 4:
                        thursdayList.getItems().add(data);
                        break;
                    case 5:
                        fridayList.getItems().add(data);
                        break;
                    case 6:
                        saturdayList.getItems().add(data);
                        break;
                    case 7:
                        sundayList.getItems().add(data);
                        break;
                }

            }


        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        weekChoiceBox.setItems(weeks1thru5);
        monthChoiceBox.setItems(months);


    }
}


