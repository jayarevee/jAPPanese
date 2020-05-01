package com.jayare.jappanese.definition;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room - creates a SQL table for this class, operations on this table handled by {@link DefinitionDao}
 */
@Entity(tableName = "definiton_table")
public class Definition {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String english;
    private String japanese;
    private String furigana;
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public String getJapanese() {
        return japanese;
    }

    public String getFurigana() {
        return furigana;
    }

    public String getCategory() {
        return category;
    }

    public Definition(String english, String japanese, String furigana, String category) {
        this.english = english;
        this.japanese = japanese;
        this.furigana = furigana;
        this.category = category;
    }

}
