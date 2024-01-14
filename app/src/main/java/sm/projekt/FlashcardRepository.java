package sm.projekt;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FlashcardRepository {
    private final FlashcardDao flashcardDao;
    private final LiveData<List<Flashcard>> flashcards;

    FlashcardRepository(Application application){
        FlashcardDatabase database = FlashcardDatabase.getDatabase(application);
        flashcardDao = database.flashcardDao();
        flashcards = flashcardDao.findAll();
    }

    LiveData<List<Flashcard>> findAllFlashcards(){ return flashcards;}

    void insert(Flashcard flashcard){
        FlashcardDatabase.databaseWriteExecutor.execute(() -> flashcardDao.insert(flashcard));
        Log.d("FlashcardRepository", "Flashcard inserted: " + flashcard.getQuestion());
        Log.d("FlashcardRepository", "Flashcard id: " + flashcard.getId());
        Log.d("FlashcardRepository", "Flashcard category: " + flashcard.getCategory());

    }

    void update(Flashcard flashcard){
        FlashcardDatabase.databaseWriteExecutor.execute(() -> flashcardDao.update(flashcard));
    }

    void delete(Flashcard flashcard){
        FlashcardDatabase.databaseWriteExecutor.execute(() -> flashcardDao.delete(flashcard));
    }
}
