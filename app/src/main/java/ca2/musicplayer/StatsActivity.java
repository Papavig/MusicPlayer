package ca2.musicplayer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StatsActivity extends AppCompatActivity {
    private PlayStats playStats;
    private TextView totalTimeText;
    private TextView mostPlayedText;
    private TextView averageSessionText;
    private BarChart listeningChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        playStats = new PlayStats(this);

        setupViews();
        setupToolbar();
        updateStats();
        setupChart();
    }

    private void setupViews() {
        totalTimeText = findViewById(R.id.total_time_text);
        mostPlayedText = findViewById(R.id.most_played_text);
        averageSessionText = findViewById(R.id.average_session_text);
        listeningChart = findViewById(R.id.listening_chart);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Listening Stats");
    }

    private void updateStats() {
        // Format total listening time
        long totalMillis = playStats.getTotalListeningTime();
        long hours = TimeUnit.MILLISECONDS.toHours(totalMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalMillis) % 60;

        totalTimeText.setText(String.format("Total Listening Time: %dh %dm", hours, minutes));

        // Most played song
        String mostPlayed = playStats.getMostPlayedSong();
        int playCount = playStats.getMostPlayedCount();
        mostPlayedText.setText(String.format("Most Played: %s (%d times)", mostPlayed, playCount));

        // Average session (simplified calculation)
        long avgSessionMinutes = hours > 0 ? (minutes + (hours * 60)) / playCount : 0;
        averageSessionText.setText(String.format("Average Session: %d minutes", avgSessionMinutes));
    }

    private void setupChart() {
        // Sample data for the chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 20));
        entries.add(new BarEntry(1, 50));
        entries.add(new BarEntry(2, 30));
        entries.add(new BarEntry(3, 70));
        entries.add(new BarEntry(4, 40));
        entries.add(new BarEntry(5, 60));
        entries.add(new BarEntry(6, 45));

        BarDataSet dataSet = new BarDataSet(entries, "Listening Time (minutes)");

        BarData barData = new BarData(dataSet);
        listeningChart.setData(barData);
        listeningChart.getDescription().setEnabled(false);
        listeningChart.animateY(1000);
        listeningChart.invalidate();
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