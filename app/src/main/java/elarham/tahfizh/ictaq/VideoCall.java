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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.LocaleHelper;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Global.StringUtility;


public class VideoCall extends AppCompatActivity {

    SharedPreferenceManager sharePrefMan;

    private WebView mWebRTCWebView;
    LinearLayout editLay;
    ActionBar actBar;
    String roomId, url, urlUpdateJadwal;

    String jadwalId, reqId, jam, tanggal, catatan;

    TextView jadwalTxt;
    EditText catatanTxt;
    RadioGroup nilaiRad;
    RadioButton nilaiRadBtn, radBtnA, radBtnB, radBtnC;
    Button simpanBtn;

    String nilai = "";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        jadwalTxt = findViewById(R.id.jadwalTxt);
        catatanTxt = findViewById(R.id.catatanTxt);
        nilaiRad = findViewById(R.id.nilaiRadio);
        radBtnA = findViewById(R.id.scoreA);
        radBtnB = findViewById(R.id.scoreB);
        radBtnC = findViewById(R.id.scoreC);
        simpanBtn = findViewById(R.id.simpanBtn);



        sharePrefMan = new SharedPreferenceManager(this);
        mWebRTCWebView = findViewById(R.id.main_webview);
        editLay = findViewById(R.id.editLay);

        roomId = getIntent().getStringExtra("idRoom");
        jadwalId = getIntent().getStringExtra("idJadwal");
        reqId = getIntent().getStringExtra("idReq");
        jam = getIntent().getStringExtra("jam");
        tanggal = getIntent().getStringExtra("tanggal");
        nilai = getIntent().getStringExtra("nilai");
        catatan = getIntent().getStringExtra("catatan");

        if(!nilai.equals("null")){
            if(nilai.equals("A")){
                radBtnA.setChecked(true);
            }else if(nilai.equals("B")){
                radBtnB.setChecked(true);
            }else if(nilai.equals("C")){
                radBtnC.setChecked(true);
            }

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
            mWebRTCWebView.setVisibility(View.GONE);
            editLay.setVisibility(View.VISIBLE);
        }else if(sharePrefMan.getSpType().equals("3")){
            editLay.setVisibility(View.GONE);
            openRTC();
        }

        jadwalTxt.setText(new StringUtility().exactTime(tanggal, this) + " " + jam);

        if(catatan.trim().equals("null")){
            catatanTxt.setText("");
        }else{
            catatanTxt.setText(catatan);
        }

        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                catatan = catatanTxt.getText().toString();

                if(nilaiRad.getCheckedRadioButtonId() != -1){
                    int selectedId = nilaiRad.getCheckedRadioButtonId();

                    nilaiRadBtn = findViewById(selectedId);



                    if(nilaiRadBtn.getText().equals(getApplicationContext().getString(R.string.scorea))){
                        nilai = "A";
                    }else if(nilaiRadBtn.getText().equals(getApplicationContext().getString(R.string.scoreb))){
                        nilai = "B";
                    }else if(nilaiRadBtn.getText().equals(getApplicationContext().getString(R.string.scorec))){
                        nilai = "C";
                    }

                    ubahNilai("1", nilai, catatan, urlUpdateJadwal);
                }else{
                    Toast.makeText(VideoCall.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_SHORT).show();
                }

            }
        });



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

                Map<String, String> params = new HashMap<String, String>();

                params.put("value", String.format("tgl='%s',jam='%s'", tanggal, jam));
                params.put("where", String.format("where id='%s'",jadwalId));

                return params;
            }
        };

        queue.add(strRequest);
    }


    private void ubahNilai(final String mulai, final String nilai, final String catatan, final String url){
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

                params.put("value", String.format("mulai='%s',nilai='%s',catatan='%s'",mulai, nilai, catatan));
                params.put("where", String.format("where id='%s'",jadwalId));

                return params;
            }
        };

        queue.add(strRequest);
    }




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
                editLay.setVisibility(View.GONE);
                onBackPressed();
                return true;
            case R.id.browser_menu:
                mWebRTCWebView.loadUrl("about:blank");
                mWebRTCWebView.setVisibility(View.GONE);
                editLay.setVisibility(View.GONE);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
            case R.id.room_menu:
                changeRoomId(reqId, new StringUtility().randomRoomID());
                break;
            case R.id.exam_menu:
                editLay.setVisibility(View.GONE);
                openRTC();
                break;
            case R.id.edit_menu:
                mWebRTCWebView.loadUrl("about:blank");
                mWebRTCWebView.setVisibility(View.GONE);
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
