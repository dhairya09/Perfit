package com.example.dhairyapujara.perfit_submit;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class userinfo extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfopage);
    }
    /*@Override
    public void onBackPressed() {
        startActivity(new Intent(this, loginactivity.class));
    }*/
    public void nextclick(View view){

        boolean invalid=false;
        EditText na=(EditText)findViewById(R.id.fname);
        String name=na.getText().toString().trim();

        EditText h=(EditText)findViewById(R.id.height);
        String height=h.getText().toString().trim();

       RadioGroup radiogroup = (RadioGroup)findViewById(R.id.radioSex);
        int id=radiogroup.getCheckedRadioButtonId();

        RadioButton radbut=(RadioButton)findViewById(id);
        String radioText=radbut.getText().toString().trim();

        if(name.equals("")){
            invalid=true;
            Toast.makeText(getApplicationContext(), "Please enter your Name", Toast.LENGTH_SHORT).show();
        }
        else if(height.equals("")){
            invalid=true;
            Toast.makeText(getApplicationContext(), "Please enter your height", Toast.LENGTH_SHORT).show();
        }
        else {
            invalid=false;
        }
        if(!invalid) {
            Toast.makeText(getApplicationContext(), "Thank you for the information, Let's proceed", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(userinfo.this, OrientationActivity.class);
            startActivity(intent2);
        }
    }
}