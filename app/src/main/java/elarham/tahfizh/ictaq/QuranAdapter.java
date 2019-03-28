package elarham.tahfizh.ictaq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Ayat Ayat = list.get(position);

        holder.arabicTxt.setText(Ayat.getArabic() + "   |" + Ayat.getNomorAyat());
        if(Ayat.getIndo() != null && Ayat.getEnglish() != null){
            String translationLang = prefs.getString(context.getString(R.string.translationlangkey),context.getResources().getStringArray(R.array.listLanguageValues)[0]);
            if(translationLang.equals("ina")){
                holder.translateTxt.setText(Ayat.getNomorAyat() + ". " + Ayat.getIndo());
            }else if(translationLang.equals("en")){
                holder.translateTxt.setText(Ayat.getNomorAyat() + ". " + Ayat.getEnglish());
            }else{
                holder.translateTxt.setVisibility(View.GONE);
            }

        }else{
           holder.translateTxt.setVisibility(View.GONE);
        }

        String arabicFont = prefs.getString(context.getString(R.string.arabicfontkey),context.getResources().getStringArray(R.array.listFontValues)[0]);
        Typeface face= Typeface.createFromAsset(context.getAssets(), arabicFont);
        holder.arabicTxt.setTypeface(face);

        String arabicFontSize = prefs.getString(context.getString(R.string.arabicfontsizekey),context.getResources().getStringArray(R.array.listFontSizeValues)[0]);
        holder.arabicTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(arabicFontSize));

        if(prefs.getBoolean(context.getString(R.string.translationkey),true)){
            holder.translateTxt.setVisibility(View.VISIBLE);
        }else{
            holder.translateTxt.setVisibility(View.GONE);
        }

        holder.lafazTxt.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView arabicTxt, translateTxt, lafazTxt;

        public ViewHolder(View itemView) {
            super(itemView);

            arabicTxt = itemView.findViewById(R.id.arabicTxt);
            translateTxt = itemView.findViewById(R.id.translateTxt);
            lafazTxt = itemView.findViewById(R.id.lafazTxt);

        }
    }



}
