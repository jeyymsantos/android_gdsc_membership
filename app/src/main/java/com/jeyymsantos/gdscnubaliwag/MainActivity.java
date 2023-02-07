package com.jeyymsantos.gdscnubaliwag;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnLogout, btnEdit, btnDelete;
    TextView txtFirstName, txtFullname, txtEmail, txtSex, txtSection;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtFullname = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtSex = findViewById(R.id.txtSex);
        txtSection = findViewById(R.id.txtSection);
        sharedPreferences = getSharedPreferences("GDSCNUBaliwag", MODE_PRIVATE);

        // Checker / Validation
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }

        // Display from Shared Preferences
        txtFirstName.setText(sharedPreferences.getString("firstName", ""));
        txtFullname.setText(sharedPreferences.getString("firstName", "") + " " + sharedPreferences.getString("lastName", ""));
        txtEmail.setText(sharedPreferences.getString("email", ""));
        txtSex.setText(sharedPreferences.getString("sex", ""));
        txtSection.setText(sharedPreferences.getString("section", ""));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("Logout", "Are you sure you want to logout?", "logout");
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("Delete Account", "Are you sure you want to delete account?", "delete");
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void alert(String title, String message, String option){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            if(option.equals("logout")){
                logout();
            }else{
                delete();
            }
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void delete(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://jeyym.tech/delete.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("success")) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("logged", "false");
                            editor.putString("firstName", "");
                            editor.putString("lastName", "");
                            editor.putString("section", "");
                            editor.putString("email", "");
                            editor.putString("sex", "");
                            editor.putString("apiKey", "");
                            editor.apply();

                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);

                            Toast.makeText(MainActivity.this, "Account Deleted Successfully", Toast.LENGTH_LONG).show();

                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
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
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void logout() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://jeyym.tech/logout.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("success")) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("logged", "false");
                            editor.putString("firstName", "");
                            editor.putString("lastName", "");
                            editor.putString("section", "");
                            editor.putString("email", "");
                            editor.putString("sex", "");
                            editor.putString("apiKey", "");
                            editor.apply();

                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);

                            Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();

                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
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
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

}