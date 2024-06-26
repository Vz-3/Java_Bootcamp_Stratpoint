package com.Von.Controllers;

import com.Von.Controllers.Impl.LibraryControllerImpl;

public interface LibraryController {
    static LibraryControllerImpl getInstance() {
        return null;
    };

    public void start();

    public void removeBookBy();

    public void searchBookBy();

    public void addBookBy();

}
