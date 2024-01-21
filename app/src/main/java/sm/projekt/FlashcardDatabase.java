package sm.projekt;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Flashcard.class,Score.class}, version = 3, exportSchema = false)
public abstract class FlashcardDatabase extends RoomDatabase {
    private static FlashcardDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract FlashcardDao flashcardDao();
    public abstract ScoreDao scoreDao();
    static FlashcardDatabase getDatabase(final Context context){
        if(databaseInstance == null){
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    FlashcardDatabase.class, "flashcard_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseInstance;
    }
}
