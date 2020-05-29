package com.jayare.jappanese;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jayare.jappanese.adapter.DefinitionAdapter;
import com.jayare.jappanese.category.CategoryEnum;
import com.jayare.jappanese.definition.AddEditDefinitionActivity;
import com.jayare.jappanese.definition.Definition;
import com.jayare.jappanese.definition.DefinitionViewModel;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VocabularyActivity extends AppCompatActivity {
    public static final int ADD_DEFINITION_REQUEST= 1;
    public static final String ACTIVITY_CATEGORY= CategoryEnum.VOCABULARY.getCategory();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_add_definition)
    FloatingActionButton buttonAddDefinition;
    private DefinitionViewModel definitionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        ButterKnife.bind(this);

        buttonAddDefinition.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(VocabularyActivity.this, AddEditDefinitionActivity.class);
                startActivityForResult(intent,ADD_DEFINITION_REQUEST);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final DefinitionAdapter adapter = new DefinitionAdapter();
        recyclerView.setAdapter(adapter);

        definitionViewModel = new ViewModelProvider(this).get(DefinitionViewModel.class);
        definitionViewModel.getAllDefinitions().observe(this, new Observer<List<Definition>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Definition> definitions) {
                adapter.setDefinitions(definitions.stream()
                        .filter(def -> def.getCategory().equalsIgnoreCase(ACTIVITY_CATEGORY))
                        .collect(Collectors.toList()));
                Toast.makeText(VocabularyActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(VocabularyActivity.this)
                        .setTitle("Delete definition")
                        .setMessage("Do you really want to this doe?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            final RecyclerView.ViewHolder viewHolder2 = viewHolder;
                            public void onClick(DialogInterface dialog, int whichButton) {
                                definitionViewModel.delete(adapter.getDefinitionAt(viewHolder2.getAdapterPosition()));
                                Toast.makeText(VocabularyActivity.this, "Definition deleted", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_DEFINITION_REQUEST && resultCode == RESULT_OK){
            String english = data.getStringExtra(AddEditDefinitionActivity.EXTRA_ENGLISH);
            String furigana = data.getStringExtra(AddEditDefinitionActivity.EXTRA_FURIGANA);
            String japanese = data.getStringExtra(AddEditDefinitionActivity.EXTRA_JAPANESE);

            Definition definition = new Definition(english,japanese,furigana, CategoryEnum.VOCABULARY.getCategory());
            definitionViewModel.insert(definition);

            Toast.makeText(this, "Definition saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Definition not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
