package com.Von;

import com.Von.Controllers.Impl.LibraryControllerImpl;

public class Main {
    public static void main(String[] args) {
        LibraryControllerImpl myLMS = LibraryControllerImpl.getInstance();
        myLMS.start();
    }
}