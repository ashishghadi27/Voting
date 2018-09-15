package com.rootdevs.ashish.voting;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.appevents.UserDataStore.EMAIL;

public class Signup extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    LoginButton loginButton;
    CallbackManager callbackManager;
    SignInButton google;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    EditText phonetext, addresstext, gendertext, constituencytext, fnametext, lnametext, mailtext, passtext;
    String TAG = "CHECK", fname, lname, mail, pass, phoneno, address, constituency, gender;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebooksignin);
        google = findViewById(R.id.googlesign_in_button);
        fnametext = findViewById(R.id.name);
        lnametext = findViewById(R.id.lastname);
        mailtext = findViewById(R.id.email);
        passtext = findViewById(R.id.password);
        submit = findViewById(R.id.submitbutton);
        /*SharedPreferences sharedPreferences = getSharedPreferences("Signup", MODE_PRIVATE);
        if(sharedPreferences.getString("signed", "0").equals("1")){
            Intent intent = new Intent(Signup.this, Home.class);
            startActivity(intent);
            finish();
        }*/
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final Bundle params = new Bundle();
                params.putString("fields", "name,email,gender,picture.type(large)");
                new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    try {
                                        JSONObject data = response.getJSONObject();
                                        if (data.has("picture")) {
                                            String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                            String name[] = data.getString("name").split(" ");
                                            Log.v("NM:", data.getString("name"));
                                            fname = name[0];
                                            lname = name[1];
                                            pass = "";
                                            String email = data.getString("email");
                                            Log.v("THE EMAIL IS:", ""+email);
                                            mail = email;

                                            /*SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("profilepic", profilePicUrl);
                                            editor.apply();*/

                                        }
                                        show_alert();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(Signup.this,"EXCEPTION",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }).executeAsync();
            }

            @Override
            public void onCancel() {
                Log.v("STATUS", "CANCELLED");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("STATUS", "ERROR");

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = fnametext.getText().toString();
                lname = lnametext.getText().toString();
                mail = mailtext.getText().toString();
                pass = mailtext.getText().toString();
                show_alert();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Inactivity block");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.d(TAG, "Intry block");
                ProgressDialog mDialog = new ProgressDialog(Signup.this);
                mDialog.setMessage("Loading");
                mDialog.show();
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        String name[] = Objects.requireNonNull(user.getDisplayName()).split(" ");
                        fname = name[0];
                        lname = name[1];
                        mail = user.getEmail();
                        pass = "";
                        show_alert();

                        Toast.makeText(Signup.this, "COMPLETE", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void signIn() {
        if(isNetworkAvailable()){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else Toast.makeText(Signup.this,"Check Your connection", Toast.LENGTH_SHORT).show();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void show_alert(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);


        // Setting Dialog Message
        // alertDialog.setMessage("Enter Password");
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alert, null);

        alertDialog.setView(alertLayout);

        phonetext = alertLayout.findViewById(R.id.phone);
        addresstext = alertLayout.findViewById(R.id.address);
        gendertext = alertLayout.findViewById(R.id.gender);
        constituencytext = alertLayout.findViewById(R.id.constituency);




        // Setting Icon to Dialog

        // Setting Positive "Yes" Button
        Button proceed = alertLayout.findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneno = phonetext.getText().toString();
                address = addresstext.getText().toString();
                constituency = constituencytext.getText().toString();
                gender = gendertext.getText().toString();
                if(gender.contains("male")){
                    gender = "1";
                }
                else if(gender.contains("female")){
                    gender = "2";
                }
                else if(gender.contains("not specify")){
                    gender = "3";
                }
                else {
                    gender = "4";
                }

                if(!(phoneno.equals("")||address.equals("")||constituency.equals("")||gender.equals(""))){
                    Background_Worker background_worker = new Background_Worker(Signup.this);
                    background_worker.execute("register", fname, lname, mail, pass, address, phoneno, constituency, gender);
                    SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("consti", constituency);
                    editor.putString("email", mail);
                    editor.apply();
                    SharedPreferences sharedPreferences1 = getSharedPreferences("Signup", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("signed", "1");
                    editor1.apply();
                    Intent intent = new Intent(Signup.this, Home.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Signup.this,"Fill Details to Proceed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // Setting Negative "NO" Button

        // closed

        // Showing Alert Message
        alertDialog.show();
    }







}
