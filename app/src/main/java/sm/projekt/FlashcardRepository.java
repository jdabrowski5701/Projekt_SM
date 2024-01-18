package sm.projekt;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class FlashcardRepository {
    private final FlashcardDao flashcardDao;
    private final LiveData<List<Flashcard>> flashcards;

    FlashcardRepository(Application application){
        FlashcardDatabase database = FlashcardDatabase.getDatabase(application);
        flashcardDao = database.flashcardDao();
        flashcards = flashcardDao.findAll();
    }

    public RandomFlashcardIterator getRandomFlashcardIterator() {
        List<Flashcard> flashcards = findAllFlashcards().getValue();
        if (flashcards == null) flashcards = new ArrayList<>();
        return new RandomFlashcardIterator(flashcards);
    }

    LiveData<List<Flashcard>> findAllFlashcards(){
        Log.d("FlashcardRepository", "findAllFlashcards");
         return flashcards;
       // return flashcardDao.findAll();
         }

  /*  void insert(Flashcard flashcard){
        FlashcardDatabase.databaseWriteExecutor.execute(() -> flashcardDao.insert(flashcard));
        Log.d("FlashcardRepository", "Flashcard inserted: " + flashcard.getQuestion());
        //Log.d("FlashcardRepository", "Flashcard id: " + flashcardDao.insert(flashcard));
        Log.d("FlashcardRepository", "Flashcard category: " + flashcard.getCategory());
        long id = FlashcardDatabase.databaseWriteExecutor.submit(() -> flashcardDao.insert(flashcard)).get();
        Log.d("FlashcardRepository", "Flashcard inserted ID: " + id);


    }*/
  void insert(Flashcard flashcard){
      FlashcardDatabase.databaseWriteExecutor.execute(() -> {
          try {
              long id = flashcardDao.insert(flashcard);
              Log.d("FlashcardRepository", "Flashcard inserted ID: " + id);
              Log.d("FlashcardRepository", "Flashcard question: " + flashcard.getQuestion());
              Log.d("FlashcardRepository", "Flashcard answer: " + flashcard.getAnswer());
              Log.d("FlashcardRepository", "Flashcard category " + flashcard.getCategory());
          } catch (Exception e) {
              Log.e("FlashcardRepository", "Error inserting flashcard", e);
          }
      });
  }


    void update(Flashcard flashcard){
        FlashcardDatabase.databaseWriteExecutor.execute(() -> flashcardDao.update(flashcard));
    }

    void delete(Flashcard flashcard){
        FlashcardDatabase.databaseWriteExecutor.execute(() -> flashcardDao.delete(flashcard));
    }

  /*  public LiveData<List<Flashcard>> getAllFlashcards() {

        Log.d("FlashcardRepository", "getAllFlashcards()");
        return flashcardDao.findAll();
    }*/

}
