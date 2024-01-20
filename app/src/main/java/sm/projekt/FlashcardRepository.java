package sm.projekt;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class FlashcardRepository {
    private final FlashcardDao flashcardDao;
    private final LiveData<List<Flashcard>> flashcards;

    FlashcardRepository(Application application){
        FlashcardDatabase database = FlashcardDatabase.getDatabase(application);
        flashcardDao = database.flashcardDao();
        flashcards = flashcardDao.findAll();
    }

    public RandomFlashcardIterator getRandomFlashcardIteratorByCategory(String category) {
        LiveData<List<Flashcard>> flashcardsLiveData = findFlashcardsByCategory(category);

        Log.d("FlashcardRepository", "getRandomFlashcardIterator");

        List<Flashcard> flashcards = flashcardsLiveData.getValue();
        if (flashcards == null) {
            flashcards = new ArrayList<>();
            Log.d("FlashcardRepository", "No flashcards found in category.");
        } else {
            Log.d("FlashcardRepository", "Number of flashcards found: " + flashcards.size());
        }
        return new RandomFlashcardIterator(flashcards);
    }

    LiveData<List<Flashcard>> findAllFlashcards(){
        Log.d("FlashcardRepository", "findAllFlashcards");
         return flashcards;
       // return flashcardDao.findAll();
         }
    public LiveData<List<Flashcard>> findFlashcardsByCategory(String category) {
        MutableLiveData<List<Flashcard>> liveData = new MutableLiveData<>();

        FlashcardDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<Flashcard> flashcards = flashcardDao.findFlashcardWithCategory(category);
                liveData.postValue(flashcards);
            } catch (Exception e) {
                Log.e("FlashcardRepository", "Error finding flashcards by category", e);
                liveData.postValue(new ArrayList<>()); // Return an empty list in case of error
            }
        });

        return liveData;
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
