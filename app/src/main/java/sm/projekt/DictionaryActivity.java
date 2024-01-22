package sm.projekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;

public class DictionaryActivity extends AppCompatActivity {

    private EditText editTextWord;
    private Button buttonShowDefinition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        editTextWord = findViewById(R.id.editTextWord);
        buttonShowDefinition = findViewById(R.id.buttonShowDefinition);
        Button buttonCreateFlashcard = findViewById(R.id.buttonCreateFlashcard);

        buttonShowDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefinition();
            }
        });
    }
    private <T> T parseResponse(String jsonResponse, Type responseType) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonResponse, responseType);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            // Obsługa błędu
            return null;
        }
    }



    private void showDefinition() {
        String wordToSearch = editTextWord.getText().toString().trim();

        if (wordToSearch.isEmpty()) {
            // Handle empty input
            Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show();
            return;
        }

        DictionaryApiService.getWordDefinition(wordToSearch, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Obsługa błędu
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DictionaryActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        Log.d("API_RESPONSE", jsonResponse);

                        // Parsuj odpowiedź na listę obiektów WordDefinition
                        List<WordDefinition> wordDefinitions = parseResponse(jsonResponse, new TypeToken<List<WordDefinition>>(){}.getType());

                        if (wordDefinitions != null && !wordDefinitions.isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDefinitionDialog(wordDefinitions.get(0));
                                }
                            });
                        } else {
                            // Obsługa przypadku, gdy wordDefinition jest null
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run(){
                                    Toast.makeText(DictionaryActivity.this, "Word definition is null", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        // Obsługa błędu parsowania
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DictionaryActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // Obsługa błędu odpowiedzi HTTP
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DictionaryActivity.this, "HTTP Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }

    private void showDefinitionDialog(WordDefinition wordDefinition) {
        if (wordDefinition != null && wordDefinition.getMeanings() != null && !wordDefinition.getMeanings().isEmpty()) {
            List<Definition> definitions = wordDefinition.getMeanings().get(0).getDefinitions();

            // Nowy adapter do wyświetlenia definicji
            WordDefinitionAdapter adapter = new WordDefinitionAdapter(definitions);

            // Pobierz RecyclerView z widoku
            RecyclerView recyclerView = findViewById(R.id.recyclerViewDefinitions);

            // Ustaw adapter w RecyclerView
            recyclerView.setAdapter(adapter);
            String firstDefinition = adapter.getFirstDefinition();

            // Obsługę przycisku "Stwórz fiszkę"
            Button createFlashcardButton = findViewById(R.id.buttonCreateFlashcard);
            createFlashcardButton.setVisibility(View.VISIBLE);
            createFlashcardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createFlashcard(firstDefinition);
                }
            });
        } else {
            Log.d("DEBUG", "Word definition is null");
        }
    }
    private void createFlashcard(String definition) {
        // Pobieranie słowa
        String word = editTextWord.getText().toString().trim();

        // Przejdź do AddFlashcardActivity i przekaż dane
        Intent intent = new Intent(this, AddFlashcardActivity.class);
        intent.putExtra("word", word);
        intent.putExtra("definition", definition);
        startActivity(intent);
    }

}
