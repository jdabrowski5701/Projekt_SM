package sm.projekt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

public class ViewFlashcardsActivity extends AppCompatActivity {

    private FlashcardViewModel viewModel;
    private FlashcardAdapter adapter;
    private RandomFlashcardIterator flashcardIterator;
    private Flashcard currentFlashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flashcards);

        String selectedCategory = getIntent().getStringExtra("SELECTED_CATEGORY");


        RecyclerView recyclerView = findViewById(R.id.recyclerViewFlashcards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);
        adapter = new FlashcardAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);

        Button buttonKnow = findViewById(R.id.buttonKnow);
        buttonKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFlashcard.setAnsweredCorrectly(true);
                Log.d("ViewFlashcardActivity", "currentFlashcard.getAnsweredCorrectly: " + currentFlashcard.isAnsweredCorrectly());
                displayNextFlashcard();
            }
        });


        Button buttonDontKnow = findViewById(R.id.buttonDontKnow);
        buttonDontKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFlashcard.setAnsweredCorrectly(false);
                Log.d("ViewFlashcardActivity", "currentFlashcard.getAnsweredCorrectly: " + currentFlashcard.isAnsweredCorrectly());
                displayNextFlashcard();
            }
        });
        viewModel.getFlashcardsByCategory(selectedCategory).observe(this, flashcards -> {
            if (flashcards != null && !flashcards.isEmpty()) {
                Log.d("ViewFlashcardsActivity", "Number of flashcards found: " + flashcards.size());
                flashcardIterator = new RandomFlashcardIterator(flashcards);
                displayNextFlashcard();
            } else {
                Log.d("ViewFlashcardsActivity", "No flashcards found in category.");
            }
        });
    }

    private void displayNextFlashcard() {
        if (flashcardIterator != null && flashcardIterator.hasNext()) {
            currentFlashcard = flashcardIterator.next();
            adapter.setFlashcards(Collections.singletonList(currentFlashcard)); // Display one flashcard at a time
        }
    }

    // Add a method or button click listener to call displayNextFlashcard()
}
