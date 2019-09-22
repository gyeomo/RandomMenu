package momos.org.randommenu;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class TertiarySelect extends AppCompatActivity {
    LinearLayout tertiaryLayout;
    Button tertiaryRandom;
    int primaryNum;
    int secondaryNum;
    String dbName = "ListDB.db";
    String sql;
    Cursor cursor;
    int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<String> tertiaryName;
    ArrayList<String> tertiaryNameDump;
    ArrayList<Integer> tertiaryFlag;
    private int countNum = 1;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tertiary);
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        db=dbHelper.getWritableDatabase();
        tertiaryLayout = (LinearLayout)findViewById(R.id.tertiaryLayout);
        tertiaryRandom = (Button) findViewById(R.id.tertiaryRandom);

        tertiaryName = new ArrayList<String>();
        tertiaryNameDump = new ArrayList<String>();
        tertiaryFlag = new ArrayList<Integer>();

        primaryNum = getIntent().getExtras().getInt("primaryNum");
        secondaryNum = getIntent().getExtras().getInt("secondaryNum")+1;

        sql = "SELECT * FROM MenuList WHERE Category_1='"+primaryNum+"' AND Category_2='"+secondaryNum+"';";
        cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                if(cursor.getInt(3) != 0) {
                    if (cursor.getInt(4) == 1)
                        tertiaryName.add(cursor.getString(5));
                    tertiaryNameDump.add(cursor.getString(5));
                    tertiaryFlag.add(cursor.getInt(4));
                }
                else {
                    if(cursor.getInt(1) == primaryNum && cursor.getInt(2) == secondaryNum)
                        setTitle(cursor.getString(5));
                }
            }
        }
        countNum = tertiaryNameDump.size()+1;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        SelectFunc selectFunc = new SelectFunc(tertiaryLayout,tertiaryRandom, getBaseContext() ,
                tertiaryName,2,primaryNum,secondaryNum,0,width);
        selectFunc.SetSelector();
        selectFunc = null;
        //tertiaryName.clear();
        db.close();
        cursor.close();
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int curId = item.getItemId();
        switch(curId){
            case R.id.menu_add:
                DialogSingleton singleton = new DialogSingleton(this);
                AlertDialog.Builder ad = singleton.getAD();
                final EditText et = singleton.getET();
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db=dbHelper.getWritableDatabase();
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , primaryNum, secondaryNum, countNum, 1, et.getText().toString());
                       // Toast.makeText(getBaseContext(), countNum+"", Toast.LENGTH_SHORT).show();
                        db.execSQL(sql);
                        db.close();
                        tertiaryName.add(et.getText().toString());
                        tertiaryNameDump.add(et.getText().toString());
                        countNum = tertiaryNameDump.size()+1;

                        tertiaryFlag.add(1);
                        SelectFunc selectFunc = new SelectFunc(tertiaryLayout,tertiaryRandom, getBaseContext() ,
                                tertiaryName,2,primaryNum,secondaryNum,0,width);
                        selectFunc.SetSelector();
                        selectFunc = null;
                        dialog.cancel();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                ad.show();
                break;
            case R.id.menu_able:
                db=dbHelper.getWritableDatabase();
                final String[] items = tertiaryNameDump.toArray(new String[tertiaryNameDump.size()]);
                final boolean[] booleen = new boolean[tertiaryFlag.size()];
                for(int i=0; i < tertiaryFlag.size(); i++){
                    if(tertiaryFlag.get(i) == 1)
                        booleen[i] = true;
                    else
                        booleen[i] = false;
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog .setTitle("보여질 항목을 선택하세요")
                        .setMultiChoiceItems(items,
                                booleen,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if(isChecked) {
                                            sql = "UPDATE MenuList SET SelectFlag='" +  1+
                                                    "'WHERE MenuName='" +  tertiaryNameDump.get(which)+ "';";
                                            db.execSQL(sql);
                                        } else {
                                            sql = "UPDATE MenuList SET SelectFlag='" +  0+
                                                    "'WHERE MenuName='" +  tertiaryNameDump.get(which)+ "';";
                                            db.execSQL(sql);
                                        }
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tertiaryName.clear();
                                tertiaryNameDump.clear();
                                tertiaryFlag.clear();
                                countNum=1;
                                sql = "SELECT * FROM MenuList WHERE Category_1='"+primaryNum+"' AND Category_2='"+secondaryNum+"';";
                                cursor = db.rawQuery(sql,null);
                                if(cursor.getCount()>0) {
                                    while (cursor.moveToNext()) {
                                        if(cursor.getInt(3) != 0) {
                                            if (cursor.getInt(4) == 1)
                                                tertiaryName.add(cursor.getString(5));
                                            if (!tertiaryName.isEmpty())
                                                countNum++;
                                            tertiaryNameDump.add(cursor.getString(5));
                                            tertiaryFlag.add(cursor.getInt(4));
                                        }
                                    }
                                }
                                SelectFunc selectFunc = new SelectFunc(tertiaryLayout,tertiaryRandom, getBaseContext() ,
                                        tertiaryName,2,primaryNum,secondaryNum,0,width);
                                selectFunc.SetSelector();
                                cursor.close();
                                db.close();
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
        return super.onOptionsItemSelected(item);


    }
}
