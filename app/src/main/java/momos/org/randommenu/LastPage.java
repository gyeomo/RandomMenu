package momos.org.randommenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LastPage extends AppCompatActivity {
    String dbName = "ListDB.db";
    String sql;
    int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Button homeButton;
    Button mapButton;
    Button deliveryButton;
    int primaryNum, secondaryNum, tertiaryNum;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        db=dbHelper.getWritableDatabase();
        primaryNum = getIntent().getExtras().getInt("primaryNum");
        secondaryNum = getIntent().getExtras().getInt("secondaryNum");
        tertiaryNum = getIntent().getExtras().getInt("tertiaryNum");
        context = this;
        sql = "SELECT * FROM MenuList WHERE Category_1='"+primaryNum+"' AND Category_2='"+secondaryNum+"' " +
                "AND Category_3='"+tertiaryNum+"';";
        cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                if(cursor.getInt(1) ==primaryNum &&  cursor.getInt(2) ==secondaryNum && cursor.getInt(3) ==tertiaryNum)
                    break;
            }
        }
        TextView textView;
        textView = (TextView)findViewById(R.id.textView);
        textView.setText(cursor.getString(5));
        setTitle(cursor.getString(5));
        homeButton = (Button)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(),PrimarySelect.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("startNum", 0);
                startActivity(intent);
                finish();
            }
        });
        mapButton = (Button)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0,0?q="+cursor.getString(5));                  // 좌표값 설정.
                Intent intent = new Intent( android.content.Intent.ACTION_VIEW, uri); // 인텐트 생성.
                Intent chooser = Intent.createChooser(intent, "지도");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });
        deliveryButton = (Button)findViewById(R.id.deliveryButton);
        deliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] item = {"배달의민족","요기요","배달통"};
                AlertDialog.Builder ad  = new AlertDialog.Builder(context,R.style.DialogStyle);
                ad.setTitle("배달");
                ad.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0 :
                                if(getPackageList("com.sampleapp")) {
                                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.sampleapp");
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(context, "해당 어플이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case 1 :
                                if(getPackageList("com.fineapp.yogiyo")) {
                                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.fineapp.yogiyo");
                                   // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(context, "해당 어플이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case 2 :
                                if(getPackageList("stonykids.baedaltong.season2")) {
                                    Intent intent = getPackageManager().getLaunchIntentForPackage("stonykids.baedaltong.season2");
                                   // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(context, "해당 어플이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

    }
    public boolean getPackageList(String packageNAme) {
        boolean isExist = false;

        PackageManager pkgMgr = getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith(packageNAme)){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }
}
