package com.InternShip.Validators;

import com.InternShip.Services.UserService;

public class UserValidator {
    public static boolean isEmailValidToAdd( UserService userService, String email, StringBuilder errorMessage ){
        if(userService.isEmailUnique(email)){
            return true;
        }

        errorMessage.append("Користвувач із таким email вже існує");
        return false;
    }

    public static boolean isEmailValidToEdit( UserService userService, String email, long id, StringBuilder errorMessage ){
        if(userService.isEmailUniqueExludingId(email, id)){
            return true;
        }

        errorMessage.append("Користвувач із таким email вже існує");
        return false;
    }



}
