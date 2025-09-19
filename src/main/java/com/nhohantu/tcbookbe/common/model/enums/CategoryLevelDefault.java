package com.nhohantu.tcbookbe.common.model.enums;

public enum CategoryLevelDefault {
    DEFAULT(1);
    private final int categoryLevel;

    CategoryLevelDefault(int categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public int getCategoryLevel() {
        return categoryLevel;
    }
}
