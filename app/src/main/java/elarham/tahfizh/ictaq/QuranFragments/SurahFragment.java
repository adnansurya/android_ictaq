package elarham.tahfizh.ictaq.QuranFragments;

import android.app.ProgressDialog;
import android.graphics.Movie;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import elarham.tahfizh.ictaq.Models.Surah;
import elarham.tahfizh.ictaq.R;

import static com.android.volley.VolleyLog.TAG;


public class SurahFragment extends Fragment {

    RecyclerView mList;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Surah> surahList;
    RecyclerView.Adapter adapter;

    String url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surah, container, false);

        mList = view.findViewById(R.id.surahList);

        surahList = new ArrayList<>();
        adapter = new SurahAdapter(getContext(),surahList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(adapter);

        url = getContext().getString(R.string.urlsurah);

        getData();

        return view;
    }

    String status;
    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());

                try {
                    status = response.getString("status");
                    JSONArray hasil = response.getJSONArray("hasil");
                    for (int i=0; i <hasil.length(); i++){

                        JSONObject jsonObj = hasil.getJSONObject(i);

                        Surah surah = new Surah();
                        surah.setNama(jsonObj.getString("nama"));
                        surah.setType(jsonObj.getString("type"));
                        surah.setNomor(jsonObj.getString("nomor"));
                        surah.setAyat(jsonObj.getString("ayat"));
                        surah.setAsma(jsonObj.getString("asma"));
                        surah.setArti(jsonObj.getString("arti"));

                        surahList.add(surah);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjReq);


    }
}
