package sm.projekt;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void insert(Score score);

    @Query("SELECT * FROM scores ORDER BY id DESC LIMIT 1")
    LiveData<Score> getLastScore();

    @Query("SELECT * FROM scores")
    LiveData<List<Score>>getAllScores();
}

