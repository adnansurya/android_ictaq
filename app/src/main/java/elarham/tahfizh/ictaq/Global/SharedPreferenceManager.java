package elarham.tahfizh.ictaq.Global;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    public String SP_APP = "spMyTahfizhApp";

    public String SP_USERDATA = "spUserData";

    public String SP_USERNAME = "spUsername";
    public String SP_PASSWORD = "spPassword";
    public String SP_NAMA = "spNama";


    public String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPreferenceManager(Context context){
        sp = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void setSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void setSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void setSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpUsername(){
        return sp.getString(SP_USERNAME, "");
    }

    public String getSpPassword(){
        return sp.getString(SP_PASSWORD, "");
    }

    public String getSpNama() {
        return sp.getString(SP_NAMA,"");
    }

    public String getSpUserData(){
        return sp.getString(SP_USERDATA, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public void logout(){
        spEditor.clear();
        spEditor.commit();
    }
}