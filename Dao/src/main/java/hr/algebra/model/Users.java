/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

/**
 *
 * @author Bruno
 */
public class Users {

    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String userPass;
    private String email;
    private boolean isAdmin;

    public Users(String firstName, String lastName, String username, String email, boolean isAdmin, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.userPass = password;
    }
    public Users(int id, String firstName, String lastName, String userName, String email, boolean isAdmin, String password) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
    this.isAdmin = isAdmin;
    this.userPass = password;
}


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Users other = (Users) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return firstName + lastName;
    }

    public String getPassword() {
        return userPass;
    }

    public void setPassword(String password) {
        this.userPass = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

}
