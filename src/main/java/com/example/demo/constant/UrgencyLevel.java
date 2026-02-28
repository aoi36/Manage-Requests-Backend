package com.example.demo.constant;

public enum UrgencyLevel {
    LOW("Thấp"),
    MEDIUM("Trung bình"),
    HIGH("Cao");

    private final String label;

    UrgencyLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}