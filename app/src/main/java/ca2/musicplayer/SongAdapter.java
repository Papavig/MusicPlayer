package ca2.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final List<Song> songs;
    private final Context context;
    private final OnSongClickListener listener;

    public interface OnSongClickListener {
        void onSongClick(Song song, int position);
    }

    public SongAdapter(Context context, List<Song> songs, OnSongClickListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.titleText.setText(song.getTitle());
        holder.artistText.setText(song.getArtist());

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSongClick(song, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView titleText;
        TextView artistText;
        ImageView playButton;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            titleText = itemView.findViewById(R.id.song_title);
            artistText = itemView.findViewById(R.id.song_artist);
            playButton = itemView.findViewById(R.id.play_button);
        }
    }
}