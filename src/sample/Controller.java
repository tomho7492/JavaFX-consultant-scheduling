package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    private String invalidText;

    @FXML
    Alert invalidLogin = new Alert(Alert.AlertType.ERROR);

    public static String userName;

    public static void recordLogin(String userName) throws IOException {
        File file = new File("testFile1.txt");
        if (file.createNewFile())
        {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }
        FileWriter writer = new FileWriter(file, true);
        LocalDateTime time = LocalDateTime.now();
        String data = time.toString();
        writer.write('\n' +userName + " logged in at " +data + '\n');
        writer.close();
    }
    @FXML
    public void verifyLogin(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
    Database.connectDatabase();
    String sql = "Select * from user where userName=? and password=?";
        PreparedStatement pst = Database.getConnection().prepareStatement(sql);
        pst.setString(1, usernameField.getText());
        pst.setString(2, passwordField.getText());
        ResultSet rs = pst.executeQuery();
        if (rs.next()){

            userName = usernameField.getText();
            recordLogin(this.userName);
           System.out.println(Database.getUserId());
            Parent mainScreen = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene scene = new Scene(mainScreen);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
           if ( Database.alert15() == true){
               Alert a = new Alert(Alert.AlertType.INFORMATION);
               a.setContentText("You have an appointment in 15 minutes!");
               a.show();
           }
        }
        else {
            invalidLogin.setContentText(invalidText);
            invalidLogin.show();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()));
                Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("rb", locale);
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        invalidText = rb.getString("invalid");
        loginButton.setText(rb.getString("login"));
    }
}



