package sm.projekt;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomFlashcardIterator implements Iterator<Flashcard> {

    private final List<Flashcard> flashcardList;
    private int currentIndex = 0;

    public RandomFlashcardIterator(List<Flashcard> flashcards) {
        this.flashcardList = flashcards;
        Collections.shuffle(this.flashcardList, new Random());
    }

    @Override
    public boolean hasNext() {
        return currentIndex < flashcardList.size();
    }

    @Override
    public Flashcard next() {
        return flashcardList.get(currentIndex++);
    }

    public List<Flashcard> getAll() { return flashcardList;}

    public int getSizeFlashcardList() {
        return flashcardList.size();
    }
}
