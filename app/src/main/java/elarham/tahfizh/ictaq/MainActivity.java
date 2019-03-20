package elarham.tahfizh.ictaq;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActionBar actBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        actBar = getSupportActionBar();
        actBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);

        actBar.setIcon(R.mipmap.ic_mytahfizh);
        actBar.setTitle(" MyTahfizh");

        // kita set default nya Home Fragment
        loadFragment(new HomeFragment());

        // inisialisasi BottomNavigaionView

        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);

        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.quran_menu:
                fragment = new QuranFragment();
                break;
            case R.id.ustadz_menu:
                fragment = new UstadzFragment();
                break;
            case R.id.vidcall_menu:
                fragment = new VidcallFragment();
                break;
            case R.id.certificate_menu:
                fragment = new CertificateFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return true;
    }

//

}
