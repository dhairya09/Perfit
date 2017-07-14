package com.example.dhairyapujara.perfit_submit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.BindView;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @BindView(R.id.input_email) EditText _username;
    @BindView(R.id.input_password) EditText _password;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;
    SharedPreferences prefs;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //session = new SessionManager(getApplicationContext());

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //startActivity(intent);
            }
        });
    }

    public void login() {

        Log.d(TAG, "Login");

        if (!validate()) {
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(Login.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Logging In...");
            progressDialog.show();


            //String username = String.valueOf(getIntent().getExtras().get("username"));
            //String password = String.valueOf(getIntent().getExtras().get("password"));

            EditText username = (EditText)findViewById(R.id.input_email);
            EditText password = (EditText)findViewById(R.id.input_password);


            String url = "http://perfit.pwmky3qky6.us-west-2.elasticbeanstalk.com/login";
            // Post params to be sent to the server
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("username",username.getText().toString());
            params.put("password", password.getText().toString());

            Log.d("msg:", new JSONObject(params).toString());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("response:", response.toString());
                                if(response.getString("access_token").length() > 0){

                                    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("token", response.getString("access_token"));
                                    //editor.putString("user_id", response.getString("user_id"));
                                    editor.apply();
                                    //signup successful
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Login.this, ProductActivity.class);
                                    //put user unique id to the bundle(to be used when making a request to the server
                                    startActivity(i);
                                }

                                else {

                                    Toast.makeText(getApplicationContext(), "Please try logging in!", Toast.LENGTH_SHORT).show();

                                    //when the response is successful
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.cancel();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                           //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });

            // Access the RequestQueue through your singleton class.
            MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        }

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _username.getText().toString();
        String password = _password.getText().toString();
        String MobilePattern = "[0-9]{10}";
        //String email1 = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        if (!email.isEmpty())
        {
            if(email.matches(emailPattern)){
                //_username.setError("Enter a valid Email-address");
                //valid = false;
            }
            else{
                _username.setError(null);
            }
        }
        else
        {
            _username.setError("Email can't be left blank");
            valid = false;
        }

        //if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        if (!password.isEmpty()){
            if(password.length() <= 6){
                _password.setError("Password must be greater than 6 characters");
                valid = false;
            }
            else{
                _password.setError(null);
            }
        }
        else
        {
            _password.setError("Password can't be left blank");
            valid = false;
        }
        return valid;
    }
}
