package managment.backend.model;

import jakarta.persistence.*;


@Entity
@Table(name="Vendor")
public class Vendor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vendorID;
    @Column(name="Username")
    private String vendorUsername;
    @Column(name="Email",nullable = false,unique = true)
    private String vendorEmail;
    @Column(name="Password")
    private String vendorPassword;

    //Constructor
    public Vendor(String vendorID){//,String vendorUsername,String vendorEmail,String vendorPassword){
        this.vendorID=vendorID;
        this.vendorUsername=vendorUsername;
        this.vendorEmail=vendorEmail;
        this.vendorPassword=vendorPassword;
    }

    public Vendor() {}//For JPA

    //Getters
    public int getVendorID(){return vendorID;}
    public String getVendorUsername(){return vendorUsername;}
    public String getVendorEmail(){return vendorEmail;}
    public String getVendorPassword(){return vendorPassword;}

    //Setters

    public void setVendorID(int vendorID){
        this.vendorID=vendorID;
    }
    public void setVendorUsername(String vendorUsername){
        this.vendorUsername=vendorUsername;
    }
    public void setVendorPassword(String vendorPassword){
        this.vendorPassword=vendorPassword;
    }
    public void setVendorEmail(String vendorEmail){
        this.vendorEmail=vendorEmail;
    }

    public String toString(){
        return "Vendor{"+"vendorID="+ vendorID+ '}';
    }

}
