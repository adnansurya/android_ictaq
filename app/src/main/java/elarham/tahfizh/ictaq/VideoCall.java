package elarham.tahfizh.ictaq;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.LocaleHelper;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Global.StringUtility;

import static android.view.View.GONE;


public class VideoCall extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {

    SharedPreferenceManager sharePrefMan;

    private WebView mWebRTCWebView;
    LinearLayout editLay;
    ActionBar actBar;
    String roomId, url, urlUpdateJadwal;

    String jadwalId, reqId, jam, tanggal, catatan;

    TextView jadwalTxt;
    EditText catatanTxt;

    Button simpanBtn;
    Button juzBtn;

    String nilai = "";

    boolean[] checkedJuz;
    StringBuilder selectedJuz, nilaiJuz;
    RatingBar rb;
    LinearLayout scoreLay;
    CardView scoreCard;

    HashMap<String,String> nilaiJuzMap;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        jadwalTxt = findViewById(R.id.jadwalTxt);

        catatanTxt = findViewById(R.id.catatanTxt);

        simpanBtn = findViewById(R.id.simpanBtn);
        juzBtn = findViewById(R.id.juzBtn);


        checkedJuz = new boolean[30];


        sharePrefMan = new SharedPreferenceManager(this);
        mWebRTCWebView = findViewById(R.id.main_webview);
        editLay = findViewById(R.id.editLay);
        scoreLay = findViewById(R.id.scoreLay);
        scoreCard = findViewById(R.id.card_view_score);

        roomId = getIntent().getStringExtra("idRoom");
        jadwalId = getIntent().getStringExtra("idJadwal");
        reqId = getIntent().getStringExtra("idReq");

        if(sharePrefMan.getSpType().equals("2")){
            jam = getIntent().getStringExtra("jam");
            tanggal = getIntent().getStringExtra("tanggal");
            nilai = getIntent().getStringExtra("nilai");
            catatan = getIntent().getStringExtra("catatan");
        }







        Log.e("ROOM ID", roomId);

        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.exam));
        actBar.setDisplayHomeAsUpEnabled(true);

        setUpWebViewDefaults(mWebRTCWebView);

        url = "https://appr.tc/r/" + roomId+ "?stereo=false&backasc=ISAC/16000&hd=false";

        urlUpdateJadwal = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=update&tabel=jadwal";

        if(sharePrefMan.getSpType().equals("2")){
            mWebRTCWebView.setVisibility(GONE);
            editLay.setVisibility(View.VISIBLE);
            jadwalTxt.setText(new StringUtility().exactTime(tanggal, this) + " " + jam);
            if(catatan.trim().equals("null")){
                catatanTxt.setText("");
            }else{
                catatanTxt.setText(catatan);
            }
        }else if(sharePrefMan.getSpType().equals("3")){
            editLay.setVisibility(GONE);
            openRTC();
        }


        simpanBtn.setEnabled(false);
        scoreCard.setVisibility(View.INVISIBLE);



        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                catatan = catatanTxt.getText().toString();

                String[] data = selectedJuz.toString().split(";");
                Log.e("Ukuran Data", String.valueOf(data.length) + " " + String.valueOf(nilaiJuzMap.size()));
                if(data.length != nilaiJuzMap.size()){
                    Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_SHORT).show();
                }else{
                    nilaiJuz = new StringBuilder();
                    selectedJuz = new StringBuilder();
                    Log.e("Juz", nilaiJuzMap.toString());
                    Iterator it = nilaiJuzMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        //System.out.println(pair.getKey() + " = " + pair.getValue());
                        selectedJuz.append(pair.getKey());
                        selectedJuz.append(";");
                        nilaiJuz.append(pair.getValue());
                        nilaiJuz.append(";");
                        it.remove(); // avoids a ConcurrentModificationException
                    }

                    if(selectedJuz.length()>0 && nilaiJuz.length()>0){
                        selectedJuz.deleteCharAt(selectedJuz.length()-1);
                        nilaiJuz.deleteCharAt(nilaiJuz.length()-1);
                        ubahNilai("2",nilaiJuz.toString(), selectedJuz.toString(), catatan, urlUpdateJadwal );

                    }else{
                        Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });


        juzBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseJuz();
            }
        });




    }
    AlertDialog alert;
    AlertDialog.Builder builder;
    private void chooseJuz(){
        List<String> listItems = new ArrayList<String>();
        for(int i=0; i<30; i++){
            listItems.add(getApplicationContext().getString(R.string.juz) + " " + String.valueOf(i+1));
        }
        final CharSequence[] allJuz = listItems.toArray(new CharSequence[listItems.size()]);



        builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getString(R.string.selecttestedjuz));
        builder.setIcon(R.drawable.ic_local_library);
        builder.setMultiChoiceItems(allJuz, checkedJuz, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                checkedJuz[item] = isChecked;

            }
        });

        builder.setPositiveButton(getApplicationContext().getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        scoreLay.removeAllViews();
                        selectedJuz = new StringBuilder("");
                        nilaiJuzMap = new HashMap<>();
                        for(int i=0; i<checkedJuz.length; i++){
                            if(checkedJuz[i]){

                                TextView txt = new TextView(VideoCall.this);
                                txt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                scoreLay.addView(txt);

                                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.juzSelectTxt));
                                txt.setText(getApplicationContext().getString(R.string.juz) +" "+  String.valueOf(i+1));

                                selectedJuz.append(String.valueOf(i+1));
                                selectedJuz.append(";");
                                rb = new RatingBar(VideoCall.this, null, android.R.attr.ratingBarStyleIndicator);
                                rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                scoreLay.addView(rb);
                                rb.setId(i+1);
                                rb.setTag(i+1);
                                rb.setNumStars(5);
                                rb.setStepSize(Float.parseFloat("1.0"));
                                rb.setRating(Float.parseFloat("0.0"));
                                rb.setIsIndicator(false);
                                rb.setOnRatingBarChangeListener(VideoCall.this);
                            }

                        }
                        if(selectedJuz.length()>0){
                            selectedJuz.deleteCharAt(selectedJuz.length()-1);
                            simpanBtn.setEnabled(true);
                            scoreCard.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_SHORT).show();
                            simpanBtn.setEnabled(false);
                            scoreCard.setVisibility(View.INVISIBLE);
                        }

