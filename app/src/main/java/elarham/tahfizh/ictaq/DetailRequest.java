package elarham.tahfizh.ictaq;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailRequest extends AppCompatActivity {

    ActionBar actBar;
    String id, idRegis, idPenguji, tanggal, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);


        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.request));
        actBar.setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");
        idRegis = getIntent().getStringExtra("idRegis");
        idPenguji = getIntent().getStringExtra("idPenguji");
        tanggal = getIntent().getStringExtra("tanggal");
        status = getIntent().getStringExtra("status");


    }
}
