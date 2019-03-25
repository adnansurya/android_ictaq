package elarham.tahfizh.ictaq;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import elarham.tahfizh.ictaq.Models.Ayat;

import static com.android.volley.VolleyLog.TAG;

public class Quran extends AppCompatActivity {

    ActionBar actBar;
    String nomorSurah, namaSurah, asmaSurah, artiSurah, ayatSurah, typeSurah, keteranganSurah;

    int ayatStart = 1;

    TextView nomorTxt, namaTxt, asmaTxt, artiTxt, ayatTxt, typeTxt, keteranganTxt;

    RecyclerView mList;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Ayat> ayatList;
    RecyclerView.Adapter adapter;

    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        nomorSurah = getIntent().getStringExtra("nomor");
        namaSurah = getIntent().getStringExtra("nama");
        asmaSurah = getIntent().getStringExtra("asma");
        artiSurah = getIntent().getStringExtra("arti");
        ayatSurah = getIntent().getStringExtra("ayat");
        typeSurah = getIntent().getStringExtra("type");
        keteranganSurah = getIntent().getStringExtra("keterangan");

        actBar = getSupportActionBar();
        actBar.setTitle(namaSurah);
        actBar.setDisplayHomeAsUpEnabled(true);

        mList = findViewById(R.id.ayatList);

        ayatList = new ArrayList<>();
        adapter = new QuranAdapter(getApplicationContext(),ayatList);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getSurahData(nomorSurah,ayatStart);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.surah_menu, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                requestQueue.getCache().clear();
                onBackPressed();
                return true;
            case R.id.info_menu:
                dialogSurahInfo();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    String status;
    JSONArray data, arabic, english, indo;

    private void getSurahData(String surahNumber, int ayatStart) {

        final ProgressDialog progressDialog = new ProgressDialog(Quran.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        String url = getApplicationContext().getString(R.string.urlquran) + "/" + surahNumber +
                getApplicationContext().getString(R.string.urledition);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                try {

                    status = response.getString("status");
                    data = response.getJSONArray("data");

                    arabic = data.getJSONObject(0).getJSONArray("ayahs");
                    indo = data.getJSONObject(1).getJSONArray("ayahs");

                    if(data.getJSONObject(0).getJSONArray("ayahs").length() ==
                            data.getJSONObject(1).getJSONArray("ayahs").length())
                    {
                        for (int i=0; i <data.getJSONObject(0).getJSONArray("ayahs").length(); i++) {

                            JSONObject arabicObj, indoObj;

                            arabicObj = arabic.getJSONObject(i);
                            indoObj = indo.getJSONObject(i);

                            Ayat ayat = new Ayat();
                            ayat.setArabic( Html.fromHtml(arabicObj.getString("text")).toString());
                            ayat.setIndo(indoObj.getString("text"));
                            ayat.setNomorAyat(arabicObj.getString("numberInSurah"));

                            ayatList.add(ayat);


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        requestQueue = Volley.newRequestQueue(Quran.this);
        requestQueue.add(jsonObjReq);

    }


    private void dialogSurahInfo() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(Quran.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_info_surah, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_info);
        dialog.setTitle(getApplicationContext().getString(R.string.surahinfo));

        nomorTxt = dialogView.findViewById(R.id.nomorTxt);
        namaTxt = dialogView.findViewById(R.id.namaTxt);
        asmaTxt = dialogView.findViewById(R.id.asmaTxt);
        artiTxt = dialogView.findViewById(R.id.artiTxt);
        ayatTxt = dialogView.findViewById(R.id.ayatTxt);
        typeTxt = dialogView.findViewById(R.id.typeTxt);
        keteranganTxt = dialogView.findViewById(R.id.keteranganTxt);


        nomorTxt.setText(nomorSurah);
        namaTxt.setText(namaSurah);
        asmaTxt.setText(asmaSurah);
        artiTxt.setText(artiSurah);
        ayatTxt.setText(ayatSurah);

        if(typeSurah.equals("mekah")){
            typeTxt.setText(getApplicationContext().getString(R.string.mekah));
        }else if(typeSurah.equals("madinah")){
            typeTxt.setText(getApplicationContext().getString(R.string.madinah));
        }

        keteranganTxt.setText(Html.fromHtml(keteranganSurah).toString());

        dialog.setPositiveButton(getApplicationContext().getString(R.string.close), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }

}
