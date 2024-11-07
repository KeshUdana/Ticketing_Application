package managment.backend.model;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name="Customer")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger userID;
    @Column(name="Username")
    private String userUsername;
    @Column(name="Email",nullable = false,unique = true)
    private String userEmail;
    @Column(name="Password")
    private String userPassword;

    //Getters

    public BigInteger getUserID(){return userID;}
    public String getUserUsername(){return userUsername;}
    public String getUserEmail(){return userEmail;}
    public String getUserPassword(){return userPassword;}

    //Setters

    public void setUserID(BigInteger userID){
        this.userID=userID;
    }
    public void setUserUsername(String userUsername){
        this.userUsername=userUsername;
    }
    public void setUserEmail(String userEmail){
        this.userEmail=userEmail;
    }
    public void setUserPassword(String userPassword){
        this.userPassword=userPassword;
    }

}
