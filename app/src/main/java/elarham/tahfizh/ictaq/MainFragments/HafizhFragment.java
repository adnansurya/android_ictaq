package elarham.tahfizh.ictaq.MainFragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Global.StringUtility;
import elarham.tahfizh.ictaq.Models.Jadwal;
import elarham.tahfizh.ictaq.Models.User;
import elarham.tahfizh.ictaq.R;
import elarham.tahfizh.ictaq.ScheduleFragments.ReadyAdapter;


public class HafizhFragment extends Fragment {

    RecyclerView mList;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Jadwal> readyList;
    RecyclerView.Adapter adapter;
    FloatingActionButton fab;
    SharedPreferenceManager sharePrefMan;

    RelativeLayout reqIsReadyLayout, reqNotReadyLayout;
    TextView judulTxt, tanggalTxt, waktuTxt;


    String url;
    String id_penguji;

    StringBuilder reqSiap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hafalan, container, false);

        mList = view.findViewById(R.id.recycleList);
        fab = view.findViewById(R.id.fab);

        reqIsReadyLayout = view.findViewById(R.id.reqIsReadyLayout);
        reqNotReadyLayout = view.findViewById(R.id.reqNotReadyLayout);

        judulTxt = view.findViewById(R.id.judulTxt);
        tanggalTxt = view.findViewById(R.id.tanggalTxt);
        waktuTxt = view.findViewById(R.id.jamTxt);


        readyList = new ArrayList<>();
        adapter = new ReadyAdapter(getContext(),readyList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        sharePrefMan = new SharedPreferenceManager(getContext());

        fab.setTranslationY(-24);

        url = getContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=permintaan";
        reqSiap = new StringBuilder("(");

        getLastReq();


        getReqAccData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              

                final Calendar myCalendar = Calendar.getInstance();
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);



                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle(getContext().getString(R.string.examrequest));
                builder.setMessage(getContext().getString(R.string.examrequestdialog));

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        requestJadwal(sharePrefMan.getSpKode(), id_penguji, String.valueOf(sdf.format(myCalendar.getTime())),"0");
                        getIdPenguji();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        return view;
    }


    private void getLastReq(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL LAST REQ", url);
                        Log.e("LAST REQ", response);

                        try {
                            String status, tanggal, id;
                            JSONArray dataQuery = new JSONObject(response).getJSONArray("data");
                            if(dataQuery.length()==0){
                                fab.show();
                                reqIsReadyLayout.setVisibility(View.GONE);
                                reqNotReadyLayout.setVisibility(View.GONE);

                                Toast.makeText(getContext(), getContext().getString(R.string.schedule) + " " + getContext().getString(R.string.notavailable), Toast.LENGTH_SHORT).show();

                            }else{
                                fab.hide();
                                id =  dataQuery.getJSONObject(0).getString("id");
                                status = dataQuery.getJSONObject(0).getString("status");
                                tanggal = dataQuery.getJSONObject(0).getString("tgl");
                                if(status.equals("0")){
                                    reqIsReadyLayout.setVisibility(View.GONE);
                                    reqNotReadyLayout.setVisibility(View.VISIBLE);
                                    judulTxt.setText(getContext().getString(R.string.sent) + " : " + new StringUtility().relativeTime(tanggal, getContext()));
                                }else if(status.equals("1")){
                                    reqIsReadyLayout.setVisibility(View.VISIBLE);
                                    reqNotReadyLayout.setVisibility(View.GONE);
                                    url = getContext().getString(R.string.urlmain) +
                                            "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=jadwal";
                                    getLastReady(id);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                if(sharePrefMan.getSpType().equals("3")){
                    params.put("where", String.format("where id_regis='%s' order by id desc limit 1",sharePrefMan.getSpKode() ));
                }else if(sharePrefMan.getSpType().equals("2")){
                    params.put("where", String.format("where id_penguji='%s' order by id desc limit 1",sharePrefMan.getSpKode() ));
                }
                return params;
            }
        };

        requestQueue.add(strRequest);
    }

    private void getLastReady(final String id){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL LAST READY", url);
                        Log.e("LAST READY", response);

                        try {
                            String mulai, tanggal, waktu, nilai, catatan;
                            JSONArray dataQuery = new JSONObject(response).getJSONArray("data");
                            if(dataQuery.length()==0){
                                Toast.makeText(getContext(), getContext().getString(R.string.data) + " " + getContext().getString(R.string.notavailable), Toast.LENGTH_SHORT).show();

                            }else{

                                mulai =  dataQuery.getJSONObject(0).getString("mulai");
                                waktu =  dataQuery.getJSONObject(0).getString("jam");
                                nilai = dataQuery.getJSONObject(0).getString("nilai");
                                catatan = dataQuery.getJSONObject(0).getString("catatan");
                                tanggal = dataQuery.getJSONObject(0).getString("tgl");
                                if(mulai.equals("0")){
                                    waktuTxt.setText(waktu);
                                    tanggalTxt.setText(new StringUtility().relativeTime(tanggal, getContext()));
                                }else if(mulai.equals("1")){
                                    reqIsReadyLayout.setVisibility(View.GONE);
                                    fab.show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                if(sharePrefMan.getSpType().equals("3")){
                    params.put("where", String.format("where  id_permintaan='%s' ", id ));
                }
                return params;
            }
        };

        requestQueue.add(strRequest);
    }


    private void getReqAccData(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL REQ READY", url);
                        Log.e("REQ READY", response);

                        try {

                            JSONArray request = new JSONObject(response).getJSONArray("data");

                            if(request.length()>0){
                                for (int i=0; i <request.length(); i++){

                                    JSONObject jsonObj = request.getJSONObject(i);

                                    String id = jsonObj.getString("id");

                                    reqSiap.append(id);
                                    reqSiap.append(",");


                                }
                                reqSiap.deleteCharAt(reqSiap.length()-1);
                                reqSiap.append(")");

                                url = getContext().getString(R.string.urlmain) +
                                        "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=jadwal";
                                getReadyData();
                            }else{
                                Toast.makeText(getContext(), getContext().getString(R.string.history) + " " + getContext().getString(R.string.notavailable), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), getContext().getString(R.string.wrongdataformat), Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                if(sharePrefMan.getSpType().equals("3")){
                    params.put("where", String.format("where id_regis='%s' AND status='1'",sharePrefMan.getSpKode() ));
                }else if(sharePrefMan.getSpType().equals("2")){
                    params.put("where", String.format("where id_penguji='%s'AND status='1'",sharePrefMan.getSpKode() ));
                }
                return params;
            }
        };

        requestQueue.add(strRequest);
    }

    private void getReadyData() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL READY", url);
                        Log.e("READY", response);

                        try {

                            JSONArray ready = new JSONObject(response).getJSONArray("data");
                            for (int i=0; i <ready.length(); i++){

                                JSONObject jsonObj = ready.getJSONObject(i);

                                Jadwal jadwal = new Jadwal();
                                jadwal.setId(jsonObj.getString("id"));
                                jadwal.setIdReq(jsonObj.getString("id_permintaan"));
                                jadwal.setTanggal(jsonObj.getString("tgl"));
                                jadwal.setJam(jsonObj.getString("jam"));
                                jadwal.setMulai(jsonObj.getString("mulai"));
                                jadwal.setNilai(jsonObj.getString("nilai"));
                                jadwal.setCatatan(jsonObj.getString("catatan"));

                                readyList.add(jadwal);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), getContext().getString(R.string.wrongdataformat), Toast.LENGTH_SHORT).show();
                        }



                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("where", String.format("where id_permintaan IN %s AND mulai='1' order by id desc",reqSiap.toString()));
                return params;
            }
        };

        requestQueue.add(strRequest);


    }


    private void getIdPenguji(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = getContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=penguji";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("PENGUJI", response);
                        progressDialog.dismiss();

                        try {

                            id_penguji = new JSONObject(response).getJSONArray("data").getJSONObject(0).getString("id");

                            Toast.makeText(getContext(), id_penguji, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("where", "order by rand() limit 1");


                return params;
            }
        };

        queue.add(strRequest);
    }
    private void requestJadwal(final String id_regis, final String id_penguji, final String tanggal, final String status){



        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = getContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=insert&tabel=permintaan";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("PROFILE EDIT SIMPAN", response);
                        progressDialog.dismiss();

//                        Intent ubah = new Intent(ProfileEdit.this, MainActivity.class);
//                        startActivity(ubah);
                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                Toast.makeText(getContext(), R.string.registerok, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("field", "id_regis, id_penguji, tgl, status");

                //params.put("value", String.format("'%s','%s','%s','%s'",sharePrefMan.getSpKode(), user.getKode(), sdf.format(myCalendar.getTime()), "0"));
                params.put("value", String.format("'%s','%s','%s','%s'",id_regis, id_penguji, tanggal, status));
                return params;
            }
        };

        queue.add(strRequest);

    }
}