//                        Toast.makeText(VideoCall.this, String.valueOf(alert.getListView().getCheckedItemPositions()), Toast.LENGTH_SHORT).show();
                        Log.e("SelectedJUZ", selectedJuz.toString());
                    }
                });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        String getJuz = ratingBar.getTag().toString();
        int nilaiSatuJuz = Math.round(rating);


        nilaiJuzMap.put(getJuz, String.valueOf(nilaiSatuJuz));

        Log.e("Nilai juz : ", nilaiJuzMap.toString());
    }
    private void setWebChromeClient(){

        final ProgressDialog progressDialog = new ProgressDialog(VideoCall.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        mWebRTCWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                Log.e("PROGRESS", String.valueOf(newProgress));
                if(newProgress == 100){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                request.grant(request.getResources());

            }



        });
    }

    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog dateDial;
    TimePickerDialog.OnTimeSetListener time;
    TimePickerDialog timeDial;
    Calendar myCalendar;
    String tglJadwal, jamJadwal;

    @SuppressLint("ClickableViewAccessibility")
    private void showScheduleDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_make_schedule, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        final EditText tglTxt, waktuTxt;
        tglTxt = dialogView.findViewById(R.id.tanggalTxt);
        waktuTxt = dialogView.findViewById(R.id.waktuTxt);

        tglTxt.setText(tanggal);
        waktuTxt.setText(jam);


        myCalendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd h:mm a"; //In which you need put here
        SimpleDateFormat sdfNow = new SimpleDateFormat(myFormat, Locale.US);
        try {
            myCalendar.setTime(sdfNow.parse(tanggal+" "+jam));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                tglTxt.setText(sdf.format(myCalendar.getTime()));
                dateDial.dismiss();
            }
        };

        dateDial = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        tglTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dateDial.show();
                return false;
            }
        });




        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);


                String myFormat = "h:mm a"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                waktuTxt.setText(sdf.format(myCalendar.getTime()));

                timeDial.dismiss();
            }
        };

        timeDial = new TimePickerDialog(this, time, myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),false);

        waktuTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                timeDial.show();
                return false;
            }
        });

        builder.setTitle(getApplicationContext().getString(R.string.makeschedule));

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                tglJadwal = tglTxt.getText().toString();
                jamJadwal = waktuTxt.getText().toString();

                if(tglJadwal.equals("") || jamJadwal.equals("")){
                    Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_SHORT).show();
                }else{

                    ubahJadwal(tglJadwal, jamJadwal, urlUpdateJadwal);
                }
                dialog.dismiss();

            }
        });

        builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void ubahJadwal(final String tanggal, final String jam, final String url){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL UPDATE JADWAL", url);
                        Log.e("UPDATE JADWAL ", response);

                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(VideoCall.this, MainActivity.class);
                                startActivity(home);
                            }else{
                                Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VideoCall.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<>();

                params.put("value", String.format("tgl='%s',jam='%s'", tanggal, jam));
                params.put("where", String.format("where id='%s'",jadwalId));

                return params;
            }
        };

        queue.add(strRequest);
    }


    private void ubahJuz( final String juz, final String url){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL UPDATE JADWAL", url);
                        Log.e("UPDATE JADWAL ", response);

                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(VideoCall.this, MainActivity.class);
                                startActivity(home);
                            }else{
                                Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VideoCall.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<>();

                params.put("value", String.format("juz='%s'", juz));
                params.put("where", String.format("where id='%s'",jadwalId));

                return params;
            }
        };

        queue.add(strRequest);
    }



    private void ubahNilai(final String mulai, final String nilai, final String juz, final String catatan, final String url){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL UPDATE NILAI", url);
                        Log.e("UPDATE NILAI", response);

                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(VideoCall.this, MainActivity.class);
                                startActivity(home);
                            }else{
                                Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VideoCall.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("value", String.format("mulai='%s',juz='%s',nilai_juz='%s',catatan='%s'",mulai, juz, nilai, catatan));
                params.put("where", String.format("where id='%s'",jadwalId));

                return params;
            }
        };

        queue.add(strRequest);
    }

