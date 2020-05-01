package com.jayare.jappanese.category;

public enum CategoryEnum {
    VOCABULARY("Vocabulary"),
    PHRASES("Phrases"),
    FOOD("Food"),
    DIRECTIONS("Directions"),
    SHOPPING("Shopping"),
    SLANG("Slang"),
    VERBS("Verbs");

    private String category;

    CategoryEnum(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
