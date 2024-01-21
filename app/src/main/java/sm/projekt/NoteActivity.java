package sm.projekt;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sm.projekt.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NoteViewModel noteViewModel;
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_ACTIVITY_REQUEST_CODE = 2;
    private Note editedNote = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE) {
                Note note = new Note(data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_TITLE),
                        data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_CONTENT));
                noteViewModel.insert(note);
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.note_added),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
            else if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE) {
                editedNote.setTitle(data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_TITLE));
                editedNote.setContent(data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_CONTENT));
                noteViewModel.update(editedNote);
                editedNote = null;
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.note_edited),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
        }
        else
            Snackbar.make(findViewById(R.id.coordinator_layout),
                            getString(R.string.empty_not_saved),
                            Snackbar.LENGTH_LONG)
                    .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.findAll().observe(this, adapter::setNotes);


        FloatingActionButton addNoteButton = findViewById(R.id.add_button);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, EditNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_note);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView noteTitleTextView;
        private TextView noteContentTextView;
        private Note note;

        public NoteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.note_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            noteTitleTextView = itemView.findViewById(R.id.note_title);
            noteContentTextView = itemView.findViewById(R.id.note_content);
        }

        public void bind(Note note) {
            this.note = note;
            noteTitleTextView.setText(note.getTitle());
            noteContentTextView.setText(note.getContent());
        }

        @Override
        public void onClick(View v) {
            NoteActivity.this.editedNote = this.note;
            Intent intent = new Intent(NoteActivity.this, EditNoteActivity.class);
            intent.putExtra(EditNoteActivity.EXTRA_EDIT_NOTE_TITLE, note.getTitle());
            intent.putExtra(EditNoteActivity.EXTRA_EDIT_NOTE_CONTENT, note.getContent());
            startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            NoteActivity.this.noteViewModel.delete(this.note);
            return true;
        }


    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<Note> notes;

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NoteHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            if (notes != null) {
                Note note = notes.get(position);
                holder.bind(note);
            }
            else
                Log.d("NoteActivity", "No notes");
        }

        @Override
        public int getItemCount() {
            if (notes != null) {
                return notes.size();
            }else {
                return 0;
            }
        }

        void setNotes(List<Note> notes) {
            this.notes = notes;
            notifyDataSetChanged();
        }
    }
}