package elarham.tahfizh.ictaq.QuranFragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import elarham.tahfizh.ictaq.Models.*;
import elarham.tahfizh.ictaq.Quran;
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

        String type = surah.getType();

        if(type.equals("mekah")){
            holder.typeTxt.setText(context.getString(R.string.makkiyah)+ ",");
        }else if(type.equals("madinah")){
            holder.typeTxt.setText(context.getString(R.string.madaniyah) + ",");
        }
        holder.surahTxt.setText(surah.getNama());
        holder.nomorTxt.setText(surah.getNomor());
        holder.asmaTxt.setText(surah.getAsma());
        holder.artiTxt.setText(surah.getArti());
        holder.ayatTxt.setText(surah.getAyat()+ " " + context.getString(R.string.ayat));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView surahTxt, nomorTxt, asmaTxt, artiTxt, ayatTxt, typeTxt;

        public ViewHolder(View itemView) {
            super(itemView);

            surahTxt = itemView.findViewById(R.id.surahTxt);
            nomorTxt = itemView.findViewById(R.id.nomorTxt);
            asmaTxt = itemView.findViewById(R.id.asmaTxt);
            artiTxt = itemView.findViewById(R.id.artiTxt);
            ayatTxt = itemView.findViewById(R.id.ayatTxt);
            typeTxt = itemView.findViewById(R.id.typeTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    Surah surah = list.get(position);

                    Intent quran = new Intent(context, Quran.class);
                    quran.putExtra("nama", surah.getNama());
                    quran.putExtra("nomor", surah.getNomor());
                    quran.putExtra("asma", surah.getAsma());
                    quran.putExtra("arti", surah.getArti());
                    quran.putExtra("ayat", surah.getAyat());
                    quran.putExtra("type", surah.getType());
                    quran.putExtra("keterangan", surah.getKeterangan());
                    quran.putExtra("mode", "surah");
                    context.startActivity(quran);
                }
            });
        }
    }

}
