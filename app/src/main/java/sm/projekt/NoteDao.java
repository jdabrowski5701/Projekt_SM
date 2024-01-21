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
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note")
    void deleteAll();

    @Query("SELECT * FROM note ORDER BY title")
    LiveData<List<Note>> findAll();

    @Query("SELECT * FROM note WHERE title LIKE :title")
    List<Note> findBookWithTitle(String title);
}

