package sm.projekt;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

public class ViewFlashcardsActivity extends AppCompatActivity {

    private FlashcardViewModel viewModel;
    private FlashcardAdapter adapter;
    private RandomFlashcardIterator flashcardIterator;

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

        viewModel.getAllFlashcards().observe(this, flashcards -> {
            flashcardIterator = viewModel.getRandomFlashcardIterator();
            displayNextFlashcard();
        });
    }

    private void displayNextFlashcard() {
        if (flashcardIterator != null && flashcardIterator.hasNext()) {
            Flashcard flashcard = flashcardIterator.next();
            adapter.setFlashcards(Collections.singletonList(flashcard)); // Display one flashcard at a time
        }
    }

    // Add a method or button click listener to call displayNextFlashcard()
}
