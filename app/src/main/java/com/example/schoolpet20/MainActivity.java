package com.example.schoolpet20;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import lockScreen.LockScreenService;
import floatWindow.FloatService;
import petState.PetState;

import static android.widget.CompoundButton.*;
import static java.util.Arrays.sort;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ServiceConnection{
    public ImageView imageView_head;
    public ImageView imageView_pet;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public TextView textView;
    public Chronometer chronometer;
    public Switch needFloatwindow;
    public Switch needLockScreen;
    public PetState petState=new PetState();
    private LocationService.Binder binder;
    private String Location;
    private Boolean isChronmetering=false;
    private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
            Location=msg.getData().getString("data");
            switch (Location) {
                case "classroom":
                    imageView_pet.setBackgroundResource(R.drawable.anim_study);
                    if(!isChronmetering) {
                        chronometer.start();
                        isChronmetering=true;
                    }
                    break;
                case "cartoon":
                    imageView_pet.setBackgroundResource(R.drawable.anim_eat);
                    if(isChronmetering) {
                        chronometer.stop();
                        isChronmetering=false;
                    }
                    break;
                case "restroom":
                    imageView_pet.setBackgroundResource(R.drawable.anim_sleep);
                    if(isChronmetering) {
                        chronometer.stop();
                        isChronmetering=false;
                    }
                    break;
                case "playground":
                    imageView_pet.setBackgroundResource(R.drawable.anim_sport);
                    if(!isChronmetering) {
                        chronometer.start();
                        isChronmetering=true;
                    }
                    break;
                case "playroom":
                    imageView_pet.setBackgroundResource(R.drawable.anim_sport);
                    if(!isChronmetering) {
                        chronometer.start();
                        isChronmetering=true;
                    }
                    break;
                default:
                    imageView_pet.setBackgroundResource(R.drawable.anim_common);
                    if(isChronmetering) {
                        chronometer.stop();
                        isChronmetering=false;
                    }
                    break;
            }
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView_pet.getBackground();
            animationDrawable.start();
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bmob.initialize(this, "5720c9bd86279e8d4ca4ce640775164b");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        imageView_pet=(ImageView)findViewById(R.id.activity_main_imageView_pet);
        textView=(TextView)findViewById(R.id.activity_main_textView_address);
        chronometer=(Chronometer)findViewById(R.id.activity_main_chronometer);
        needFloatwindow=(Switch)findViewById(R.id.activity_main_switch_floatwindow);
        needLockScreen=(Switch)findViewById(R.id.activity_main_switch_lockScreen);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
        bindService(new Intent(this,LocationService.class),this, Context.BIND_AUTO_CREATE);

        needFloatwindow.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(MainActivity.this, FloatService.class);
                    startService(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, FloatService.class);
                    stopService(intent);
                }
            }
        });

        needLockScreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(MainActivity.this, LockScreenService.class);
                    startService(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, LockScreenService.class);
                    stopService(intent);
                }
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder=(LocationService.Binder)service;
        binder.getService().setCallback(new LocationService.CallBack(){
            public void onDataChange(String data) {
                if(Location!=data) {
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("data", data);
                    msg.setData(b);
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        unbindService(this);
        stopService(new Intent(this,LocationService.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
        //chronometer.setBase(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chronometer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
