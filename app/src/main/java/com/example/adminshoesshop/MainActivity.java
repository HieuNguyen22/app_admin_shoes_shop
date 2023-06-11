package com.example.adminshoesshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminshoesshop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        // SIGN IN
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = binding.email.getText().toString().trim();
                String pass = binding.password.getText().toString().trim();

                System.out.println(user + " - " + pass);

                if(user.isEmpty() == true) {
                    binding.email.setError("Email is required!");
                    return;
                }

                if(pass.isEmpty() == true) {
                    binding.password.setError("Password is required!");
                    return;
                }

                if(user.equals("admin") && pass.equals("admin")) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getApplicationContext(), NavigateActivity.class);
                    intent.putExtra("user",user);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                    binding.progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}