//    private void ubahHafalan(final String id_regis, final String nilai, final String juz){
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
//        progressDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//
//        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response)
//                    {
//                        Log.e("URL UPDATE NILAI", url);
//                        Log.e("UPDATE NILAI", response);
//
//                        try {
//                            JSONObject simpan = new JSONObject(response);
//                            if(simpan.getString("status").equals("sukses")){
//                                Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
//                                Intent home = new Intent(VideoCall.this, MainActivity.class);
//                                startActivity(home);
//                            }else{
//                                Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(VideoCall.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
//                        }
//
//                        progressDialog.dismiss();
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
//                        Log.e("Volley Error", error.toString());
//                        progressDialog.dismiss();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams()
//            {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("value", String.format("mulai='%s',juz='%s',nilai_juz='%s',catatan='%s'",mulai, juz, nilai, catatan));
//                params.put("where", String.format("where id='%s'",jadwalId));
//
//                return params;
//            }
//        };
//
//        queue.add(strRequest);
//    }




    @Override
    public void onStop() {
        super.onStop();

        /**
         * When the application falls into the background we want to stop the media stream
         * such that the camera is free to use by other apps.
         */
        Log.e("VIDCALL", "STOP");

        mWebRTCWebView.loadUrl("about:blank");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebRTCWebView.loadUrl("about:blank");
        Log.e("VIDCALL", "PAUSE");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();



        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(mWebRTCWebView, true);
    }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.videocall_menu, menu);

         if(sharePrefMan.getSpType().equals("3")){
             MenuItem check = menu.findItem(R.id.edit_menu);
             check.setVisible(false);
             MenuItem changeRoom = menu.findItem(R.id.room_menu);
             changeRoom.setVisible(false);
             MenuItem reschedule = menu.findItem(R.id.reschedule_menu);
             reschedule.setVisible(false);
         }

        return true;
    }

    public void openRTC(){
        mWebRTCWebView.setVisibility(View.VISIBLE);
        mWebRTCWebView.loadUrl(url);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED){

            setWebChromeClient();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case android.R.id.home:
                mWebRTCWebView.loadUrl("about:blank");
                editLay.setVisibility(GONE);
                onBackPressed();
                return true;
            case R.id.browser_menu:
                mWebRTCWebView.loadUrl("about:blank");
                mWebRTCWebView.setVisibility(GONE);
                editLay.setVisibility(GONE);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
            case R.id.room_menu:
                changeRoomId(reqId, new StringUtility().randomRoomID());
                break;
            case R.id.exam_menu:
                editLay.setVisibility(GONE);
                openRTC();
                break;
            case R.id.edit_menu:
                mWebRTCWebView.loadUrl("about:blank");
                mWebRTCWebView.setVisibility(GONE);
                editLay.setVisibility(View.VISIBLE);
                break;
            case R.id.reschedule_menu:
                showScheduleDialog();
        }


        return super.onOptionsItemSelected(item);
    }

    public void changeRoomId(final String idReq, final String newRoomId){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        url = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=update&tabel=room";



        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL UPDATE ROOM", url);
                        Log.e("UPDATE ROOM ", response);

                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                roomId = newRoomId;
                                Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VideoCall.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(VideoCall.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("value", String.format("id_room='%s'", newRoomId));
                params.put("where", String.format("where id_permintaan='%s'",idReq));

                return params;
            }
        };

        queue.add(strRequest);
    }


}
