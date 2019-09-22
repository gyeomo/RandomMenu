package momos.org.randommenu;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

public class DialogSingleton {
    Context context;
    public  AlertDialog.Builder ad;
    public  EditText et;
    public DialogSingleton(Context context){
        this.context = context;
        if(ad == null) {
            ad = new AlertDialog.Builder(context);
            ad.setTitle("추가하기");
            ad.setMessage("");
        }
        et = new EditText(context);
        ad.setView(et);
    }
    public android.support.v7.app.AlertDialog.Builder getAD(){
        return ad;
    }
    public EditText getET(){
        return et;
    }


}
