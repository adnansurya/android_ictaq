package elarham.tahfizh.ictaq.Global;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    public String SP_APP = "spMyTahfizhApp";

    public String SP_USERDATA = "spUserData";
    public String SP_FRAGMENT = "HomeFragment";

    public String SP_KODE = "spKode";
    public String SP_TYPE = "spType";
    public String SP_USERNAME = "spUsername";
    public String SP_PASSWORD = "spPassword";
    public String SP_NAMA = "spNama";
    public String SP_ID_USER = "spIdUser";


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

    public String getSpIdUser(){ return sp.getString(SP_ID_USER, "");}

    public String getSpUsername(){
        return sp.getString(SP_USERNAME, "");
    }

    public String getSpPassword(){
        return sp.getString(SP_PASSWORD, "");
    }

    public String getSpFragment() { return sp.getString(SP_FRAGMENT, "");}

    public String getSpKode() {
        return sp.getString(SP_KODE, "");
    }

    public String getSpType() {return  sp.getString(SP_TYPE, "");}

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
