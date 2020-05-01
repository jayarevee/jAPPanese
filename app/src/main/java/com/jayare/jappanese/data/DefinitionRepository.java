package com.jayare.jappanese.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jayare.jappanese.definition.Definition;

import java.util.List;

//Uses @{DefinitionDao} interface to get data from the SQLite Db as a List of LiveData
public class DefinitionRepository {

    private DefinitionDao definitionDao;
    private LiveData<List<Definition>> allDefinitions;
    private LiveData<List<String>> allCategories;

    public DefinitionRepository(Application application){
        DefinitionDatabase database = DefinitionDatabase.getInstance(application);
        definitionDao = database.definitionDao();
        allDefinitions = definitionDao.getAllDefinitions();
        allCategories = definitionDao.getAllCategories();
    }

    public void insert(Definition definition){
        new InsertDefinitionAsyncTask(definitionDao).execute(definition);
    }

    public void update(Definition definition){
        new UpdateDefinitionAsyncTask(definitionDao).execute(definition);
    }

    public void delete(Definition definition){
        new DeleteDefinitionAsyncTask(definitionDao).execute(definition);
    }

    public LiveData<List<Definition>> getAllDefinitions(){
        return allDefinitions;
    }

    public LiveData<List<String>> getAllCategories() {
        return allCategories;
    }

    private static class InsertDefinitionAsyncTask extends AsyncTask<Definition,Void,Void>{
        private DefinitionDao definitionDao;

        private InsertDefinitionAsyncTask(DefinitionDao definitionDao){
            this.definitionDao = definitionDao;
        }
        @Override
        protected Void doInBackground(Definition... definitions) {
            definitionDao.insert(definitions[0]);
            return null;
        }
    }

    private static class UpdateDefinitionAsyncTask extends AsyncTask<Definition,Void,Void>{
        private DefinitionDao definitionDao;

        private UpdateDefinitionAsyncTask(DefinitionDao definitionDao){
            this.definitionDao = definitionDao;
        }
        @Override
        protected Void doInBackground(Definition... definitions) {
            definitionDao.update(definitions[0]);
            return null;
        }
    }

    private static class DeleteDefinitionAsyncTask extends AsyncTask<Definition,Void,Void>{
        private DefinitionDao definitionDao;

        private DeleteDefinitionAsyncTask(DefinitionDao definitionDao){
            this.definitionDao = definitionDao;
        }
        @Override
        protected Void doInBackground(Definition... definitions) {
            definitionDao.delete(definitions[0]);
            return null;
        }
    }


}
