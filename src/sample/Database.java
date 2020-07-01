package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;

public class Database {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();


    private static Connection conn;
    public static void connectDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        // enter db info
        conn = DriverManager.getConnection("enter db info here");
     // ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
       // String timeZoneQuery = "SET time_zone = '" + offset + "';";
       // Statement stm = getConnection().createStatement();
       // stm.executeUpdate(timeZoneQuery);
    }

    public static boolean alert15() throws SQLException {

        for (Appointment appointment: Database.getAllAppointments()){


            if (appointment.getAptStartConverted().toLocalDate().isEqual(LocalDate.now())) {
                System.out.println("dates are equal");
                LocalTime start = appointment.getAptStartConverted().toLocalTime();
                LocalTime now = LocalTime.now();
                System.out.println("now" + now + "start" + start);
                String title = appointment.getAppointmentTitle();
                String description = appointment.getAppointmentDescription();
                long elapsedMinutes = Duration.between(now, start).toMinutes();

                if (elapsedMinutes <= 15 && elapsedMinutes >= 0) {
                    System.out.println(elapsedMinutes);
                    return true;
                }
                else {
                    System.out.println(elapsedMinutes);
                }
            }
        }

        return false;

    }
    public static Connection getConnection(){
        return conn;
    }

    public static void disconnectDatabase() throws SQLException {
        conn.close();
    }


    public static ObservableList<User> getAllUsers() throws SQLException{
        allUsers.clear();
        String query = "Select user.userId, user.userName, user.password from user";
        Statement stm = Database.getConnection().createStatement();
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()){
            User user = new User(
                    rs.getInt("userId"),
                    rs.getString("userName"),
                    rs.getString("password")
            );
            allUsers.add(user);
        }
        return allUsers;
    }
    //Returns all Appointments in Database
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        allAppointments.clear();
        Statement stm = Database.getConnection().createStatement();

        //int id, int custId, String start, String end, String title, String description, String location, String contact)
        String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.start, appointment.end, appointment.title, appointment.description, appointment.location, appointment.contact FROM appointment";
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()){


            Appointment appointment = new Appointment(
                    rs.getInt("appointmentId"),
                    rs.getInt("customerId"),
                    rs.getTimestamp("start").toLocalDateTime(),
                     rs.getTimestamp("end").toLocalDateTime(),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("contact"));
                allAppointments.add(appointment);

        }
