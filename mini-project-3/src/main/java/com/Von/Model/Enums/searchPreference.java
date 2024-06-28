package com.Von.Model.Enums;

public enum searchPreference {
    byName(0),
    bySN(1),
    bySeller(2);

    private Integer index;

    searchPreference(Integer i) {
        this.index = i;
    }
}
