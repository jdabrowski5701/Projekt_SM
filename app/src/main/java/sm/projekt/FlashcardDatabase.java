package sm.projekt;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Flashcard.class}, version = 1, exportSchema = false)
public abstract class FlashcardDatabase extends RoomDatabase {
    private static FlashcardDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract FlashcardDao flashcardDao();

    static FlashcardDatabase getDatabase(final Context context){
        if(databaseInstance == null){
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    FlashcardDatabase.class, "flashcard_database")
                    .build();
        }
        return databaseInstance;
    }
}
