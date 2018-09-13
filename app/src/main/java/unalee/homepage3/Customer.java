package unalee.homepage3;

import java.io.Serializable;
import java.sql.Blob;

@SuppressWarnings("serial")
public class Customer{

    int IdCustomer;
    private String name, email, password, phone, birthday, address, gender, customerId;
//    private int discount;
    private Blob customerPic;


    public Customer (int IdCustomer, String customerID, String name, String email, String password, String gender,
                     String birthday, String phone, String address, Blob customerPic){
        this.customerId = customerID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
        this.address = address;
        this.customerPic = customerPic;
    }

    public Customer (int IdCustomer, String customerID, String name, String email, String password, String gender,
                     String birthday, String phone, String address){
        return;
    }

    public int getIdCustomer(){
        return IdCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        IdCustomer = idCustomer;
    }

    public boolean equals(Object obj) {
        return this.customerId ==((Customer) obj).customerId;
    }

    public String getCustomerID() {
        return customerId;
    }

    public void setCustomerID(String customerID) {
        this.customerId = customerID;
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

    public Blob getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(Blob customerPic) {
        this.customerPic = customerPic;
    }
}
