package sm.projekt;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewFlashcardsActivity extends AppCompatActivity {

    private FlashcardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flashcards);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFlashcards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FlashcardViewModel viewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);

        // Initialize your adapter here and set it to the RecyclerView
        adapter = new FlashcardAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Observe LiveData from ViewModel
        viewModel.findAll().observe(this, new Observer<List<Flashcard>>() {
            @Override
            public void onChanged(List<Flashcard> flashcards) {
                // Update the adapter with the new list
                adapter.setFlashcards(flashcards);
            }
        });
    }
}



