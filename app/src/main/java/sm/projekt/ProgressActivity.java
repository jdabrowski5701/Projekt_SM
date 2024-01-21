package sm.projekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ProgressActivity extends AppCompatActivity {

   private RecyclerView scoresRecyclerView;
    private ScoresAdapter scoresAdapter;
    private FlashcardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        scoresRecyclerView = findViewById(R.id.scoresRecyclerView);
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        scoresAdapter = new ScoresAdapter();
        scoresRecyclerView.setAdapter(scoresAdapter);
       viewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);
        viewModel.getAllScores().observe(this, scores -> {
                scoresAdapter.setScores(scores);
        });
    }


}
