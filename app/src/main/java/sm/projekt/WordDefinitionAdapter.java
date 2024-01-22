package sm.projekt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordDefinitionAdapter extends RecyclerView.Adapter<WordDefinitionAdapter.ViewHolder> {

    private List<Definition> definitions;

    public WordDefinitionAdapter(List<Definition> definitions) {
        this.definitions = definitions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Definition definition = definitions.get(position);

        // Ustaw tekst na TextView zgodnie z definicją
        holder.definitionTextView.setText(definition.getDefinition());
        // Ustaw kolor tekstu na biały
        holder.definitionTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
    }

    @Override
    public int getItemCount() {
        return definitions.size();
    }
    public void updateData(List<Definition> newDefinitions) {
        // Zaktualizuj dane
        definitions.clear();
        definitions.addAll(newDefinitions);

        // Powiadom adapter o zmianach
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView definitionTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            definitionTextView = itemView.findViewById(R.id.definitionTextView);
        }
    }
    public String getFirstDefinition() {
        if (definitions != null && !definitions.isEmpty()) {
            return definitions.get(0).getDefinition();
        }
        return "";
    }
}
