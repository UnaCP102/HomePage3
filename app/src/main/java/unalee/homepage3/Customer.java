package unalee.homepage3;


import java.sql.Blob;

@SuppressWarnings("serial")
public class Customer{

    int IdCustomer;
    private String Name, Email, Password, Birthday, Phone, Address, Gender, CustomerId;
//    private int discount;
    private Blob CustomerPic;


    public Customer (int IdCustomer, String customerID, String name, String email, String password, String gender,
                     String birthday, String phone, String address, Blob customerPic){
        this.IdCustomer = IdCustomer;
        this.CustomerId = customerID;
        this.Name = name;
        this.Email = email;
        this.Password = password;
        this.Gender = gender;
        this.Phone = phone;
        this.Birthday = birthday;
        this.Address = address;
        this.CustomerPic = customerPic;
    }

    public Customer (int idCustomer, String customerID, String name, String email, String password, String gender,
                     String birthday, String phone, String address){
        this.IdCustomer = idCustomer;
        this.CustomerId = customerID;
        this.Name = name;
        this.Email = email;
        this.Password = password;
        this.Gender = gender;
        this.Phone = phone;
        this.Birthday = birthday;
        this.Address = address;
    }

    public Customer(String name, String email, String gender, String birthday, String phone, String address) {
        this.Name = name;
        this.Email = email;
        this.Gender = gender;
        this.Phone = phone;
        this.Birthday = birthday;
        this.Address = address;
    }


    public  Customer(int idCustomer, String password, String phone, String address){
        this.IdCustomer = idCustomer;
        this.Password = password;
        this.Phone = phone;
        this.Address = address;

    }

    public Customer(int IdCustomer, String name, String email, String birthday,
                    String Phone, String address) {
        this.IdCustomer = IdCustomer;
        this.Name = name;
        this.Email = email;
        this.Birthday = birthday;
        this.Phone = Phone;
        this.Address = address;
    }

    public int getIdCustomer(){
        return IdCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        IdCustomer = idCustomer;
    }

    public boolean equals(Object obj) {
        return this.CustomerId ==((Customer) obj).CustomerId;
    }

    public String getCustomerID() {
        return CustomerId;
    }

    public void setCustomerID(String customerID) {
        this.CustomerId = customerID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name){
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email){
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public Blob getCustomerPic() {
        return CustomerPic;
    }

    public void setCustomerPic(Blob customerPic) {
        this.CustomerPic = customerPic;
    }
}
