package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private int appointmentId;
    private int appointmentCustomerId;
    private  LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentContact;

    public Appointment(int id, int custId, LocalDateTime start, LocalDateTime end, String title, String description, String location, String contact) {
        this.appointmentId = id;
        this.appointmentCustomerId = custId;
        this.appointmentStart = start;
        this.appointmentEnd = end;
        this.appointmentTitle = title;
        this.appointmentDescription = description;
        this.appointmentLocation = location;
        this.appointmentContact = contact;
    }

    public LocalDateTime getAptStartConverted() {
        LocalDateTime ldt = this.appointmentStart;
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid);
        return utcDate.toLocalDateTime();
    }
    public LocalDateTime getAptEndConverted() {
        LocalDateTime ldt = this.appointmentEnd;
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid);
       return utcDate.toLocalDateTime();

    }

    public int getWeek(){
        LocalDateTime lt = getAppointmentStart();
       int month = getAppointmentStart().getMonth().getValue();
    int daysInMonth;
        if (month == 4 || month == 6 || month == 9 || month == 11)
            daysInMonth = 30;
        else
        if (month == 2)
            if (lt.toLocalDate().isLeapYear()){
                daysInMonth = 29;
            }
             else daysInMonth=28;
        else
            daysInMonth = 31;

        double day =  appointmentStart.getDayOfMonth();

        if (day/7.0 <= 1.0){
            return 1;
        }
        else if (day/7.0 <= 2.0){
            return 2;
        }
        else if (day/7.0 <= 3.0){
            return 3;
        }
        else if (daysInMonth >= 29 && day/7.0 >= 4.0){
            return 5;
        }
        else
            return 4;
    }

    public String getAppointmentTitleAndTime(){
        return appointmentTitle + " " + appointmentStart.toLocalTime().toString();
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getAppointmentCustomerId() {
        return appointmentCustomerId;
    }

    public void setAppointmentCustomerId(int appointmentCustomerId) {
        this.appointmentCustomerId = appointmentCustomerId;
    }

    public LocalDateTime getAppointmentStart() {
        return appointmentStart;
    }

    public void setAppointmentStart(LocalDateTime appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }

    public void setAppointmentEnd(LocalDateTime appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getAppointmentContact() {
        return appointmentContact;
    }

    public void setAppointmentContact(String appointmentContact) {
        this.appointmentContact = appointmentContact;
    }


}

