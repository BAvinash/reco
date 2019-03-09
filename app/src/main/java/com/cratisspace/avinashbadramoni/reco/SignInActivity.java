package com.cratisspace.avinashbadramoni.reco;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignInActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FirebaseDatabase db;
    DatabaseReference users;
    FirebaseAuth auth;
    Button btn,registerbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth =FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btn = (Button)findViewById(R.id.sign_btn);
        registerbtn = (Button)findViewById(R.id.register_btn);
        progressBar = (ProgressBar)findViewById(R.id.progessbar);


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });


    }


    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign In");
        dialog.setMessage("Please use Email and Password");
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);


        dialog.setView(login_layout);
        dialog.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);



                if (TextUtils.isEmpty(edtEmail.getText().toString())){
                    Toast.makeText(SignInActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();


                    return;
                }


                if (TextUtils.isEmpty(edtPassword.getText().toString())){
                    Toast.makeText(SignInActivity.this, "please enter password", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (edtPassword.getText().toString().length()<6){
                    Toast.makeText(SignInActivity.this, "password is too short!", Toast.LENGTH_SHORT).show();

                    return;
                }

                auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        progressBar.setVisibility(View.INVISIBLE);


                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this, "Faild"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });



            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void showRegisterDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Register");
        dialog.setMessage("Please use Email and Password");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);

        dialog.setView(register_layout);
        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(edtEmail.getText().toString())){
                  //  Snackbar.make(rootLayout,"Please enter email address",Snackbar.LENGTH_LONG).show();

                    return;
                }

                if (TextUtils.isEmpty(edtPhone.getText().toString())){
                   // Snackbar.make(rootLayout,"Please enter Phone number",Snackbar.LENGTH_LONG).show();

                    return;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())){
                  //  Snackbar.make(rootLayout,"Please enter password",Snackbar.LENGTH_LONG).show();

                    return;
                }
                if (edtPassword.getText().toString().length()<6){
                    //Snackbar.make(rootLayout,"Password is too short!!",Snackbar.LENGTH_LONG).show();

                    return;
                }

                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {


                        User user = new User();
                        user.setEmail(edtEmail.getText().toString());
                        user.setName(edtName.getText().toString());
                        user.setPassword(edtPassword.getText().toString());;
                        user.setPhone(edtPhone.getText().toString());

                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignInActivity.this, "Register successfull1" , Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);


                                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignInActivity.this, "faild"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);

                                    }
                                });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignInActivity.this, "faild"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        })
                ;




            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
