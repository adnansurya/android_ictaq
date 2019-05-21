package elarham.tahfizh.ictaq.AchievementFragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import elarham.tahfizh.ictaq.Models.Hafalan;

import elarham.tahfizh.ictaq.R;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private Context context;
    private List<Hafalan> list;

    public AchievementAdapter(Context context, List<Hafalan> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AchievementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_hafalan, parent, false);
        return new AchievementAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AchievementAdapter.ViewHolder holder, int position) {
        Hafalan hafalan = list.get(position);
        holder.juzTxt.setText(hafalan.getJuzHafalan());
        holder.nilaiBar.setRating(Float.parseFloat(hafalan.getNilaiHafalan()));
        holder.nilaiBar.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView juzTxt;
        public RatingBar nilaiBar;

        public ViewHolder(View itemView) {
            super(itemView);


            juzTxt = itemView.findViewById(R.id.juzTxt);
            nilaiBar = itemView.findViewById(R.id.nilaiBar);


        }
    }

}