return allAppointments;
    }

    // Returns all Customers in Database
    public static ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT customer.customerId, customer.customerName, address.address, address.phone, address.postalCode, city.city, address.address2"
                    + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                    + "INNER JOIN city ON address.cityId = city.cityId";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Customer customer = new Customer(
                        results.getInt("customerId"),
                        results.getString("customerName"),
                        results.getString("address"),
                        results.getString("address2"),
                        results.getString("city"),
                        results.getString("phone"),
                        results.getString("postalCode"));
                allCustomers.add(customer);
            }
            statement.close();
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public static ObservableList<String> getAllCustomersNames(){
        ObservableList<String> customersNames = FXCollections.observableArrayList();
        for (Customer customer: getAllCustomers()){
        
            customersNames.add(String.format("%s ID: %d", customer.getName(), customer.getId()));

        }
        return customersNames;
    }

    public static ObservableList<String> getAllAppointmentTitles() throws SQLException {
        ObservableList<String> appointmentNames = FXCollections.observableArrayList();
        for (Appointment appointment: getAllAppointments()){

            appointmentNames.add(String.format("%s ID: %d", appointment.getAppointmentTitle(), appointment.getAppointmentId()));

        }
        return appointmentNames;
    }

    public static int getAppointmentForUser(int userId) throws SQLException {
    int apptId = 0;
        String query = "SELECT appointmentId FROM appointment WHERE userId = " + userId;
       Statement stm = getConnection().createStatement();
       ResultSet rs = stm.executeQuery(query);
       while (rs.next()){
           apptId =  rs.getInt("appointmentId");
       }
        return apptId;
    }
    public static boolean addCustomer(String name, String address, String address2, int cityId, String phoneNumber, String postal, String username) throws SQLException, ClassNotFoundException {
        //update to use string.format
        int countryId;
        String country;
        String cityName = MainScreen.getCities().get(cityId - 1);
        if (cityId == 1 || cityId == 2){
            countryId = 1;
            country = "United States";
        }
        else {
            country = "England";
            countryId = 2;
        }

        String sql = "Select * from country where countryId=?";
                String setAddressQuery = String.format("INSERT INTO address SET address ='%s', cityId='%d', phone='%s', postalCode='%s', address2='%s', createDate = NOW(), createdBy = '%s', lastUpdate = NOW(), lastUpdateBy = '%s';"
                , address, cityId, phoneNumber, postal, address2, username, username);

        String setCountryQuery =  String.format("INSERT IGNORE INTO country SET countryId = '%d', country = '%s', createDate = NOW(), createdBy = '%s', lastUpdate = NOW(), lastUpdateBy = '%s';",
                countryId, country, username, username);

        String setCityQuery = String.format("INSERT IGNORE INTO city SET cityId = '%d', city='%s', countryId = '%d', createDate = NOW(), createdBy = '%s', lastUpdate = NOW(), lastUpdateBy = '%s';"
        , cityId, cityName, countryId, username, username);




   Statement stm = getConnection().createStatement();
    stm.executeUpdate(setCountryQuery);
    stm.executeUpdate(setCityQuery);
    int addressId = 0;
            int checkpointOne = stm.executeUpdate(setAddressQuery, Statement.RETURN_GENERATED_KEYS);
            if (checkpointOne == 1) {

                ResultSet rs = stm.getGeneratedKeys();
               if (rs.next()) {
                    addressId = rs.getInt(1);
                }

            }
        String setCustomerQuery = String.format("INSERT INTO customer SET customerName='%s', addressId='%d', active = '1', createDate = NOW(), createdBy = '%s', lastUpdate = NOW(), lastUpdateBy = '%s';",
                name, addressId, username, username);
                int checkpointTwo = stm.executeUpdate(setCustomerQuery);
                if (checkpointTwo == 1){
                    return true;
                }
        return false;

    }


    public static boolean deleteCustomer(int id) throws SQLException, ClassNotFoundException {
        Statement stm = Database.getConnection().createStatement();
        String deleteAppointmentsQuery = "DELETE FROM appointment WHERE customerId = '" + id + "'";
        String deleteCustomerQuery = "DELETE FROM customer WHERE customerId='" + id + "'";
        String deleteAddressQuery = "DELETE FROM address WHERE addressId='" + id + "'";
        stm.executeUpdate(deleteAppointmentsQuery);
        stm.executeUpdate(deleteCustomerQuery);
        stm.executeUpdate(deleteAddressQuery);

return true;
    }

    public static boolean modifyCustomer(int id, String name, String address, int cityId, String phoneNumber, String postal, String address2) throws SQLException, ClassNotFoundException {
        //update to use string.format
        String updateAddressQuery = String.format("UPDATE address SET address ='%s', cityId='%d', phone='%s', postalCode='%s', address2='%s', createDate = NOW(), createdBy = '%s', lastUpdate = NOW(), lastUpdateBy = '%s' WHERE addressId = '%d';"
        , address, cityId, phoneNumber, postal, address2, "test", "test", id);
        String updateCustomerQuery =   String.format("UPDATE customer SET customerName='%s', addressId='%d', active = '1', createDate = NOW(), createdBy = '%s', lastUpdate = NOW(), lastUpdateBy = '%s' WHERE customerId = '%d';"
                , name, id, "test", "test", id);


        Statement stm = getConnection().createStatement();
        int checkpointOne = stm.executeUpdate(updateAddressQuery);
        if (checkpointOne == 1){
            int checkpointTwo = stm.executeUpdate(updateCustomerQuery);
            if (checkpointTwo == 1){
                return true;
            }
           }
        return false;
    }




    public static boolean addAppointment(int id, String title, String description, String location, String contact, String type, String url) throws SQLException, ClassNotFoundException {
        Connection conn = Database.getConnection();
        Statement stm = conn.createStatement();
        String username = Controller.userName;
        LocalDateTime start = Appointments.startDateTime;
        LocalDateTime end = Appointments.endDateTime;
        int userId = 1; //need to implement in parameters and grab user ID..

        String addApptQuery = String.format("INSERT INTO appointment SET customerId = '%d', userId = '%d', title = '%s', description = '%s', location = '%s', contact = '%s', type = '%s', url = '%s', start = '%s', end = '%s', createDate = NOW(), createdBy = '%s', lastUpdate = NOW(), lastUpdateBy = '%s';"
        , id, userId, title, description, location, contact, type, url, start, end, "test1", "test2");
        int checkpointOne =  stm.executeUpdate(addApptQuery);
        if (checkpointOne == 1){
            return true;

        }
return false;
    }

    public static int getUserId() throws SQLException {
      String username = Controller.userName;
      String getUserIdQuery = String.format("SELECT userId FROM user WHERE userName = '%s';",username);
      Statement stm = getConnection().createStatement();
      ResultSet rs = stm.executeQuery(getUserIdQuery);
        if (rs.next()) {
            String userId = rs.getString("userId");
            return Integer.parseInt(userId);
        }

        else {
            return 0;

        }
    }

   public static boolean modifyAppointment(int id, String title, String description, String location, String contact, String type, String url) throws SQLException {
    int userId = getUserId();
    String username = Controller.userName;
       LocalDateTime start = Appointments.startDateTime1;
       LocalDateTime end = Appointments.endDateTime1;
       String modifyApptQuery = String.format("UPDATE appointment SET userId = '%d', title = '%s', description = '%s', location = '%s', contact = '%s', type = '%s', url = '%s', start = '%s', end = '%s', createDate = NOW(),  lastUpdate = NOW(), lastUpdateBy = '%s' WHERE appointmentId = '%d';"
               , userId, title, description, location, contact, type, url, start, end, username, id);
       Statement stm = getConnection().createStatement();
      int checkpointOne = stm.executeUpdate(modifyApptQuery);
      if (checkpointOne == 1) {
          return true;
      }
       return false;
   }


    public static boolean deleteAppointment(int id) throws SQLException, ClassNotFoundException {

        Connection conn = Database.getConnection();
        Statement stm = conn.createStatement();
        String deleteQuery = "DELETE FROM appointment WHERE appointmentId =" + id;
        int checkpoint = stm.executeUpdate(deleteQuery);
        if (checkpoint == 1) {
            return true;
        }
        return false;
    }
    }

