package sm.projekt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sm.projekt.GameView;

public class GameActivity extends AppCompatActivity {

    private FlashcardRepository flashcardRepository;
    private TextView currentWord;
    private TextView leftWord;
    private TextView rightWord;
    private LiveData<List<Flashcard>> flashcardsCopy;
    private LiveData<List<Flashcard>> flashcardsLiveData;
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        showInstructionPopup();

        flashcardRepository = new FlashcardRepository(this.getApplication());
        currentWord = findViewById(R.id.word);
        leftWord = findViewById(R.id.leftWord);
        rightWord = findViewById(R.id.rightWord);

        flashcardsLiveData = flashcardRepository.findAllFlashcards();
        flashcardsCopy = flashcardsLiveData;
        loadFlashcards();
        gameView = new GameView(this);

        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        frameLayout.addView(gameView);
        gameView.setOnBallReachEndListener(new GameView.OnBallReachEndListener() {
            @Override
            public void onBallReachEnd() {

                loadNextFlashcards();
            }
        });
    }

    private void showInstructionPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How to Play"); // Set the title of the popup

        // Inflate the instruction_popup.xml layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.instruction_popup, null);

        // Set the content of the popup to the inflated layout
        builder.setView(dialogView);

        // Add a button to close the popup
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close the popup
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadFlashcards() {
        // Observe the LiveData to get updates when flashcards change
        flashcardsCopy.observe(this, new Observer<List<Flashcard>>() {
            @Override
            public void onChanged(List<Flashcard> flashcards) {
                // Handle the list of flashcards here
                if (flashcards != null && !flashcards.isEmpty()) {
                    // Flashcards are available, you can use them in your game
                    // For example, you can start the game with the first flashcard
                    Flashcard firstFlashcard = flashcards.get(0);

                    currentWord.setText(firstFlashcard.getQuestion());

                    Random random = new Random();
                    int questionNumber = random.nextInt(flashcards.size() - 1)+1;
                    boolean isLeft = random.nextBoolean();
                    if (isLeft) {
                        leftWord.setText(firstFlashcard.getAnswer());
                        rightWord.setText(flashcards.get(questionNumber).getAnswer());
                    } else {
                        rightWord.setText(firstFlashcard.getAnswer());
                        leftWord.setText(flashcards.get(questionNumber).getAnswer());
                    }
                    usedFlashcards.add(firstFlashcard);
                }
            }
        });
    }

    private List<Flashcard> usedFlashcards = new ArrayList<>();
    private Random random = new Random();
    private int i=5;

    //when the ball falls of for the first time, the left and right word do not change
    private void loadNextFlashcards() {
        List<Flashcard> flashcards = flashcardsLiveData.getValue();
        if (flashcards == null || flashcards.isEmpty()) {
            return;
        }

        Flashcard newCurrentFlashcard = getNextFlashcard(flashcards);
        if (newCurrentFlashcard == null) {
            Toast.makeText(this,"To były wszystie słowa", Toast.LENGTH_SHORT).show();
            return;
        }
        //Flashcard leftFlashcard
        //Random random = new Random();
        int questionNumber = random.nextInt(flashcards.size() - 1)+1;
        boolean isLeft = random.nextBoolean();
        if (isLeft) {
            leftWord.setText(newCurrentFlashcard.getAnswer());
            rightWord.setText(flashcards.get(random.nextInt(flashcards.size())).getAnswer());
        } else {
            rightWord.setText(newCurrentFlashcard.getAnswer());
            leftWord.setText(flashcards.get(random.nextInt(flashcards.size())).getAnswer());
        }
//        Flashcard leftFlashcard = flashcards.get(random.nextInt(flashcards.size()));
//        Flashcard rightFlashcard;
//        do {
//            rightFlashcard = flashcards.get(random.nextInt(flashcards.size()));
//        } while (rightFlashcard.equals(leftFlashcard));

        // Now update the TextViews
        currentWord.setText(newCurrentFlashcard.getQuestion());
//        leftWord.setText(leftFlashcard.getAnswer());
//        rightWord.setText(rightFlashcard.getAnswer());

        // Optionally, you can add the newCurrentFlashcard to the list of used flashcards
        usedFlashcards.add(newCurrentFlashcard);

        int newSpeed = gameView.getBallSpeed() + 1;//2;
        gameView.setBallSpeed(newSpeed);

        gameView.setBall1X(i);
        gameView.setBall2X(i);
        gameView.checkX();
        //i+=1;
    }

    private Flashcard getNextFlashcard(List<Flashcard> flashcards) {
        Flashcard nextFlashcard;
        if (usedFlashcards.size() == flashcards.size()) return null;
        do {
            nextFlashcard = flashcards.get(random.nextInt(flashcards.size()));
        } while (usedFlashcards.contains(nextFlashcard) && usedFlashcards.size()<flashcards.size());

        return nextFlashcard;
    }

}
