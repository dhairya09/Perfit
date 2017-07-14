package com.example.dhairyapujara.perfit_submit;

import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by nehasachdev on 2/2/2017.
 */

public class loginactivity extends AppCompatActivity {
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
    }

    public void addActionOnButton(View view){
        Intent mintent = new Intent(loginactivity.this, SignupActivity.class);
        startActivity(mintent);
        finish();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }
    public void loginclick(View v){
        boolean invalid =false;

        EditText un=(EditText)findViewById(R.id.uname);
        String uname=un.getText().toString().trim();

        EditText pa=(EditText)findViewById(R.id.pass);
        String password=pa.getText().toString().trim();

        if(uname.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(uname).matches()){
            invalid=true;
            Toast.makeText(getApplicationContext(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        else if(password.equals("") || password.length()<6){
            invalid=true;
            Toast.makeText(getApplicationContext(), "Enter the valid password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            invalid=false;
        }

        if(!invalid){
            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
            Intent intent2=new Intent(loginactivity.this, userinfo.class);
            startActivity(intent2);
            finish();
        }
    }
}

