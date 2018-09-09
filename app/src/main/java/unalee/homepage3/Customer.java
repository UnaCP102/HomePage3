package unalee.homepage3;

import java.io.Serializable;
import java.sql.Blob;

@SuppressWarnings("serial")
public class Customer implements Serializable{

    private int customerID;
    private String name, email, password="", phone, birthday, address, gender;
    private int discount;
    private Blob customerPic;
    public int customerId;

    public Customer (int customerID, String name, String email, String password, String gender,
                     String birthday, String phone, String address){
        this.customerID = customerID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
        this.address = address;
    }

    public boolean equals(Object obj) {
        return this.customerID ==((Customer) obj).customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
