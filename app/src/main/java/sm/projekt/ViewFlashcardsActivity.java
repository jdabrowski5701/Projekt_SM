package sm.projekt;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewFlashcardsActivity extends AppCompatActivity {

    private FlashcardViewModel viewModel;
    private FlashcardAdapter adapter;
    private RandomFlashcardIterator flashcardIterator;
    private Flashcard currentFlashcard;
    private List<Flashcard> falseAnswered;
    private AtomicInteger flashcardsAmount;
    private int answeredCorrectly;
    private Button buttonDontKnow;
    private Button buttonKnow;
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
        falseAnswered = new ArrayList<>();
        answeredCorrectly = 0;
        flashcardsAmount = new AtomicInteger();

        viewModel.getFlashcardsByCategory(selectedCategory).observe(this, flashcards -> {
            if (flashcards != null && !flashcards.isEmpty()) {
                Log.d("ViewFlashcardsActivity", "Number of flashcards found: " + flashcards.size());
                flashcardsAmount.set(flashcards.size());
                flashcardIterator = new RandomFlashcardIterator(flashcards);
                falseAnswered = new ArrayList<>(flashcards);
                displayNextFlashcard();
            } else {
                Log.d("ViewFlashcardsActivity", "No flashcards found in category.");
            }

        });

        buttonKnow = findViewById(R.id.buttonKnow);
        buttonKnow.setOnClickListener(v -> {
            currentFlashcard.setAnsweredCorrectly(true);
            answeredCorrectly++;
            Log.d("ViewFlashcardActivity", "currentFlashcard.getAnsweredCorrectly: " + currentFlashcard.isAnsweredCorrectly());
            displayNextFlashcard();
        });

        buttonDontKnow = findViewById(R.id.buttonDontKnow);
        buttonDontKnow.setOnClickListener(v -> {
            currentFlashcard.setAnsweredCorrectly(false);
            Log.d("ViewFlashcardActivity", "currentFlashcard.getAnsweredCorrectly: " + currentFlashcard.isAnsweredCorrectly());
            displayNextFlashcard();
        });
    }


    private void displayNextFlashcard() {
        if (flashcardIterator.hasNext()) {
            currentFlashcard = flashcardIterator.next();
            adapter.setFlashcards(Collections.singletonList(currentFlashcard)); // Display one flashcard at a time
        } else {
            displayIncorrectlyAnsweredFlashcards();
        }
    }

    private void displayIncorrectlyAnsweredFlashcards() {
        List<Flashcard> tempAnsweredCorrectly= new ArrayList<>();

        // Check if there are incorrectly answered flashcards
        for(Flashcard flashcard: falseAnswered) {
            if(flashcard.isAnsweredCorrectly()) {
                tempAnsweredCorrectly.add(flashcard);
            }
        }

        falseAnswered.removeAll(tempAnsweredCorrectly);



        if (falseAnswered != null && !falseAnswered.isEmpty()){
            Log.d("ViewFlashcardsActivity", "Displaying " + falseAnswered.size() + " incorrectly answered flashcards");

            adapter.setFlashcards(falseAnswered);

            Toast.makeText(this, "You scored " + answeredCorrectly + "/" + flashcardsAmount.get() + " Learn remaining flashcards" , Toast.LENGTH_SHORT).show();


            flashcardsAmount.set(falseAnswered.size());
            flashcardIterator = new RandomFlashcardIterator(falseAnswered);
            displayNextFlashcard();

        } else {
            Toast.makeText(this,"Congratulations! You know every flashcard", Toast.LENGTH_SHORT).show();

           new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, Toast.LENGTH_SHORT); // The delay is the duration of the length short toast
        }
        answeredCorrectly=0;

    }
}
