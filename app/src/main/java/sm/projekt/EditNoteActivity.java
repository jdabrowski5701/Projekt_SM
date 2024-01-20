package sm.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_NOTE_TITLE = "sm.projekt.EDIT_NOTE_TITLE";
    public static final String EXTRA_EDIT_NOTE_CONTENT = "sm.projekt.EDIT_NOTE_CONTENT";
    private EditText editTitleEditText;
    private EditText editContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTitleEditText = findViewById(R.id.edit_note_title);
        editContentEditText = findViewById(R.id.edit_note_content);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EDIT_NOTE_TITLE))
            editTitleEditText.setText(intent.getStringExtra(EXTRA_EDIT_NOTE_TITLE));
        if (intent.hasExtra(EXTRA_EDIT_NOTE_CONTENT))
            editContentEditText.setText(intent.getStringExtra(EXTRA_EDIT_NOTE_CONTENT));

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if(TextUtils.isEmpty(editTitleEditText.getText()) || TextUtils.isEmpty(editContentEditText.getText())){
                setResult(RESULT_CANCELED, replyIntent);
            }else{
                String title = editTitleEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_NOTE_TITLE, title);
                String content = editContentEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_NOTE_CONTENT, content);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}