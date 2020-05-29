package com.jayare.jappanese.definition;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jayare.jappanese.category.CategorySelectionActivity;
import com.jayare.jappanese.adapter.DefinitionAdapter;
import com.jayare.jappanese.R;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefinitionListActivity extends AppCompatActivity {
    public static final int ADD_DEFINITION_REQUEST = 1;
    public static final int EDIT_DEFINITION_REQUEST = 2;
    public String ACTIVITY_CATEGORY;
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

        /**
         Take the Category from the {@link CategorySelectionActivity}
         */
        Intent intent = getIntent();
        ACTIVITY_CATEGORY = intent.getStringExtra(CategorySelectionActivity.EXTRA_CATEGORY);
        buttonAddDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DefinitionListActivity.this, AddEditDefinitionActivity.class);
                startActivityForResult(intent, ADD_DEFINITION_REQUEST);
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
                Toast.makeText(DefinitionListActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
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
                new AlertDialog.Builder(DefinitionListActivity.this)
                        .setTitle("Delete definition")
                        .setMessage("Do you really want to this doe?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            final RecyclerView.ViewHolder viewHolder2 = viewHolder;

                            public void onClick(DialogInterface dialog, int whichButton) {
                                definitionViewModel.delete(adapter.getDefinitionAt(viewHolder2.getAdapterPosition()));
                                Toast.makeText(DefinitionListActivity.this, "Definition deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new DefinitionAdapter.OnItemClickListener() {
                                           @Override
                                           public void onItemClick(Definition definition) {
                                               Intent intent = new Intent(DefinitionListActivity.this, AddEditDefinitionActivity.class);
                                               intent.putExtra(AddEditDefinitionActivity.EXTRA_ID, definition.getId());
                                               intent.putExtra(AddEditDefinitionActivity.EXTRA_JAPANESE, definition.getJapanese());
                                               intent.putExtra(AddEditDefinitionActivity.EXTRA_ENGLISH, definition.getEnglish());
                                               intent.putExtra(AddEditDefinitionActivity.EXTRA_FURIGANA, definition.getFurigana());
                                               intent.putExtra(AddEditDefinitionActivity.EXTRA_CATEGORY, definition.getCategory());
                                               startActivityForResult(intent, EDIT_DEFINITION_REQUEST);
                                           }
                                       }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_DEFINITION_REQUEST && resultCode == RESULT_OK) {
            String english = data.getStringExtra(AddEditDefinitionActivity.EXTRA_ENGLISH);
            String furigana = data.getStringExtra(AddEditDefinitionActivity.EXTRA_FURIGANA);
            String japanese = data.getStringExtra(AddEditDefinitionActivity.EXTRA_JAPANESE);

            /**
             Take the Category from the {@link CategorySelectionActivity} and set it here
             */
            Definition definition = new Definition(english, japanese, furigana, ACTIVITY_CATEGORY);
            definitionViewModel.insert(definition);

            Toast.makeText(this, "Definition saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_DEFINITION_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditDefinitionActivity.EXTRA_ID,-1);

            if(id == -1){
                Toast.makeText(this, "Definition cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String english = data.getStringExtra(AddEditDefinitionActivity.EXTRA_ENGLISH);
            String furigana = data.getStringExtra(AddEditDefinitionActivity.EXTRA_FURIGANA);
            String japanese = data.getStringExtra(AddEditDefinitionActivity.EXTRA_JAPANESE);
            String category = data.getStringExtra(AddEditDefinitionActivity.EXTRA_CATEGORY);
            Definition definition = new Definition(english, japanese, furigana, category);
            definition.setId(id);

            definitionViewModel.update(definition);

            Toast.makeText(this, "Definition updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Definition not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
