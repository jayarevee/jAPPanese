package com.jayare.jappanese.definition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jayare.jappanese.category.CategoryEnum;
import com.jayare.jappanese.category.CategorySelectionActivity;
import com.jayare.jappanese.R;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDefinitionActivity extends AppCompatActivity {
    private static final String TAG = "AddDefinitionActivity";
    @BindView(R.id.edit_text_english)
    EditText editTextEnglish;
    @BindView(R.id.edit_text_furigana)
    EditText editTextFurigana;
    @BindView(R.id.edit_text_japanese)
    EditText editTextJapanese;

    private String callingActivityName;
    private EditText editTextCategory;
    LinearLayout layout;

    public static final String EXTRA_ENGLISH =
            "com.jayare.jappanesev3.EXTRA_ENGLISH";

    public static final String EXTRA_FURIGANA =
            "com.jayare.jappanesev3.EXTRA_FURIGANA";

    public static final String EXTRA_JAPANESE =
            "com.jayare.jappanesev3.EXTRA_JAPANESE";

    public static final String EXTRA_CATEGORY =
            "com.jayare.japanesev3.ADD_EXTRA_CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_definition);

        ButterKnife.bind(this);
        callingActivityName = this.getCallingActivity().getClassName();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Definition");

        /**
         * Dynamically add category edit text if this is called from {@link CategorySelectionActivity}
         */
        layout = (LinearLayout) findViewById(R.id.add_definition_layout);

        if (callingActivityName.equalsIgnoreCase(CategorySelectionActivity.class.getName())) {
            //Add Category EditText
            editTextCategory = new EditText(this);
            editTextCategory.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            editTextCategory.setHint("Insert category");
            layout.addView(editTextCategory, 3);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveDefinition() {
        String english = editTextEnglish.getText().toString();
        String furigana = editTextFurigana.getText().toString();
        String japanese = editTextJapanese.getText().toString();
        String category = callingActivityName.equalsIgnoreCase(CategorySelectionActivity.class.getName())
                ? editTextCategory.getText().toString() : null;

        if (english.trim().isEmpty() || japanese.trim().isEmpty()) {
            Toast.makeText(this, "Enter a valid definition!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (callingActivityName.equalsIgnoreCase(CategorySelectionActivity.class.getName())
                && !isValidCategory(category)) {
            Toast.makeText(this, "Enter a valid category!", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         send this data to the calling activity {@link DefinitionListActivity}
         or {@link CategorySelectionActivity}
         */

        Intent data = new Intent();
        data.putExtra(EXTRA_ENGLISH, english);
        data.putExtra(EXTRA_FURIGANA, furigana);
        data.putExtra(EXTRA_JAPANESE, japanese);

        if (callingActivityName.equalsIgnoreCase(CategorySelectionActivity.class.getName())) {
            data.putExtra(EXTRA_CATEGORY, category);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_definition_menu, menu);
        //return true to show menu false for not
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_definition:
                saveDefinition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isValidCategory(String category) {
        if (category.trim().isEmpty() || category == null) {
            return false;
        }
        List<String> categories = Stream.of(CategoryEnum.values())
                .map(CategoryEnum::getCategory)
                .collect(Collectors.toList());
        for (String s : categories) {
            Log.d(TAG, "isValidCategory: " + s);
        }
        if (categories.contains(category)) {
            return true;
        }
        return false;
    }

}
