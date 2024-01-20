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
    private AtomicInteger answeredCorrectly, flashcardsAmount;
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
        buttonKnow = findViewById(R.id.buttonKnow);
        answeredCorrectly= new AtomicInteger();
        flashcardsAmount = new AtomicInteger();
        buttonKnow.setOnClickListener(v -> {
            currentFlashcard.setAnsweredCorrectly(true);
            Log.d("ViewFlashcardActivity", "currentFlashcard.getAnsweredCorrectly: " + currentFlashcard.isAnsweredCorrectly());
            displayNextFlashcard();
        });



        buttonDontKnow = findViewById(R.id.buttonDontKnow);
        buttonDontKnow.setOnClickListener(v -> {
            currentFlashcard.setAnsweredCorrectly(false);
            Log.d("ViewFlashcardActivity", "currentFlashcard.getAnsweredCorrectly: " + currentFlashcard.isAnsweredCorrectly());
            falseAnswered.add(currentFlashcard);
            answeredCorrectly.decrementAndGet();
            displayNextFlashcard();
        });
        viewModel.getFlashcardsByCategory(selectedCategory).observe(this, flashcards -> {
            if (flashcards != null && !flashcards.isEmpty()) {
                Log.d("ViewFlashcardsActivity", "Number of flashcards found: " + flashcards.size());
                flashcardsAmount.set(flashcards.size());
                answeredCorrectly.set(flashcards.size());
                flashcardIterator = new RandomFlashcardIterator(flashcards);
                displayNextFlashcard();
            } else {
                Log.d("ViewFlashcardsActivity", "No flashcards found in category.");
            }

        });
    }

    /*private void displayNextFlashcard() {
        if(falseAnswered!=null) {
            for (Flashcard flashcard : falseAnswered) {
                Log.d("ViewFlashcardActivity", "flashcard answer: " + flashcard.getAnswer() );
            }
        }


        if (flashcardIterator != null && flashcardIterator.hasNext()) {
            currentFlashcard = flashcardIterator.next();
            adapter.setFlashcards(Collections.singletonList(currentFlashcard)); // Display one flashcard at a time
        }
    }*/
    private void displayNextFlashcard() {
        if (flashcardIterator.hasNext()) {
            currentFlashcard = flashcardIterator.next();
            adapter.setFlashcards(Collections.singletonList(currentFlashcard)); // Display one flashcard at a time
        } else {
            // No more flashcards to display, so display the ones marked as incorrect
            displayIncorrectlyAnsweredFlashcards();
        }
    }

    private void displayIncorrectlyAnsweredFlashcards() {
        // Check if there are incorrectly answered flashcards
        if (falseAnswered != null && !falseAnswered.isEmpty()){
            // Update the adapter with the list of incorrectly answered flashcards

            Log.d("ViewFlashcardsActivity", "Displaying " + falseAnswered.size() + " incorrectly answered flashcards");

            adapter.setFlashcards(falseAnswered);

            // Optionally, disable or hide the 'Know' and 'Don't Know' buttons
            //buttonKnow.setVisibility(View.GONE);
           // buttonDontKnow.setVisibility(View.GONE);

            // Optionally, show a message or update the UI to indicate the test has ended
            // Example: Show a Toast message
            Toast.makeText(this, "You scored " + answeredCorrectly.get() + "/" + flashcardsAmount.get() , Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Review these flashcards", Toast.LENGTH_SHORT).show();

            flashcardsAmount.set(falseAnswered.size());
            answeredCorrectly.set(falseAnswered.size());
            flashcardIterator = new RandomFlashcardIterator(falseAnswered);
            displayNextFlashcard();

        } else {
            // No incorrectly answered flashcards, handle accordingly
            // Example: Show a Toast message
            Toast.makeText(this,"Congratulations! You know every flashcard", Toast.LENGTH_SHORT).show();

            // This handler will allow the toast to be shown for a little while before finishing the activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Will close this activity and go back to MainActivity
                    finish();
                }
            }, Toast.LENGTH_SHORT); // The delay is the duration of the length short toast
        }

    }


    // Add a method or button click listener to call displayNextFlashcard()
}
