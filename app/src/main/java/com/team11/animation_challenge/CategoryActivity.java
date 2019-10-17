package com.team11.animation_challenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ArrayList<CategoryModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        loadData();

        getSupportActionBar().setTitle("Choose Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initContent();
    }

    private void loadData() {
        arrayList = new ArrayList<>();
        arrayList.add(new CategoryModel("Movies", R.drawable.ic_movies, "https://opentdb.com/api.php?amount=11&category=11&type=multiple"));
        arrayList.add(new CategoryModel("Animals", R.drawable.ic_animals, "https://opentdb.com/api.php?amount=11&category=27&type=multiple"));
        arrayList.add(new CategoryModel("History", R.drawable.ic_history, "https://opentdb.com/api.php?amount=11&category=23&type=multiple"));
        arrayList.add(new CategoryModel("Geography", R.drawable.ic_geography, "https://opentdb.com/api.php?amount=11&category=22&type=multiple"));
        arrayList.add(new CategoryModel("Vehicles", R.drawable.ic_vehicles, "https://opentdb.com/api.php?amount=11&category=28&type=multiple"));
        arrayList.add(new CategoryModel("Arts", R.drawable.ic_arts, "https://opentdb.com/api.php?amount=11&category=25&type=multiple"));
        arrayList.add(new CategoryModel("General Knowledge", R.drawable.ic_general_knowledge, "https://opentdb.com/api.php?amount=11&category=9&type=multiple"));
        arrayList.add(new CategoryModel("Science: Mathematics", R.drawable.ic_math, "https://opentdb.com/api.php?amount=11&category=19&type=multiple"));
    }

    private void initContent() {
        final RecyclerView catagoiresGrid = (RecyclerView) findViewById(R.id.categories_grid);
        final GridLayoutManager manager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        catagoiresGrid.setLayoutManager(manager);

        CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(this, arrayList);
        catagoiresGrid.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
