package elarham.tahfizh.ictaq;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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

        holder.arabicTxt.setText(Ayat.getArabic() + "   |" + Ayat.getNomorAyat());
        if(Ayat.getIndo() != null){
            holder.indoTxt.setText(Ayat.getNomorAyat() + ". " + Ayat.getIndo());
        }else{
           holder.indoTxt.setVisibility(View.GONE);
        }


        Typeface face= Typeface.createFromAsset(context.getAssets(), "fonts/scheherazade-webfont.ttf");
        holder.arabicTxt.setTypeface(face);


        holder.lafazTxt.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView arabicTxt, englishTxt, indoTxt, lafazTxt;

        public ViewHolder(View itemView) {
            super(itemView);

            arabicTxt = itemView.findViewById(R.id.arabicTxt);
            indoTxt = itemView.findViewById(R.id.indoTxt);
            lafazTxt = itemView.findViewById(R.id.lafazTxt);

        }
    }



}
