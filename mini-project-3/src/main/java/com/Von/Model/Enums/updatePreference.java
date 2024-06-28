package com.Von.Model.Enums;

public enum updatePreference {
    updateName(0),
    updatePrice(1),
    updateDescription(2),
    updateSeller(3);

    private Integer index;

    updatePreference(Integer i) {
        this.index = i;
    }
}
