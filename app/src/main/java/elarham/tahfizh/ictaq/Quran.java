package elarham.tahfizh.ictaq;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class Quran extends AppCompatActivity {

    ActionBar actBar;
    String nomor, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        nomor = getIntent().getStringExtra("nomor");
        nama = getIntent().getStringExtra("nama");

        actBar = getSupportActionBar();
        actBar.setTitle(nama);
        actBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
