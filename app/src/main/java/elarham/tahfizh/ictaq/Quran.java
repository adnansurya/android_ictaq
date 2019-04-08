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
import android.widget.LinearLayout;
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
import java.util.Iterator;
import java.util.List;

import elarham.tahfizh.ictaq.Models.Ayat;

import static com.android.volley.VolleyLog.TAG;

public class Quran extends AppCompatActivity {

    ActionBar actBar;
    String mode, nomor, namaSurah, asmaSurah, artiSurah, ayatSurah, typeSurah, keteranganSurah;


    int ayatStart = 1;

    TextView nomorTxt, namaTxt, asmaTxt, artiTxt, ayatTxt, typeTxt, keteranganTxt, juzTxt;

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

        actBar = getSupportActionBar();
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

        mode = getIntent().getStringExtra("mode");
        nomor = getIntent().getStringExtra("nomor");

        if(mode.equals("surah")){


            namaSurah = getIntent().getStringExtra("nama");
            asmaSurah = getIntent().getStringExtra("asma");
            artiSurah = getIntent().getStringExtra("arti");
            ayatSurah = getIntent().getStringExtra("ayat");
            typeSurah = getIntent().getStringExtra("type");
            keteranganSurah = getIntent().getStringExtra("keterangan");

            actBar.setTitle(namaSurah);

        }else if(mode.equals("juz")){
            actBar.setTitle("Juz "+ nomor);
        }


        getSurahData(mode,nomor,ayatStart);


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
    JSONArray arabic, english, indo;
    JSONObject surahJuz;

    private void getSurahData(final String mode, String number, int ayatStart) {
        String url = null;
        final ProgressDialog progressDialog = new ProgressDialog(Quran.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        if(mode.equals("surah")){
            url = getApplicationContext().getString(R.string.urlquran) + "/surah/" + number + "/editions" +
                    getApplicationContext().getString(R.string.urledition);
        }else if(mode.equals("juz")){
            url = getApplicationContext().getString(R.string.urlquran) + "/juz/" + number +
                    getApplicationContext().getString(R.string.urledition);
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                try {

                    status = response.getString("status");



                    if(mode.equals("surah")){

                        arabic = response.getJSONArray("data").getJSONObject(0).getJSONArray("ayahs");
                        indo = response.getJSONArray("data").getJSONObject(1).getJSONArray("ayahs");
                        english = response.getJSONArray("data").getJSONObject(2).getJSONArray("ayahs");
                    }else if(mode.equals("juz")){

                        arabic = response.getJSONObject("data").getJSONArray("ayahs");
                        surahJuz = response.getJSONObject("data").getJSONObject("surahs");
                    }



                    for (int i=0; i <arabic.length(); i++) {

                        JSONObject arabicObj = arabic.getJSONObject(i);

                        Ayat ayat = new Ayat();
                        ayat.setArabic( Html.fromHtml(arabicObj.getString("text")).toString());
                        ayat.setNomorAyat(arabicObj.getString("numberInSurah"));
                        if(mode.equals("surah")){
                            ayat.setIndo(indo.getJSONObject(i).getString("text"));
                            ayat.setEnglish(english.getJSONObject(i).getString("text"));
                        }

                        ayatList.add(ayat);
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
        LinearLayout juzLay, surahLay;
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_info);

        juzLay = dialogView.findViewById(R.id.juzLay);
        surahLay = dialogView.findViewById(R.id.surahLay);

        nomorTxt = dialogView.findViewById(R.id.nomorTxt);
        namaTxt = dialogView.findViewById(R.id.namaTxt);
        asmaTxt = dialogView.findViewById(R.id.asmaTxt);
        artiTxt = dialogView.findViewById(R.id.artiTxt);
        ayatTxt = dialogView.findViewById(R.id.ayatTxt);
        typeTxt = dialogView.findViewById(R.id.typeTxt);
        juzTxt = dialogView.findViewById(R.id.juzTxt);
        keteranganTxt = dialogView.findViewById(R.id.keteranganTxt);

        if(mode.equals("surah")){
            dialog.setTitle(getApplicationContext().getString(R.string.surahinfo));
            juzLay.setVisibility(View.GONE);

            nomorTxt.setText(nomor);
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

        }else if(mode.equals("juz")){
            dialog.setTitle(getApplicationContext().getString(R.string.surahlist));
            surahLay.setVisibility(View.GONE);
            String surahInJuz = "";
            for(Iterator<String> iter = surahJuz.keys(); iter.hasNext();) {
                String key = iter.next();
                try {
                    Object value = surahJuz.getJSONObject(key);

                    surahInJuz += key + ". " + ((JSONObject) value).getString("englishName") + "\n";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            juzTxt.setText(surahInJuz);
        }

        dialog.setPositiveButton(getApplicationContext().getString(R.string.close), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

}
