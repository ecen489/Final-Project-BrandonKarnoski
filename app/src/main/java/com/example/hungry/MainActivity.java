package com.example.hungry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
public class MainActivity extends AppCompatActivity {
    //Initialize variables
    Button loginBtn, createAccountBtn;
    EditText username, password;
    private static final int REQ_CODE = 2048;

    FirebaseDatabase firebse;
    DatabaseReference databse;

    FirebaseAuth mAuth;
    FirebaseUser user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebse = FirebaseDatabase.getInstance();
        databse = firebse.getReference();

        loginBtn = (Button) findViewById(R.id.loginBtn);
        createAccountBtn = (Button) findViewById(R.id.createAccnt);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();


    }

    //Logs in and starts new activity
    public void loginAction(View view){
        String email = username.getText().toString();
        String emailPass = password.getText().toString();

        mAuth.signInWithEmailAndPassword(email, emailPass)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser(); //The user is signed in
                            Intent foodIntent = new Intent(MainActivity.this,Places.class);
                            startActivity(foodIntent);
                        } else{
                            Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void logoutAction(View view){
        mAuth.signOut();
        user = null;
        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
    }

    public void createAccount(View view){

        String email = username.getText().toString();
        String emailPass = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, emailPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(getApplicationContext(), "Created Account", Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser(); //The newly created user is already signed in
                        } else{
                            Toast.makeText(getApplicationContext(), "Task Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
