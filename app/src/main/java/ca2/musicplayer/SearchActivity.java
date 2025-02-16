package ca2.musicplayer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText searchInput;
    private RecyclerView searchResults;
    private ArrayList<Song> allSongs;
    private ArrayList<Song> filteredSongs;
    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.search_input);
        searchResults = findViewById(R.id.search_results);
        allSongs = new ArrayList<>();
        filteredSongs = new ArrayList<>();

        setupToolbar();
        setupSearchInput();
        setupRecyclerView();
        loadAllSongs();
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");
    }

    private void setupSearchInput() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongAdapter(this, filteredSongs, (song, position) -> {
            Intent intent = new Intent(SearchActivity.this, PlayerActivity.class);
            intent.putExtra("songPath", song.getPath());
            intent.putExtra("songName", song.getTitle());
            startActivity(intent);
        });
        searchResults.setAdapter(adapter);
    }

    private void loadAllSongs() {
        // Similar to MainActivity's loadSongs() but store in allSongs list
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        try {
            Cursor cursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Media.ARTIST));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Media.DATA));

                    allSongs.add(new Song(title, artist != null ? artist : "Unknown Artist", path));
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading music files: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void filterSongs(String query) {
        filteredSongs.clear();
        if (query.isEmpty()) {
            filteredSongs.addAll(allSongs);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Song song : allSongs) {
                if (song.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        song.getArtist().toLowerCase().contains(lowerCaseQuery)) {
                    filteredSongs.add(song);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}