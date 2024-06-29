package com.Von.Model.Enums;

public class customEnums {
    private customEnums() {}

    public enum updatePreference {
        updateName,
        updatePrice,
        updateDescription,
        updateSeller;
    }

    public enum searchPreference {
        byName(),
        bySN(),
        bySeller();
    }

    public enum builderPreference {
        minimum,
        withDescription,
        withSeller,
        allDetails;
    }

}


