package sm.projekt;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FlashcardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Flashcard flashcard);

    @Update
    void update(Flashcard flashcard);

    @Delete
    void delete(Flashcard flashcard);

    @Query("DELETE FROM flashcard")
    void deleteAll();

    @Query("SELECT * FROM flashcard")
    LiveData<List<Flashcard>> findAll();

    @Query("SELECT * FROM flashcard WHERE category LIKE :category")
    List<Flashcard> findFlashcardWithCategory(String category);
}
