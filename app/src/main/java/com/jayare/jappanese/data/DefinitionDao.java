package com.jayare.jappanese.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jayare.jappanese.definition.Definition;

import java.util.List;

/**
 * Operations on the SQLite DB - used by the {@link DefinitionRepository}
 */
@Dao
public interface DefinitionDao {

    @Insert
    void insert(Definition definition);

    @Update
    void update(Definition definition);

    @Delete
    void delete(Definition definition);

    /*@Query("DELETE FROM definiton_table")
    void deleteAllDefinitions();*/

    @Query("SELECT * FROM definiton_table")
    LiveData<List<Definition>> getAllDefinitions();

    @Query(("SELECT DISTINCT category FROM definiton_table ORDER BY category DESC"))
    LiveData<List<String>> getAllCategories();
}
