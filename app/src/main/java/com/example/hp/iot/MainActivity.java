package com.example.hp.iot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";
    public static final int RC_SIGN_IN=1;
    NavigationView navigationView;
    SqlLiteHelper db = new SqlLiteHelper(this);
    TextView result;
    TextView name;
    TextView email;
    Button on;
    Button off;
    private Boolean isOnline()  {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected())
            return true;

        return false;
    }

    public void checkNetwork() throws Exception
    {
        if(isOnline())
        {
            TextView error_msg=(TextView)findViewById(R.id.network_status);
            error_msg.setText("Online");
        }
        else
        {
            TextView error_msg=(TextView)findViewById(R.id.network_status);
            error_msg.setText("Offline");
            Toast.makeText(getApplicationContext(),"No network",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getCurrentUser();
        result=(TextView)findViewById(R.id.upload_msg);
        final TextView led_status=(TextView)findViewById(R.id.led_status);


        on=(Button)findViewById(R.id.led_on);
        off=(Button)findViewById(R.id.led_off);

        if(isOnline()){
            TextView network_msg=(TextView)findViewById(R.id.network_status);
            network_msg.setText("Online");
        }
        on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    checkNetwork();
                    if(db.insertStatus("on")){
                        led_status.setText("On");
                        Toast.makeText(getApplicationContext(), "Inserted on status successfully",Toast.LENGTH_LONG).show();
                    }
                    else{
                        led_status.setText("Error");
                        Toast.makeText(getApplicationContext(), "error inserting",Toast.LENGTH_LONG).show();
                    }
                    new LedDataAsyncTask().execute("on","http://data.sparkfun.com/input/AJE3QqanMwfYV20dJdOj?private_key=rzR4MGmxPkHNKV0n5nJb");
                    Toast.makeText(getApplicationContext(),"ON",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("Error::"+e);
                }
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    checkNetwork();
                    if(db.insertStatus("off")) {
                        led_status.setText("Off");
                        Toast.makeText(getApplicationContext(), "Inserted off status successfully", Toast.LENGTH_LONG).show();
                    }
                    else {
                        led_status.setText("Error");
                        Toast.makeText(getApplicationContext(), "error inserting", Toast.LENGTH_LONG).show();
                    }
                    new LedDataAsyncTask().execute("off","http://data.sparkfun.com/input/AJE3QqanMwfYV20dJdOj?private_key=rzR4MGmxPkHNKV0n5nJb");
                    Toast.makeText(getApplicationContext(), "OFF",Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    //TODO Auto-generated catch block
                    System.out.println("Error::"+e);
                }

            }
        });
    }

public void onActivityResult(int requestCode, int resultCode,Intent data){

    super.onActivityResult(requestCode,resultCode,data);
    if(requestCode==RC_SIGN_IN){
        if(resultCode==RESULT_OK){

            Toast.makeText(this,"Signed In",Toast.LENGTH_SHORT).show();
        }else if(resultCode==RESULT_CANCELED) {
            Toast.makeText(this, "Sign in Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    finish();
}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
       /* if(id==R.id.sign_out){
            AuthUI.getInstance().signOut(this);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String u_name,u_email;
        View hView =  navigationView.getHeaderView(0);
        name=(TextView)hView.findViewById(R.id.current_user);
        email=(TextView)hView.findViewById(R.id.current_user_mailid);
        if (user != null) {
            // Name, email address, and profile photo Url
            u_name = user.getDisplayName();
            u_email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            name.setText(u_name);
            email.setText(u_email);
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

        }
    }
    public void setContentText(String msg) {
           result.setText(msg);
       }
    private class LedDataAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String message=HttpPostData.postData(params[0],params[1]);
            return message;
        }
        protected void onPostExecute(String msg){
            setContentText(msg);
        }
    }
    private class LedDataRetrieveAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String message=HttpGetData.GetText(params[0]);
            return message;
        }
    }

}
