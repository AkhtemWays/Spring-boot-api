package com.example.demo.shared;

import java.util.ArrayList;
import java.util.List;

public enum LaptopType {
    ACER("ACER"),
    DELL("DELL"),
    MAC("MAC"),
    SAMSUNG("SAMSUNG");

    private final String type;
    private final List<String> possibleTypes;

    public Boolean validateLaptopType(LaptopType laptopType) {
        return laptopType == LaptopType.ACER || laptopType == LaptopType.DELL ||
                laptopType == LaptopType.MAC || laptopType == LaptopType.SAMSUNG;
    }

    LaptopType(String type) {
        this.type = type;
        possibleTypes = new ArrayList<>();
        possibleTypes.add("ACER");
        possibleTypes.add("DELL");
        possibleTypes.add("MAC");
        possibleTypes.add("SAMSUNG");
    }
}
