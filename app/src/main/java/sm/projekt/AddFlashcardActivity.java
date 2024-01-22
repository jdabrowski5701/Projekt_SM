package sm.projekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddFlashcardActivity extends AppCompatActivity {

    private EditText editTextQuestion, editTextAnswer;
    private Spinner spinnerCategory;
    private Button buttonSave;
    private Flashcard prototypeFlashcard;

    private AdapterView.OnItemSelectedListener OnCatSpinnerCL = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            int hintColor = getResources().getColor(R.color.hint_color);
            ((TextView) parent.getChildAt(0)).setTextColor(hintColor);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Optional: handle case where nothing is selected
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        String definition = intent.getStringExtra("definition");

        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        prototypeFlashcard = new Flashcard();

        // Ustaw dane w polach
        editTextQuestion.setText(word);
        editTextAnswer.setText(definition);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(OnCatSpinnerCL);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFlashcard();
            }
        });
    }

    private void saveFlashcard() {
        String question = editTextQuestion.getText().toString().trim();
        String answer = editTextAnswer.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (question.isEmpty() || answer.isEmpty()) {
            // Handle empty inputs
            return;
        }

        Flashcard flashcard = prototypeFlashcard.clone();
        flashcard.setQuestion(question);
        flashcard.setAnswer(answer);
        flashcard.setCategory(category);

        FlashcardRepository repository = new FlashcardRepository(getApplication());
        repository.insert(flashcard);

        finish(); // Close the activity
    }
}
