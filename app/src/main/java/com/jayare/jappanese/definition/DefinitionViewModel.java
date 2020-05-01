package com.jayare.jappanese.definition;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jayare.jappanese.data.DefinitionRepository;
import com.jayare.jappanese.definition.Definition;

import java.util.List;

/**
 * retrieves livedata from {@link DefinitionRepository} and caches it to be used by the Activity
 * (Doesn't store just observes the liveData)
 */

public class DefinitionViewModel extends AndroidViewModel {
    private DefinitionRepository repository;
    private LiveData<List<Definition>> allDefinitions;
    private LiveData<List<String>> allCategories;

    public DefinitionViewModel(@NonNull Application application) {
        super(application);
        /**
        need to pass category some other way or a new repo will be created every time
        maybe get all the definitions and have the view separate by category
        */
        repository = new DefinitionRepository(application);
        allDefinitions = repository.getAllDefinitions();
        allCategories = repository.getAllCategories();
    }

    public void insert(Definition definition) {
        repository.insert(definition);
    }

    public void update(Definition definition) {
        repository.update(definition);
    }

    public void delete(Definition definition) {
        repository.delete(definition);
    }

    public LiveData<List<Definition>> getAllDefinitions() {
        return allDefinitions;
    }

    public LiveData<List<String>> getAllCategories() {
        return allCategories;
    }
}
