package elarham.tahfizh.ictaq;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;
import elarham.tahfizh.ictaq.Models.*;


public class QuranAdapter extends RecyclerView.Adapter<QuranAdapter.ViewHolder> {

    private Context context;
    private List<Ayat> list;

    public QuranAdapter(Context context, List<Ayat> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_ayat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ayat Ayat = list.get(position);

        holder.arabicTxt.setText(Ayat.getArabic());
        holder.lafazTxt.setText(Ayat.getLafaz());
        holder.indoTxt.setText(Ayat.getIndo());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView arabicTxt, lafazTxt, indoTxt;

        public ViewHolder(View itemView) {
            super(itemView);

            arabicTxt = itemView.findViewById(R.id.arabicTxt);
            lafazTxt = itemView.findViewById(R.id.lafazTxt);
            indoTxt = itemView.findViewById(R.id.indoTxt);

        }
    }

}
