package sm.projekt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    }

    @Override
    public int getItemCount() {
        return Math.min(flashcards.size(), 50);
    }

    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion, textViewAnswer;
        FlashcardViewHolder(View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewAnswer = itemView.findViewById(R.id.textViewAnswer);
            textViewAnswer.setVisibility(View.GONE);

           // odwracanie fiszki
            textViewQuestion.setOnClickListener(v -> {
                boolean isAnswerVisible = textViewAnswer.getVisibility() == View.VISIBLE;
                textViewQuestion.setVisibility(isAnswerVisible ? View.VISIBLE : View.GONE);
                textViewAnswer.setVisibility(isAnswerVisible ? View.GONE : View.VISIBLE);
            });
            textViewAnswer.setOnClickListener(v -> {
                boolean isAnswerVisible = textViewAnswer.getVisibility() == View.VISIBLE;
                textViewQuestion.setVisibility(isAnswerVisible ? View.VISIBLE : View.GONE);
                textViewAnswer.setVisibility(isAnswerVisible ? View.GONE : View.VISIBLE);
            });
        }
    }
    public void setFlashcards(List<Flashcard> newFlashcards) {
        this.flashcards = newFlashcards;
        notifyDataSetChanged();
    }
}
