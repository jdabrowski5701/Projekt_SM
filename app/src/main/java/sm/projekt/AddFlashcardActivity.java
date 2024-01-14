package sm.projekt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddFlashcardActivity extends AppCompatActivity {

    private EditText editTextQuestion, editTextAnswer;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard); // Upewnij się, że używasz właściwego ID layoutu

        // Inicjalizacja komponentów interfejsu użytkownika
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        buttonSave = findViewById(R.id.buttonSave);

        // Ustawienie słuchacza kliknięć dla przycisku zapisu
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFlashcard();
            }
        });
    }

    private void saveFlashcard() {
        // Pobranie danych z pól tekstowych
        String question = editTextQuestion.getText().toString().trim();
        String answer = editTextAnswer.getText().toString().trim();

        // Walidacja danych
        if (question.isEmpty() || answer.isEmpty()) {
            // Pokaż komunikat błędu, jeśli pytanie lub odpowiedź są puste
            // Toast.makeText(this, "Proszę wprowadzić pytanie i odpowiedź", Toast.LENGTH_SHORT).show();
            return;
        }

        // Utworzenie obiektu Flashcard
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(question);
        flashcard.setAnswer(answer);

        // Zapis fiszki w bazie danych
        FlashcardRepository repository = new FlashcardRepository(getApplication());
        repository.insert(flashcard);

        // Opcjonalnie: Zamknięcie aktywności po zapisaniu fiszki
        finish();
    }
}
