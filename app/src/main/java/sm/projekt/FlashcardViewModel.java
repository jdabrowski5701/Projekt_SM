package sm.projekt;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FlashcardViewModel extends AndroidViewModel {
    private final FlashcardRepository flashcardRepository;
    private final LiveData<List<Flashcard>> flashcards;

    public FlashcardViewModel(@NonNull Application application){
        super(application);
        flashcardRepository = new FlashcardRepository(application);
        flashcards = flashcardRepository.findAllFlashcards();
    }
    LiveData<List<Flashcard>> getAllFlashcards() {
        return flashcards;
    }

    RandomFlashcardIterator getRandomFlashcardIterator() {
        return flashcardRepository.getRandomFlashcardIterator();
    }

  /*  public LiveData<List<Flashcard>> getFlashcards() {
        return flashcardRepository.getAllFlashcards();
    }*/
    public LiveData<List<Flashcard>> findAll(){ return flashcards; }
    public void insert(Flashcard flashcard){flashcardRepository.insert(flashcard);}
    public void update(Flashcard flashcard){flashcardRepository.update(flashcard);}
    public void delete(Flashcard flashcard){flashcardRepository.delete(flashcard);}
}
