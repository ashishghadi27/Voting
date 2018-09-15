package com.rootdevs.ashish.voting;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ashish on 12/3/18.
 */

public class Background_Worker extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    String TAG = "testPhp";
    private ProgressDialog mDialog;
    Background_Worker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... voids) {
        String type = voids[0];
        String login_url = "http://www.rinebars.com/api/login.php";
        String register_url = "http://paxten.in/e-vote/register.php";
        String candidate_url = "http://paxten.in/e-vote/candidate.php";
        String vote_url = "http://paxten.in/e-vote/vote.php";

        if(type.equals("login")){
            try {
                String user_name = voids[1];
                String password = voids[2];
                Log.v(TAG, user_name);
                Log.v(TAG, password);
                URL url = new URL((login_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name",  "UTF-8")+"="+URLEncoder.encode(user_name,  "UTF-8")+"&"+URLEncoder.encode("password",  "UTF-8")+"="+URLEncoder.encode(password,  "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(type.equals("register")){
            try {
                String fname = voids[1];
                String lname = voids[2];
                String email = voids[3];
                String password = voids[4];
                String address = voids[5];
                String phone = voids[6];
                String constituency = voids[7];
                String gender = voids[8];
                Log.v(TAG, email);
                Log.v(TAG, constituency);
                URL url = new URL((register_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                Log.v("RESULT:", "Before Post Data");
                String post_data = URLEncoder.encode("first_name",  "UTF-8")+"="+URLEncoder.encode(fname,  "UTF-8")+"&"
                        +URLEncoder.encode("last_name",  "UTF-8")+"="+URLEncoder.encode(lname,  "UTF-8")+"&"
                        +URLEncoder.encode("email",  "UTF-8")+"="+URLEncoder.encode(email,  "UTF-8")+"&"
                        +URLEncoder.encode("password",  "UTF-8")+"="+URLEncoder.encode(password,  "UTF-8")+"&"
                        +URLEncoder.encode("address",  "UTF-8")+"="+URLEncoder.encode(address,  "UTF-8")+"&"
                        +URLEncoder.encode("phone",  "UTF-8")+"="+URLEncoder.encode(phone,  "UTF-8")+"&"
                        +URLEncoder.encode("constituency",  "UTF-8")+"="+URLEncoder.encode(constituency,  "UTF-8")+"&"
                        +URLEncoder.encode("gender",  "UTF-8")+"="+URLEncoder.encode(gender,  "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.v("RESULT:", "After Post Data");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.v("RESULT:", "Code Executed");
                Log.v("RESULT:", result);
                return "dismiss";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(type.equals("candidate")){
            try {
                String constituency = voids[1];
                Log.v(TAG, constituency);
                URL url = new URL((candidate_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                Log.v("RESULT:", "Before Post Data");
                String post_data = URLEncoder.encode("constituency",  "UTF-8")+"="+URLEncoder.encode(constituency,  "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.v("RESULT:", "After Post Data");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.v("RESULT:", "Code Executed");
                Log.v("RESULT:", result);
                SharedPreferences sharedPreferences = context.getSharedPreferences("Candidate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("data", result);
                editor.apply();
                return "dismiss";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(type.equals("vote")){
            try {
                String constituency = voids[1];
                String candidate_id = voids[2];
                String email = voids[3];
                Log.v(TAG, constituency);
                Log.v(TAG, email);
                URL url = new URL((vote_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                Log.v("RESULT:", "Before Post Data");
                String post_data = URLEncoder.encode("constituency",  "UTF-8")+"="+URLEncoder.encode(constituency,  "UTF-8")+"&"
                        +URLEncoder.encode("candidate_id",  "UTF-8")+"="+URLEncoder.encode(candidate_id,  "UTF-8")+"&"
                        +URLEncoder.encode("email",  "UTF-8")+"="+URLEncoder.encode(email,  "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.v("RESULT:", "After Post Data");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.v("RESULT:", "Code Executed");
                Log.v("RESULT:", ""+result);
                SharedPreferences sharedPreferences = context.getSharedPreferences("Candidate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("data", result);
                editor.apply();
                return "dismiss";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("Loading");
        mDialog.show();

    }

    @Override
    protected void onPostExecute(String result) {

        if(result.equals("dismiss")){
            mDialog.dismiss();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

