package sample;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

public class Calender implements Initializable {
    @FXML
    private GridPane CalendarPane;

    @FXML
    private Label sundayLabel;

    @FXML
    private Label calendarLabel;


    @FXML
    private ListView<String> week1;

    @FXML
    private ListView<String> week2;

    @FXML
    private ListView<String> week3;

    @FXML
    private ListView<String> week4;
    @FXML
    private ListView<String> week5;

    @FXML
    private Text week5Label;

    @FXML
    private Button weeklyViewButton;
    public static int[] VALUE_MAPPER = {0, 1, 2, 3, 4, 5, 6, 0};
    ObservableList<Appointment> mondayAppts = FXCollections.observableArrayList();
    ObservableList<Appointment> tuesdayAppts = FXCollections.observableArrayList();
    ObservableList<Appointment> wednesdayAppts = FXCollections.observableArrayList();
    ObservableList<Appointment> thursdayAppts = FXCollections.observableArrayList();
    ObservableList<Appointment> fridayAppts = FXCollections.observableArrayList();
    ObservableList<Appointment> saturdayAppts = FXCollections.observableArrayList();
    ObservableList<Appointment> sundayAppts = FXCollections.observableArrayList();


    private void listClear(){
        mondayAppts.clear();
        tuesdayAppts.clear();
        wednesdayAppts.clear();
        thursdayAppts.clear();
        fridayAppts.clear();
        saturdayAppts.clear();
        sundayAppts.clear();
    }


    private void organize() throws SQLException {
        for (Appointment appointment : Database.getAllAppointments()) {
            if (calendarLabel.getText() == appointment.getAppointmentStart().getMonth().name()) {
                int dayValue = appointment.getAppointmentStart().getDayOfWeek().getValue();
                switch (dayValue) {
                    case 1:
                        mondayAppts.add(appointment);
                        break;
                    case 2:
                        tuesdayAppts.add(appointment);
                        break;
                    case 3:
                        wednesdayAppts.add(appointment);
                        break;
                    case 4:
                        thursdayAppts.add(appointment);
                        break;
                    case 5:
                        fridayAppts.add(appointment);
                        break;
                    case 6:
                        saturdayAppts.add(appointment);
                        break;
                    case 7:
                        sundayAppts.add(appointment);
                        break;
                }
            }
        }
    }
    private void switchCase(ObservableList<Appointment> xAppts){
        for (Appointment appointment : xAppts){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime lt = appointment.getAptStartConverted();
            LocalDateTime lt2 = appointment.getAptEndConverted();
            String startTime = lt.format(formatter) + "-" + lt2.format(formatter);
            int week = appointment.getWeek();
            switch (week) {
                case 1:
                    week1.getItems().add(appointment.getAppointmentStart().getDayOfWeek() + " " + lt.getDayOfMonth() + "th" + " " + startTime +  " " + appointment.getAppointmentTitle());
                    break;
                case 2:
                    week2.getItems().add(appointment.getAppointmentStart().getDayOfWeek() + " " + lt.getDayOfMonth() + "th" + " " + startTime + " " + appointment.getAppointmentTitle());
                    break;
                case 3:
                    week3.getItems().add(appointment.getAppointmentStart().getDayOfWeek() + " " + lt.getDayOfMonth() + "th" + " " + startTime + " " + appointment.getAppointmentTitle());
                    break;
                case 4:
                    week4.getItems().add(appointment.getAppointmentStart().getDayOfWeek() + " " + lt.getDayOfMonth() + "th" + " " + startTime + " " + appointment.getAppointmentTitle());
                    break;
                case 5:

                    week5.getItems().add(appointment.getAppointmentStart().getDayOfWeek() + " " + lt.getDayOfMonth() + "th" + " " + startTime + " " + appointment.getAppointmentTitle());
                    break;
            }
        }
    }
    private void populateCalendar() throws SQLException {
     organize();
     switchCase(mondayAppts);
     switchCase(tuesdayAppts);
     switchCase(wednesdayAppts);
     switchCase(thursdayAppts);
     switchCase(fridayAppts);
     switchCase(saturdayAppts);
     switchCase(sundayAppts);
    }
    /*

        private void populateCalendar() throws SQLException {

            for (Appointment appointment : Database.getAllAppointments()) {

                    String title = appointment.getAppointmentTitle();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                    LocalDateTime startTime = appointment.getAppointmentStart();
                    LocalDateTime endTime = appointment.getAppointmentEnd();

                    String formattedStartTime = startTime.format(formatter);
                    String formattedEndTime = endTime.format(formatter);

                    LocalDateTime lt1 = appointment.getAppointmentStart();

                    if (lt1.getMonth().name() == calendarLabel.getText()) {
                        String alertData = title + " from " + formattedStartTime + "-" + formattedEndTime + "\n";
                        LocalDateTime startOfMonth = lt1.with(firstDayOfMonth());
                        int firstDayNameIndex = VALUE_MAPPER[startOfMonth.getDayOfWeek().getValue()];
                        int currentDayNameIndex = VALUE_MAPPER[lt1.getDayOfWeek().getValue()];
                        int row = (lt1.getDayOfMonth() + firstDayNameIndex - 1) / 7;
                        int column = VALUE_MAPPER[lt1.getDayOfWeek().getValue()];
                        Label label = new Label(alertData);
                        label.setWrapText(true);
                        VBox vbox = new VBox();
                        vbox.getChildren().add(label);
                        CalendarPane.add(vbox, column, row);
                    }
                }
            }

     */
    @FXML
    private void setMonth() throws SQLException {
        week1.getItems().clear();
        week2.getItems().clear();
        week3.getItems().clear();
        week4.getItems().clear();
        week5.getItems().clear();
        listClear();
        ObservableList<String> months = FXCollections.observableArrayList();
        months.setAll("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
        String currentMonth = calendarLabel.getText();
        int idxCurrentMonth = months.indexOf(currentMonth);

        int idxNewMonth = idxCurrentMonth + 1;
        if (idxCurrentMonth == 11) {
            idxNewMonth = 0;
        }
        calendarLabel.setText(months.get(idxNewMonth));
        populateCalendar();

    }
    @FXML
    private void setPreviousMonth() throws SQLException {
        week1.getItems().clear();
        week2.getItems().clear();
        week3.getItems().clear();
        week4.getItems().clear();
        week5.getItems().clear();
        listClear();
        ObservableList<String> months = FXCollections.observableArrayList();
        months.setAll("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
        String currentMonth = calendarLabel.getText();
        int idxCurrentMonth = months.indexOf(currentMonth);

        int idxNewMonth = idxCurrentMonth - 1;
        if (idxCurrentMonth == 0) {
            idxNewMonth = 11;
        }
        calendarLabel.setText(months.get(idxNewMonth));
        populateCalendar();

    }
    @FXML
    private void switchToWeeklyScene(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("weeklyCalender.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }
    @FXML
    private void back(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("appointments.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalDate lt = LocalDate.now();
        String month = lt.getMonth().name();
        calendarLabel.setText(month);
        try {
            populateCalendar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
