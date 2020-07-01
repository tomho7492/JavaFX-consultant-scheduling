package sample;



public class Customer {
    private int id;
    private String name;
    private String address;
    private String address2;
    private String city;
    private String phoneNumber;
    private String zipcode;

    public Customer(int id, String name, String address, String address2, String city, String phoneNumber, String zipcode){
    this.id = id;
    this.name = name;
    this.address = address;
    this.address2 = address2;
    this.city = city;
    this.phoneNumber = phoneNumber;
    this.zipcode = zipcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }





    }
