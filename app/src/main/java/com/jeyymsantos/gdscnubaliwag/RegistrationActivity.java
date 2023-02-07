package com.jeyymsantos.gdscnubaliwag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    String[] sections = {"ITE101", "ITE201", "ITE202", "ITE301", "ITE302", "ITE303"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterSections;

    TextInputEditText txtFirstName, txtLastName, txtEmail, txtPassword;
    TextView tvError;
    RadioGroup rdSex;
    RadioButton rdMale, rdFemale;
    String section = "", sex = "";
    CheckBox ckAgreement;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialization
        autoCompleteTextView = findViewById(R.id.selectSection);
        ckAgreement = findViewById(R.id.ckAgreement);
        btnRegister = findViewById(R.id.btnRegister);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        rdSex = findViewById(R.id.rdSex);
        tvError = findViewById(R.id.tvError);

        // Initializing Array for Section
        adapterSections = new ArrayAdapter<String>(this, R.layout.list_item, sections);
        autoCompleteTextView.setAdapter(adapterSections);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                section = parent.getItemAtPosition(position).toString();
                autoCompleteTextView.setError(null);
            }

        });

        // Agreement and Button Changed
        ckAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnRegister.setEnabled(true);
                } else {
                    btnRegister.setEnabled(false);
                }
            }
        });

        // Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvError.setVisibility(View.GONE);
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if(rdSex.getCheckedRadioButtonId() != -1){
                    RadioButton rdSelectedSex = findViewById(rdSex.getCheckedRadioButtonId());
                    sex = rdSelectedSex.getText().toString();
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://jeyym.tech/register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    tvError.setText(response);
                                    tvError.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("firstName", firstName);
                        paramV.put("lastName", lastName);
                        paramV.put("email", email);
                        paramV.put("password", password);
                        paramV.put("section", section);
                        paramV.put("sex", sex);
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

    }
}