package com.jayare.jappanese.category;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jayare.jappanese.definition.AddEditDefinitionActivity;
import com.jayare.jappanese.definition.DefinitionViewModel;
import com.jayare.jappanese.R;
import com.jayare.jappanese.adapter.CategoryAdapter;
import com.jayare.jappanese.definition.Definition;
import com.jayare.jappanese.definition.DefinitionListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategorySelectionActivity extends AppCompatActivity implements CategoryAdapter.OnItemListener {
    @BindView(R.id.recycler_view_category)
    RecyclerView recyclerView;
    @BindView(R.id.btn_add_definition_with_category)
    FloatingActionButton buttonAddDefinition;

    private static final String TAG = "CategorySelectionActivi";
    public static final int ADD_DEFINITION_REQUEST = 1;
    public static final String EXTRA_CATEGORY = "com.jayare.jappanesev3.EXTRA_CATEGORY";
    private DefinitionViewModel definitionViewModel;
    private List<String> categories;
    CategoryAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_selection_activity);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(this);
        recyclerView.setAdapter(adapter);

        /*buttonAddDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategorySelectionActivity.this, AddDefinitionActivity.class);
                startActivityForResult(intent, ADD_DEFINITION_REQUEST);
            }
        });*/

        definitionViewModel = new ViewModelProvider(this).get(DefinitionViewModel.class);
        definitionViewModel.getAllCategories().observe(this, new Observer<List<String>>() {
            CategorySelectionActivity categorySelectionActivity = new CategorySelectionActivity();

            @Override
            public void onChanged(List<String> categories) {
                adapter.setCategories(categories);
                categorySelectionActivity.setCategories(categories);
            }
        });

    }

    @OnClick(R.id.btn_add_definition_with_category)
    public void AddDefinition(View view) {
        Intent intent = new Intent(CategorySelectionActivity.this, AddEditDefinitionActivity.class);
        startActivityForResult(intent, ADD_DEFINITION_REQUEST);
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = null;

        //TODO change cases to take whatever is in the list instead of preset list of categories
        switch (adapter.getCategoryAt(position)) {
            case "Vocabulary":
                intent = new Intent(this, DefinitionListActivity.class);
                intent.putExtra(EXTRA_CATEGORY, CategoryEnum.VOCABULARY.getCategory());
                break;
            case "Phrases":
                intent = new Intent(this, DefinitionListActivity.class);
                intent.putExtra(EXTRA_CATEGORY, CategoryEnum.PHRASES.getCategory());
                break;
            case "Food":
                intent = new Intent(this, DefinitionListActivity.class);
                intent.putExtra(EXTRA_CATEGORY, CategoryEnum.FOOD.getCategory());
                break;
            case "Slang":
                intent = new Intent(this, DefinitionListActivity.class);
                intent.putExtra(EXTRA_CATEGORY, CategoryEnum.SLANG.getCategory());
                break;
            case "Directions":
                intent = new Intent(this, DefinitionListActivity.class);
                intent.putExtra(EXTRA_CATEGORY, CategoryEnum.DIRECTIONS.getCategory());
                break;
            default:
                Toast.makeText(this, "Not a valid category.", Toast.LENGTH_SHORT).show();
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_DEFINITION_REQUEST && resultCode == RESULT_OK) {
            String english = data.getStringExtra(AddEditDefinitionActivity.EXTRA_ENGLISH);
            String furigana = data.getStringExtra(AddEditDefinitionActivity.EXTRA_FURIGANA);
            String japanese = data.getStringExtra(AddEditDefinitionActivity.EXTRA_JAPANESE);
            String category = data.getStringExtra(AddEditDefinitionActivity.EXTRA_CATEGORY);
            Definition definition = new Definition(english, japanese, furigana, category);
            definitionViewModel.insert(definition);

            Toast.makeText(this, "Definition saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Definition not saved", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO add a remove category to menu options?
}
