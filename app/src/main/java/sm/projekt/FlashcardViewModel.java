package sm.projekt;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class FlashcardViewModel extends AndroidViewModel {
    private final FlashcardRepository flashcardRepository;
    private final LiveData<List<Flashcard>> flashcards;
    private final LiveData<Score> lastScore;
    public FlashcardViewModel(@NonNull Application application){
        super(application);
        flashcardRepository = new FlashcardRepository(application);
        flashcards = flashcardRepository.findAllFlashcards();
        lastScore = flashcardRepository.getLastScore();
    }

    public LiveData<List<Flashcard>> getAllFlashcards() {
        return flashcards;
    }


    public LiveData<List<Flashcard>> getFlashcardsByCategory(String category) {
        return flashcardRepository.findFlashcardsByCategory(category);
    }

    public LiveData<List<Flashcard>> findAll(){ return flashcards; }
    public void insert(Flashcard flashcard){flashcardRepository.insert(flashcard);}
    public void update(Flashcard flashcard){flashcardRepository.update(flashcard);}
    public void delete(Flashcard flashcard){flashcardRepository.delete(flashcard);}
    public LiveData<Score> getLastScore() {
        return lastScore;
    }
    public void insertScore(Score score) {
        flashcardRepository.insertScore(score);
    }
    public LiveData<List<Score>> getAllScores() {
        return flashcardRepository.getAllScores();
    }
}
