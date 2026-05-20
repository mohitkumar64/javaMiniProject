/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ats;

/**
 *
 * @author Mohit kumar
 */
public class User {
    String email;
    String role;
    String status;
    int id;
    
    public void setEmail(String e){
        email = e;
    }
    public void setRole(String e){
        role = e;
    }
    public void setStatus(String e){
        status = e;
    }
    public void setId(int e){
        id = e;
    }
    public String isAllowed(){
        return status;
    }
    
    
    public String getRole(){
        return role;
    }
     public String getId(){
        return role;
    }
    
    
    
}
