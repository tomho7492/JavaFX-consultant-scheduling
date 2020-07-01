package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;


public class Reports implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab reportOneTab;
    @FXML
    private Tab reportTwoTab;
    @FXML
    private Tab reportThreeTab;
    @FXML
    private TextArea textArea;

    //methods to return boolean whether or not tab is selected
    private boolean reportOneTabSelected(){
        return reportOneTab.isSelected();
    }
    private boolean reportTwoTabSelected(){
        return reportTwoTab.isSelected();
    }
    private boolean reportThreeTabSelected(){
        return reportThreeTab.isSelected();
    }

    //create method to check how many appointments per month
    @FXML
    private void appointmentsPerMonth() throws SQLException {
        HashMap<String, Integer> map =  new HashMap<>();
        map.put("january", 0);
        map.put("february", 0);
        map.put("march", 0);
        map.put("april", 0);
        map.put("may", 0);
        map.put("june", 0);
        map.put("july", 0);
        map.put("august", 0);
        map.put("september", 0);
        map.put("october", 0);
        map.put("november", 0);
        map.put("december", 0);

        for (Appointment appointment: Database.getAllAppointments()){
            LocalDate appointmentDate = appointment.getAppointmentStart().toLocalDate();
            String month = appointmentDate.getMonth().name().toLowerCase();
            map.put(month, map.get(month) + 1);
        }
        String report = "";
        if (reportOneTabSelected()){
            for (String key : map.keySet()){
                report = report + "\n" + key + ":" + map.get(key);
            }
            String data=textArea.getText().trim();
            if(!data.equals("")) {
                textArea.clear();
            }
            textArea.setText(report);
        }
    }

    @FXML
    private void consultantReport() throws SQLException {
        Statement statement = Database.getConnection().createStatement();
        String query = "SELECT appointment.contact, appointment.description, customer.customerName, start, end " +
                "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
                "GROUP BY appointment.contact, MONTH(start), start";
        ResultSet results = statement.executeQuery(query);
        StringBuilder reportTwoText = new StringBuilder();
        reportTwoText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n",
                "Consultant", "Appointment", "Customer", "Start", "End"));
        reportTwoText.append(String.join("", Collections.nCopies(110, "-")));
        reportTwoText.append("\n");
        while(results.next()) {
            reportTwoText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n",
                    results.getString("contact"), results.getString("description"), results.getString("customerName"),
                    results.getString("start"), results.getString("end")));
        }

        if (reportTwoTabSelected()) {
            String data = textArea.getText().trim();
            if (!data.equals("")) {
                textArea.clear();
            }
            textArea.setText(reportTwoText.toString());
        }

        /*





        String report = "";
        for (User user : Database.getAllUsers()){
            report = "\n" + user.getUserName() + " has the following appointment times: ";
            for (Appointment appointment : Database.getAllAppointments()) {
                int apptId = Database.getAppointmentForUser(user.getId());
                if (apptId == appointment.getAppointmentId()) {
                    LocalTime ltStart = appointment.getAppointmentStart().toLocalTime();
                    LocalTime ltEnd = appointment.getAppointmentEnd().toLocalTime();
                    report = report + ltStart + " to " + ltEnd;
                }
            }
        }
        if (reportTwoTabSelected()) {
            String data=textArea.getText().trim();
            if(!data.equals("")) {
                textArea.clear();
            }
            textArea.setText(report);
        }
        */
    }

    @FXML
    private void apptLocationsReport() throws SQLException {
        String report = "";
        HashMap<String, Integer> map =  new HashMap<>();
        for (Appointment appointment: Database.getAllAppointments()){
           String location = appointment.getAppointmentLocation();
           if (map.get(location) != null){
           map.put(location, map.get(location) + 1);
            }
           else{
               map.put(location, 1);
           }
        }
        for (String key: map.keySet()){
            report = report + "\n" + key + ":" + map.get(key);
        }
        if (reportThreeTabSelected()) {
            String data=textArea.getText().trim();
            if(!data.equals("")) {
                textArea.clear();
            }
            textArea.setText(report);
        }
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            appointmentsPerMonth();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
