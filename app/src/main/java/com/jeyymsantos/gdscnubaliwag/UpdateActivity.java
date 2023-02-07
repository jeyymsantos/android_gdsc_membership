package com.jeyymsantos.gdscnubaliwag;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    String[] sections = {"ITE101", "ITE201", "ITE202", "ITE301", "ITE302", "ITE303"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterSections;
    String section = "", sex = "";

    ProgressBar progressBar;
    Button btnCancel, btnUpdate;
    TextView txtEmail, tvError;
    RadioGroup rdSex;
    RadioButton rdMale, rdFemale;
    TextInputEditText txtFirstName, txtLastName, txtNewPassword;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialization
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        autoCompleteTextView = findViewById(R.id.selectSection);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        rdSex = findViewById(R.id.rdSex);
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("GDSCNUBaliwag", MODE_PRIVATE);

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

        // Display from Shared Preferences
        txtFirstName.setText(sharedPreferences.getString("firstName", ""));
        txtLastName.setText(sharedPreferences.getString("lastName", ""));
        txtEmail.setText(sharedPreferences.getString("email", ""));
        sex = sharedPreferences.getString("sex", "");

        if(sex.equals("Male")){
            rdMale.setChecked(true);
        }else{
            rdFemale.setChecked(true);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdSex.getCheckedRadioButtonId() != -1){
                    RadioButton rdSelectedSex = findViewById(rdSex.getCheckedRadioButtonId());
                    sex = rdSelectedSex.getText().toString();
                }
                update();
            }
        });
    }

    private void update(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setMessage("Are you sure you want to update your credentials?");
        builder.setTitle("Update");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            progressBar.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "https://jeyym.tech/update.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            if (response.equals("success")) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("firstName", txtFirstName.getText().toString());
                                editor.putString("lastName", txtLastName.getText().toString());
                                editor.putString("section", section);
                                editor.putString("email", txtEmail.getText().toString());
                                editor.putString("sex", sex);
                                editor.apply();

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);

                                Toast.makeText(UpdateActivity.this, "Account Updated Successfully", Toast.LENGTH_LONG).show();
                                finish();

                            } else {
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
                    paramV.put("email", sharedPreferences.getString("email", ""));
                    paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                    paramV.put("firstName", txtFirstName.getText().toString());
                    paramV.put("lastName", txtLastName.getText().toString());
                    paramV.put("password", txtNewPassword.getText().toString());
                    paramV.put("section", section);
                    paramV.put("sex", sex);
                    return paramV;
                }
            };
            queue.add(stringRequest);
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}