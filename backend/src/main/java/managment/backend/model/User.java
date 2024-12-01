package managment.backend.model;

import jakarta.persistence.*;


@Entity
@Table(name="Customer")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userID;
    @Column(name="Username")
    private String userUsername;
    @Column(name="Email",nullable = false,unique = true)
    private String userEmail;
    @Column(name="Password")
    private String userPassword;

    //Constructor
    public User(String userID){
        this.userID=userID;
        this.userUsername=userUsername;
        this.userEmail=userEmail;
        this.userPassword=userPassword;
    }

    public User() {}//for JPA

    //Getters
    public  String getUserID(){return userID;}
    public String getUserUsername(){return userUsername;}
    public String getUserEmail(){return userEmail;}
    public String getUserPassword(){return userPassword;}

    //Setters

    public void setUserID(String userID){
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

    public String toString(){
        return "User{"+"userID="+ userID+ '}';
    }

}
