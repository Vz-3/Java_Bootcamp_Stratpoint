package com.Von.Model.Enums;

public enum builderPreference {
    minimum(0),
    withDescription(1),
    withSeller(2),
    allDetails(3);

    private Integer index;

    builderPreference(Integer i) {
        this.index = i;
    }
}
