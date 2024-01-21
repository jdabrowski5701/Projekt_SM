package sm.projekt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {
    private List<Score> scores = new ArrayList<>();

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scores.get(position);
        holder.answeredCorrectlyTextView.setText("Answered Correctly: " + score.getAnsweredCorrectly());
        holder.totalFlashcardsTextView.setText("Total Flashcards: " + score.getTotalFlashcards());
        holder.categoryTextView.setText("Category: " + score.getCategory());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
        notifyDataSetChanged();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView answeredCorrectlyTextView;
        TextView totalFlashcardsTextView;
        TextView categoryTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            answeredCorrectlyTextView = itemView.findViewById(R.id.answeredCorrectlyTextView);
            totalFlashcardsTextView = itemView.findViewById(R.id.totalFlashcardsTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }
    }
}
