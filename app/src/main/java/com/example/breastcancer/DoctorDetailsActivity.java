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
        doctorNames.add("Dr. R. Ramesh, Chennai");
        doctorNames.add("Dr. Pooja N. Sharma, Jaipur");
        doctorNames.add("Dr. Anupama R. Sharma, Chandigarh");
        doctorNames.add("Dr. Shanta S. M. Devi, Hyderabad");
        doctorNames.add("Dr. Ashok V. Suri, Bengaluru");
        doctorNames.add("Dr. Vidya V. Suryakanta, Hyderabad");
        doctorNames.add("Dr. Harit Chaturvedi, New Delhi");
        doctorNames.add("Dr. Satish G. Rao, Ahmedabad");
        doctorNames.add("Dr. Anjali S. Sharma, Pune");
        doctorNames.add("Dr. N. P. Kumaran, Chennai");
        doctorNames.add("Dr. Suman B. Kshirsagar, Mumbai");
        doctorNames.add("Dr. G. N. Mehta, Ahmedabad");
        doctorNames.add("Dr. K. K. Sharma, Kolkata");
        doctorNames.add("Dr. Vinay K. Gupta, Jaipur");
        doctorNames.add("Dr. Meenakshi K. Mallya, Bengaluru");
        doctorNames.add("Dr. S. R. Patil, Pune");
        doctorNames.add("Dr. Ritu D. Bansal, Kolkata");
        doctorNames.add("Dr. Anshuman Kumar, New Delhi");
        doctorNames.add("Dr. Shyam Sundar, Chandigarh");
        doctorNames.add("Dr. Pankaj S. Dhananjay, Mumbai");
        doctorNames.add("Dr. N. P. Kumaran, Chennai");

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
