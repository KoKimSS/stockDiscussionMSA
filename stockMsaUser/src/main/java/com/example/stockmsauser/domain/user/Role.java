package com.example.stockmsauser.domain.user;

public enum Role {
    USER,ADMIN;

    public static String toString(Role role) {
        if(role==USER){
            return "USER";
        }
        if (role == ADMIN) {
            return "ADMIN";
        }
        return null;
    }
}
