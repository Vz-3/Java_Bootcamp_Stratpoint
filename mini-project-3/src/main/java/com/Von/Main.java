package com.Von;

import com.Von.Controller.Impl.EcommerceControllerImpl;
import com.Von.Model.Enums.customEnums;

public class Main {
    public static void main(String[] args) {
        EcommerceControllerImpl app = new EcommerceControllerImpl();
        app.start(customEnums.Role.Admin);
    }
}