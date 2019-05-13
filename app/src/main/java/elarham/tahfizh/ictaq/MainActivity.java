package elarham.tahfizh.ictaq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import elarham.tahfizh.ictaq.Global.LocaleHelper;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.MainFragments.*;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActionBar actBar;
    int backButtonCount = 0;
    float defaultElevation = (float) 15;

    SharedPreferenceManager sharePrefMan;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_REC_AUDIO_REQUEST_CODE = 101;

    Fragment fragment = null;

    private String initialLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialLocale = LocaleHelper.getPersistedLocale(this);
        LocaleHelper.setLocale(this,initialLocale);

        sharePrefMan = new SharedPreferenceManager(this);

        actBar = getSupportActionBar();

        actBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);

        actBar.setIcon(R.mipmap.ic_mytahfizh_black);
        actBar.setTitle(" "+ getApplicationContext().getString(R.string.app_name));


        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);

        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if(sharePrefMan.getSpType().trim().equals("2")){
            MenuItem ustadz = bottomNavigationView.getMenu().findItem(R.id.hafizh_menu);
            ustadz.setVisible(false);
            MenuItem sertifikat = bottomNavigationView.getMenu().findItem(R.id.certificate_menu);
            sertifikat.setVisible(false);
        }else if(sharePrefMan.getSpType().trim().equals("3")){
            MenuItem schedule = bottomNavigationView.getMenu().findItem(R.id.schedule_menu);
            schedule.setVisible(false);
        }



        checkRequestPermission();

        // kita set default nya Home Fragment
        loadFragment(new HomeFragment());



    }

    private void checkRequestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, MY_REC_AUDIO_REQUEST_CODE);

        }

    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length != 0){
            if (requestCode == MY_CAMERA_REQUEST_CODE) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, this.getString(R.string.cameraallowed), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, this.getString(R.string.cameranotallowed), Toast.LENGTH_LONG).show();

                }

            }else if(requestCode == MY_REC_AUDIO_REQUEST_CODE){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, this.getString(R.string.micallowed), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, this.getString(R.string.micnotallowed), Toast.LENGTH_LONG).show();

                }
            }
        }


    }//end onRequestPermissionsResult

    @Override
    protected void onPause() {
        super.onPause();
        //Log.e("FRAGMENT", fragment.getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "RESUME", Toast.LENGTH_SHORT).show();
//        Log.e("Lanjut","hahaha");
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    // method listener untuk logika pemilihan
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {


        switch (item.getItemId()){
            case R.id.home_menu:
                fragment = new HomeFragment();
                actBar.setElevation(defaultElevation);
                break;
            case R.id.quran_menu:
                fragment = new QuranFragment();
                actBar.setElevation(0);
                break;
            case R.id.hafizh_menu:
                fragment = new HafizhFragment();
                actBar.setElevation(defaultElevation);
                break;
            case R.id.schedule_menu:
                fragment = new ScheduleFragment();
                actBar.setElevation(0);
                break;
            case R.id.certificate_menu:
                fragment = new CertificateFragment();
                actBar.setElevation(defaultElevation);
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

//        if(sharePrefMan.getSpType().trim().equals("2")){
//            MenuItem check = menu.findItem(R.id.profile_menu);
//            check.setVisible(false);
//        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent profil;
        switch (item.getItemId()){
            case R.id.profile_menu:
                profil = new Intent(MainActivity.this, Profile.class);
                startActivity(profil);
                break;
            case R.id.settings_menu:
                profil = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(profil);
                break;
            case R.id.logout_menu:
                SharedPreferenceManager sharePrefMan = new SharedPreferenceManager(this);
                sharePrefMan.logout();
                profil = new Intent(MainActivity.this, Login.class);
                startActivity(profil);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getApplicationContext().getString(R.string.backquit), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
