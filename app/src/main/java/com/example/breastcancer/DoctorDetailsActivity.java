package com.example.breastcancer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DoctorDetailsActivity extends AppCompatActivity {
    private EditText searchInput;
    private Button searchButton;
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;

    // Sample list of doctors
    private List<String> doctorNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.recycler_view);

        // Initialize sample doctor names
        doctorNames = new ArrayList<>();
        doctorNames.add("Dr. Smith");
        doctorNames.add("Dr. Johnson");
        doctorNames.add("Dr. Williams");
        doctorNames.add("Dr. Jones");
        doctorNames.add("Dr. Brown");
        doctorNames.add("Dr. Garcia");
        doctorNames.add("Dr. Martinez");
        doctorNames.add("Dr. Smith");
        doctorNames.add("Dr. Johnson");
        doctorNames.add("Dr. Williams");
        doctorNames.add("Dr. Jones");
        doctorNames.add("Dr. Brown");
        doctorNames.add("Dr. Garcia");
        doctorNames.add("Dr. Martinez");
        doctorNames.add("Dr. Martinez");
        doctorNames.add("Dr. Smith");
        doctorNames.add("Dr. Johnson");

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DoctorAdapter(doctorNames); // Show full list initially
        recyclerView.setAdapter(adapter);

        // Set up search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchInput.getText().toString().trim();
                if (query.isEmpty()) {
                    // If search is empty, display full list
                    adapter.updateData(doctorNames);
                } else {
                    List<String> results = linearSearch(doctorNames, query);
                    if (results.isEmpty()) {
                        Toast.makeText(DoctorDetailsActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.updateData(results);
                    }
                }
            }
        });
    }

    // Linear search algorithm
    private List<String> linearSearch(List<String> list, String query) {
        List<String> results = new ArrayList<>();
        for (String doctor : list) {
            if (doctor.toLowerCase().contains(query.toLowerCase())) {
                results.add(doctor);
            }
        }

        // Add the non-matching results at the end to maintain the list order
        List<String> nonMatching = new ArrayList<>(list);
        nonMatching.removeAll(results);
        results.addAll(nonMatching);

        return results;
    }
}
