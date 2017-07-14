package com.example.dhairyapujara.perfit_submit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.dhairyapujara.perfit_submit.R;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.BindView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_firstname) EditText _firstname;
    @BindView(R.id.input_lastname) EditText _lastname;
    @BindView(R.id.input_email) EditText _email;
    @BindView(R.id.input_phone) EditText _phone;
    @BindView(R.id.input_password) EditText _password;
    //@InjectView(R.id.dob) EditText _dob;
    //@InjectView(R.id.gender) EditText _gender;


    //@InjectView(R.id.input_name) EditText _nameText;
    //@InjectView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
                Intent i = new Intent(SignupActivity.this,Login.class);
                startActivity(i);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
        }
        else{
            //after successful validation, move tp next activity, make the SignupFollo make the request to server

            //if signup is successful
            Intent i = new Intent(SignupActivity.this, SignupFollowup.class);
            i.putExtra("firstname", _firstname.getText().toString());
            i.putExtra("lastname", _lastname.getText().toString());
            i.putExtra("email", _email.getText().toString());
            i.putExtra("phone", _phone.getText().toString());
            i.putExtra("password", _password.getText().toString());
            startActivity(i);


            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            String url = "http://srinidhisample.appspot.com/helloworld.php?only_legislators=true";
            // Post params to be sent to the server
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("token", "AbCdEfGh123456");
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, new JSONObject(params), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            //if signup is successful
                            Intent i = new Intent(SignupActivity.this, SignupFollowup.class);
                            //put user unique id to the bundle(to be used when making a request to the server
                            startActivity(i);
                            //when the response is successful
                            progressDialog.cancel();

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Access the RequestQueue through your singleton class.
            MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        }
    }

    public boolean validate() {
        boolean valid = true;

        String firstname = _firstname.getText().toString();
        String lastname = _lastname.getText().toString();
        String email = _email.getText().toString();
        String phone = _phone.getText().toString();
        String password = _password.getText().toString();

        if (firstname.isEmpty())
        {
            _firstname.setError("First Name can't be left blank");
            valid = false;
        } else {
            _firstname.setError(null);
        }

        if (lastname.isEmpty())
        {
            _lastname.setError("Last Name can't be left blank");
            valid = false;
        } else {
            _lastname.setError(null);
        }

        if (!email.isEmpty())
        {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                _email.setError("Enter a valid Email-address");
                valid = false;
            }
            else{
                _email.setError(null);
            }
        }
        else
        {
            _email.setError("Email can't be left blank");
            valid = false;
        }

        if (phone.isEmpty())
        {
            _phone.setError("Phone can't be left blank");
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (!password.isEmpty()){
            if(password.length() <= 6){
                _password.setError("Password must be greater than 6 characters");
                valid=false;
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
