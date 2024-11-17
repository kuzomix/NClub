package com.example.bottom_main;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom_main.Adapter.SearchResultsAdapter;
import com.example.bottom_main.Domain.ItemDomain;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    private ArrayList<ItemDomain> searchResults;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        // 接收資料
        searchResults = (ArrayList<ItemDomain>) getIntent().getSerializableExtra("searchResults");
        searchQuery = getIntent().getStringExtra("searchQuery");

        // 顯示搜尋條件
        TextView textViewSearchQuery = findViewById(R.id.textViewSearchQuery);
        textViewSearchQuery.setText("Results for: " + searchQuery);

        // 顯示搜尋結果
        displaySearchResults();
    }

    private void displaySearchResults() {
        if (searchResults != null && !searchResults.isEmpty()) {
            RecyclerView recyclerView = findViewById(R.id.recyclerViewSearchResults);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new SearchResultsAdapter(searchResults));
        } else {
            Toast.makeText(this, "No results found for: " + searchQuery, Toast.LENGTH_SHORT).show();
        }
    }
}
