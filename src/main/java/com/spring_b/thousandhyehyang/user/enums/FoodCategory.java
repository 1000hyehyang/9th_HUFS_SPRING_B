package com.spring_b.thousandhyehyang.user.enums;

public enum FoodCategory {
    KOREAN("한식"),
    JAPANESE("일식"),
    CHINESE("중식"),
    WESTERN("양식"),
    CHICKEN("치킨"),
    SNACK_BAR("분식"),
    BBQ_GRILL("고기/구이"),
    LUNCHBOX("도시락"),
    LATE_NIGHT("야식"),
    FAST_FOOD("패스트푸드"),
    DESSERT("디저트"),
    ASIAN("아시안푸드");

    private final String displayName;

    FoodCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
