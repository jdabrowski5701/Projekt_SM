package sm.projekt;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcard")
public class Flashcard {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String question;
    private String answer;
    private String category;
    private boolean isAnsweredCorrectly = false;


    //Gettery i Settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAnsweredCorrectly() {
        return isAnsweredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        isAnsweredCorrectly = answeredCorrectly;
    }
}
