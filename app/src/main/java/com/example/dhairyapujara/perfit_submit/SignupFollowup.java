package com.example.dhairyapujara.perfit_submit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by srinidhikarthikbs on 4/13/17.
 */

public class SignupFollowup extends AppCompatActivity {

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView, dob_text;
    private EditText height;
    private int year, month, day;
    private Button dob_button, proceed;
    private boolean date_set=false;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_followup);

        dob_button = (Button) findViewById(R.id.dob_button);
        dob_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        height = (EditText) findViewById(R.id.height);

        dob_text = (TextView) findViewById(R.id.dob_text);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        proceed = (Button) findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                if(!date_set){
                    Toast.makeText(getApplicationContext(), "Select a Date of Birth", Toast.LENGTH_SHORT).show();
                    valid=false;
                }

                if(!(height.getText().toString().trim().length()>0)){
                    Toast.makeText(getApplicationContext(), "Enter a valid height", Toast.LENGTH_SHORT).show();
                    valid=false;
                }

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);
                String gender = radioSexButton.getText().toString();

                if(valid){
                    final ProgressDialog progressDialog = new ProgressDialog(SignupFollowup.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Updating Details...");
                    progressDialog.show();

                    String firstname = String.valueOf(getIntent().getExtras().get("firstname"));
                    String lastname = String.valueOf(getIntent().getExtras().get("lastname"));
                    String email = String.valueOf(getIntent().getExtras().get("email"));
                    String phone = String.valueOf(getIntent().getExtras().get("phone"));
                    String password = String.valueOf(getIntent().getExtras().get("password"));

                    String url = "http://perfit.pwmky3qky6.us-west-2.elasticbeanstalk.com/register";
                    // Post params to be sent to the server
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("firstname", firstname);
                    params.put("lastname",lastname );
                    params.put("email", email);
                    params.put("phone", phone);
                    params.put("password", password);
                    params.put("gender", gender);
                    params.put("dob", dob_text.getText().toString());
                    params.put("height", height.getText().toString().trim());
                    Log.d("msg:", new JSONObject(params).toString());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.d("response:", response.toString());
                                        if(response.getInt("statuscode")==200){
                                            //store the x_auth_token and user_id in shared preferences,clear out while logging out of app
                                            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("token", response.getString("token"));
                                            editor.putString("user_id", response.getString("user_id"));
                                            editor.apply();
                                            //signup successful
                                            Toast.makeText(getApplicationContext(), "Signup Successful "+response.getString("token"), Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignupFollowup.this, VideoPlayerActivity.class);
                                            //put user unique id to the bundle(to be used when making a request to the server
                                            startActivity(i);
                                            //when the response is successful
                                        }
                                        else if(response.getInt("statuscode")==400){
                                            Toast.makeText(getApplicationContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Already Registered. Please try logging in!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    progressDialog.cancel();
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });

                    // Access the RequestQueue through your singleton class.
                    MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2+1 = month
                    // arg3 = day
                    //showDate(arg1, arg2+1, arg3);
                    year = arg1;
                    month = arg2+1;
                    day = arg3;
                    dob_text.setText(month+"/"+day+"/"+year);
                    date_set = true;
                }
            };
}
