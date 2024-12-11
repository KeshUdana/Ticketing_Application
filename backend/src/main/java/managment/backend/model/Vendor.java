package managment.backend.model;

import jakarta.persistence.*;


@Entity
@Table(name="Vendor")
public class Vendor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String vendorID;
    @Column(name="Username")
    private String vendorUsername;
    @Column(name="Email",nullable = false,unique = true)
    private String vendorEmail;
    @Column(name="Password")
    private String vendorPassword;

    //Constructor
    public Vendor(String vendorID){
        this.vendorID= vendorID;}

    public Vendor() {}

    public String getVendorID(){return vendorID;}

    public void setVendorID(String vendorID){
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
