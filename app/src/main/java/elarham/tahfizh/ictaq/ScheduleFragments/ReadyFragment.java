package elarham.tahfizh.ictaq.ScheduleFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Models.Jadwal;
import elarham.tahfizh.ictaq.Models.Request;
import elarham.tahfizh.ictaq.R;

public class ReadyFragment extends Fragment {

    RecyclerView mList;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Jadwal> readyList;
    RecyclerView.Adapter adapter;
    SharedPreferenceManager sharePrefMan;

    String url;
    StringBuilder reqSiap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_only, container, false);
        sharePrefMan = new SharedPreferenceManager(getContext());

        mList = view.findViewById(R.id.recycleList);

        readyList = new ArrayList<>();
        adapter = new ReadyAdapter(getContext(),readyList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        url = getContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=permintaan";

        reqSiap = new StringBuilder("(");

        getReqData();


        return view;
    }

    private void getReqData(){
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
                            getData();

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

    private void getData() {

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
                params.put("where", String.format("where id_permintaan IN %s order by id desc",reqSiap.toString()));
                return params;
            }
        };

        requestQueue.add(strRequest);


    }
}