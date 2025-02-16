package ca2.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class PlayStats {
    private static final String PREFS_NAME = "MusicPlayerStats";
    private static final String KEY_TOTAL_TIME = "total_listening_time";
    private static final String KEY_MOST_PLAYED = "most_played_song";
    private static final String KEY_MOST_PLAYED_COUNT = "most_played_count";

    private final SharedPreferences prefs;

    public PlayStats(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addPlayTime(long timeInMillis) {
        long totalTime = prefs.getLong(KEY_TOTAL_TIME, 0);
        prefs.edit().putLong(KEY_TOTAL_TIME, totalTime + timeInMillis).apply();
    }

    public void incrementSongPlay(String songTitle) {
        int currentCount = prefs.getInt(songTitle, 0);
        int mostPlayedCount = prefs.getInt(KEY_MOST_PLAYED_COUNT, 0);

        currentCount++;
        prefs.edit().putInt(songTitle, currentCount).apply();

        if (currentCount > mostPlayedCount) {
            prefs.edit()
                    .putString(KEY_MOST_PLAYED, songTitle)
                    .putInt(KEY_MOST_PLAYED_COUNT, currentCount)
                    .apply();
        }
    }

    public long getTotalListeningTime() {
        return prefs.getLong(KEY_TOTAL_TIME, 0);
    }

    public String getMostPlayedSong() {
        return prefs.getString(KEY_MOST_PLAYED, "No songs played yet");
    }

    public int getMostPlayedCount() {
        return prefs.getInt(KEY_MOST_PLAYED_COUNT, 0);
    }
}