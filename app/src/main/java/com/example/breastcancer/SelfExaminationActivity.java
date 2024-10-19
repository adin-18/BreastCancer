package com.example.breastcancer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelfExaminationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private YouTubeLinkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_examination); // Make sure this is your correct layout

        recyclerView = findViewById(R.id.recyclerView); // Make sure this ID matches your layout

        // Sample data
        List<YouTubeLink> youTubeLinks = new ArrayList<>();
        youTubeLinks.add(new YouTubeLink("Correct technique of breast self examination to detect Breast Cancer early", "https://www.youtube.com/watch?v=XKtTymNkcj0"));
        youTubeLinks.add(new YouTubeLink("How to manage Breast Pain", "https://www.youtube.com/watch?v=jVLcOTxvbsg"));
        youTubeLinks.add(new YouTubeLink("8 Signs On Breast You Should NOT Ignore", "https://www.youtube.com/watch?v=WSWPLzUJkGw"));

        // Initialize the adapter with context and the list of links
        adapter = new YouTubeLinkAdapter(this, youTubeLinks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set the item click listener
        adapter.setOnItemClickListener(new YouTubeLinkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(YouTubeLink link) {
                // Ensure the link is properly formatted
                String url = link.getUrl();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url; // Fix missing scheme if needed
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

                Log.d("SelfExaminationActivity", "Opening link: " + link.getUrl()); // Debug log
            }
        });
    }
}
