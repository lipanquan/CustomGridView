package com.jzg.cgv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jzg.lib.cgv.adapter.CustomGridViewAdapter;
import com.jzg.lib.cgv.view.CustomGridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomGridView.OnCustomGridViewItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomGridView cgv = (CustomGridView) findViewById(R.id.cgv);
        ArrayList<String> allItemStrs = new ArrayList<>();
        allItemStrs.add("Item1");
        allItemStrs.add("Item2");
        allItemStrs.add("Item3");
        allItemStrs.add("Item4");
        allItemStrs.add("Item5");
        allItemStrs.add("Item6");
        cgv.setAdapter(new CustomGridViewAdapter(allItemStrs));
        cgv.setOnItemClickListener(this);
    }

    @Override
    public void onCustomGridViewItemClick(CustomGridView parent, View view, int position) {
        Toast.makeText(this, "" + position, 0).show();
    }
}
