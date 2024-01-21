package sm.projekt;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scores")
public class Score {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int answeredCorrectly;
    private int totalFlashcards;
    private String Category;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(int answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }

    public int getTotalFlashcards() {
        return totalFlashcards;
    }

    public void setTotalFlashcards(int totalFlashcards) {
        this.totalFlashcards = totalFlashcards;
    }



    // Getters and setters...
}

