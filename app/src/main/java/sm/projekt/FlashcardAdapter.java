package sm.projekt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flashcard> flashcards;

    public FlashcardAdapter(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcards.get(position);
        holder.textViewQuestion.setText(flashcard.getQuestion());
        holder.textViewAnswer.setText(flashcard.getAnswer());
        holder.textViewCategory.setText(flashcard.getCategory());
        // Add more bindings if you have more data to display
    }

    @Override
    public int getItemCount() {
        return Math.min(flashcards.size(), 50); // Limit to 10 items
    }

    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion, textViewAnswer, textViewCategory;

        FlashcardViewHolder(View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewAnswer = itemView.findViewById(R.id.textViewAnswer);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            // Initialize other views from the flashcard_item layout
        }
    }
    public void setFlashcards(List<Flashcard> newFlashcards) {
        this.flashcards = newFlashcards;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
}
