package elarham.tahfizh.ictaq.QuranFragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import elarham.tahfizh.ictaq.Models.*;
import elarham.tahfizh.ictaq.R;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

    private Context context;
    private List<Surah> list;

    public SurahAdapter(Context context, List<Surah> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_surah, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Surah surah = list.get(position);

        holder.textTitle.setText(surah.getNama());
        holder.textRating.setText(String.valueOf(surah.getType()));
        holder.textYear.setText(String.valueOf(surah.getNomor()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating, textYear;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.main_title);
            textRating = itemView.findViewById(R.id.main_rating);
            textYear = itemView.findViewById(R.id.main_year);
        }
    }

}
