package elarham.tahfizh.ictaq;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Global.StringUtility;

public class DetailRequest extends AppCompatActivity {

    ActionBar actBar;
    String idRequest, idRegis, idPenguji, tanggal, status;
    TextView typeTxt, tanggalTxt, namaTxt, pekerjaanTxt, alamatTxt, tgl_LahirTxt, ayah_ibuTxt, tahunTxt, emailTxt, telpTxt;
    ImageView sendEmailImg, phoneCallImg;

    CardView kontakCard;
    LinearLayout personLay;

    String nama, email, phone;
    String url, urlUpdateRequest, urlAddJadwal, urlAddRoom;

    String tglJadwal, jamJadwal;

    SharedPreferenceManager sharePrefMan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);


        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.request));
        actBar.setDisplayHomeAsUpEnabled(true);


        idRequest = getIntent().getStringExtra("id");
        idRegis = getIntent().getStringExtra("idRegis");
        idPenguji = getIntent().getStringExtra("idPenguji");
        tanggal = getIntent().getStringExtra("tanggal");
        status = getIntent().getStringExtra("status");


        tanggalTxt = findViewById(R.id.tanggalTxt);
        namaTxt = findViewById(R.id.namaTxt);
        pekerjaanTxt = findViewById(R.id.pekerjaanTxt);
        alamatTxt = findViewById(R.id.alamatTxt);
        tgl_LahirTxt = findViewById(R.id.tgl_lahirTxt);
        ayah_ibuTxt = findViewById(R.id.ayah_ibuTxt);
        tahunTxt = findViewById(R.id.tahunTxt);
        emailTxt = findViewById(R.id.emailTxt);
        telpTxt = findViewById(R.id.telpTxt);
        typeTxt = findViewById(R.id.typeTxt);

        kontakCard = findViewById(R.id.kontakCard);
        personLay = findViewById(R.id.personLay);

        sendEmailImg = findViewById(R.id.sendEmailImg);
        phoneCallImg = findViewById(R.id.phoneCallImg);


        sharePrefMan = new SharedPreferenceManager(this);


        if(sharePrefMan.getSpType().equals("3")){
            typeTxt.setText(getApplicationContext().getString(R.string.data)+ " " + getApplicationContext().getString(R.string.examiner));
            kontakCard.setVisibility(View.GONE);
            personLay.setVisibility(View.GONE);
            url = getApplicationContext().getString(R.string.urlmain) +
                    "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=user";
        }else if(sharePrefMan.getSpType().equals("2")){
            typeTxt.setText(getApplicationContext().getString(R.string.data)+ " " + getApplicationContext().getString(R.string.memorizer));
            url = getApplicationContext().getString(R.string.urlmain) +
                    "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=registrasi";

        }

        urlUpdateRequest = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=update&tabel=permintaan";

        urlAddJadwal = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=insert&tabel=jadwal";

        urlAddRoom = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=insert&tabel=room";


        tanggalTxt.setText(getApplicationContext().getString(R.string.sent)+ " : " + new StringUtility().exactTime(tanggal,this));

        getUserData();

    }

    private void getUserData(){

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
                        Log.e("URL DETAIL REQ", url);
                        Log.e("DETAIL REQ ", response);


                        try {

                            final JSONObject profile = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                            nama = profile.getString("nama");
                            email = profile.getString("email");
                            phone = profile.getString("telp");
                            namaTxt.setText(nama);
                            if(sharePrefMan.getSpType().equals("2")){
                                tahunTxt.setText(profile.getString("thn_menghafal"));
                                pekerjaanTxt.setText(profile.getString("pekerjaan"));
                                tgl_LahirTxt.setText(profile.getString("tgl_lahir"));
                                alamatTxt.setText(profile.getString("alamat") + ", " + profile.getString("kota") + ", " + profile.getString("provinsi"));
                                telpTxt.setText(phone);
                                emailTxt.setText(email);
                                ayah_ibuTxt.setText(profile.getString("ayah_ibu"));

                                sendEmailImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                        emailIntent.setData(Uri.parse("mailto:"+email));
                                        startActivity(emailIntent);
                                    }
                                });

                                phoneCallImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                        startActivity(intent);
                                    }
                                });
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailRequest.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                if(sharePrefMan.getSpType().equals("3")){
                    params.put("where", String.format("where kode='%s'",idPenguji));
                }else if(sharePrefMan.getSpType().equals("2")){
                    params.put("where", String.format("where a.id='%s'",idRegis));
                }


                return params;
            }
        };

        queue.add(strRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_request_menu, menu);

        if(sharePrefMan.getSpType().trim().equals("3") || status.equals("1")){
            MenuItem check = menu.findItem(R.id.accept_menu);
            check.setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.accept_menu:
                showScheduleDialog();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog dateDial;
    TimePickerDialog.OnTimeSetListener time;
    TimePickerDialog timeDial;
    Calendar myCalendar;

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


        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

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

        timeDial = new TimePickerDialog(DetailRequest.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),
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
                Toast.makeText(DetailRequest.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_SHORT).show();
            }else{

                updateStatusRequest(urlUpdateRequest);
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

    private void updateStatusRequest(final String url){


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
                        Log.e("URL UPDATE REQ", url);
                        Log.e("UPDATE REQ ", response);

                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                addJadwal(urlAddJadwal);
                            }else{
                                Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailRequest.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                    params.put("value", String.format("status='%s'","1"));
                    params.put("where", String.format("where id='%s'",idRequest));

                return params;
            }
        };

        queue.add(strRequest);

    }

    private void addJadwal(final String url){


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
                        Log.e("URL ADD JADWAL", url);
                        Log.e("ADD JADWAL", response);

                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                addRoom(urlAddRoom);
                            }else{
                                Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailRequest.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("field", "id_permintaan,tgl,jam,mulai");
                params.put("value", String.format("'%s','%s','%s','%s'", idRequest, tglJadwal, jamJadwal, "1" ));


                return params;
            }
        };

        queue.add(strRequest);
    }

    private void addRoom(final String url){


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
                        Log.e("URL ADD ROOM", url);
                        Log.e("ADD ROOM", response);

                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                Toast.makeText(DetailRequest.this, R.string.editsuccess, Toast.LENGTH_SHORT).show();
                                Intent terima = new Intent(DetailRequest.this, MainActivity.class);
                                startActivity(terima);
                            }else{
                                Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailRequest.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("field", "id_permintaan,id_room");
                params.put("value", String.format("'%s','%s'", idRequest, new StringUtility().randomRoomID()));


                return params;
            }
        };

        queue.add(strRequest);
    }
}
