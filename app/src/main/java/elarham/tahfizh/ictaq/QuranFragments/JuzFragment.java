package elarham.tahfizh.ictaq.QuranFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import elarham.tahfizh.ictaq.Models.Juz;
import elarham.tahfizh.ictaq.R;

public class JuzFragment extends Fragment {

    RecyclerView mList;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Juz> juzList;
    RecyclerView.Adapter adapter;

    String url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_only, container, false);

        mList = view.findViewById(R.id.recycleList);

        juzList = new ArrayList<>();
        adapter = new JuzAdapter(getContext(),juzList);


        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(adapter);

        for(int i=0; i<30; i++){
            Juz juz = new Juz();
            juz.setNomor(String.valueOf(i+1));
            juzList.add(juz);
        }
        adapter.notifyDataSetChanged();

        return view;
    }
}
