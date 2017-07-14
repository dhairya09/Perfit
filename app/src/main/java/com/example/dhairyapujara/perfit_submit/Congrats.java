package com.example.dhairyapujara.perfit_submit;

/**
 * Created by srinidhikarthikbs on 4/20/17.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Congrats extends AppCompatActivity {

    private Button model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);

        model = (Button)findViewById(R.id.btn_mod);
        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Congrats.this,ProductActivity.class);
                startActivity(i);
            }
        });
    }
}
