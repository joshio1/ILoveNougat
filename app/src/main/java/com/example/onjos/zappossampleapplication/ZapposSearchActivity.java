package com.example.onjos.zappossampleapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class ZapposSearchActivity extends AppCompatActivity {

    // UI references.
    private TextView txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zappos_search);
        // Set up search.
        txtSearch = (TextView) findViewById(R.id.search);

        Button mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void search() {
        if(!txtSearch.getText().equals(""))
        {
            Intent intent = new Intent(this, ZapposMainActivity.class);
            intent.putExtra("search_term", txtSearch.getText().toString());
            startActivity(intent);
        }
        else
            Toast.makeText(ZapposSearchActivity.this, "Please enter text to search!", Toast.LENGTH_SHORT).show();
    }
}

