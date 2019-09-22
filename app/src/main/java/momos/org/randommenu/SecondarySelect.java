package momos.org.randommenu;

import android.content.DialogInterface;
import android.content.Intent;
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


public class SecondarySelect extends AppCompatActivity {
    LinearLayout secondaryLayout;
    Button secondaryRandom;
    int primaryNum;
    String dbName = "ListDB.db";
    String sql;
    Cursor cursor;
    int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    private int countNum = 1;
    ArrayList<String> secondaryName;
    ArrayList<String> secondaryNameDump;
    ArrayList<Integer> secondaryFlag;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        db=dbHelper.getWritableDatabase();
        secondaryName = new ArrayList<String>();
        secondaryNameDump = new ArrayList<String>();
        secondaryFlag = new ArrayList<Integer>();
        secondaryLayout = (LinearLayout)findViewById(R.id.secondaryLayout);
        secondaryRandom = (Button) findViewById(R.id.secondaryRandom);
        primaryNum = getIntent().getExtras().getInt("primaryNum")+1;

        sql = "SELECT * FROM MenuList WHERE Category_1='"+primaryNum+"' AND Category_3='0';";
        cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                if(cursor.getInt(2) != 0) {
                    if (cursor.getInt(4) == 1)
                        secondaryName.add(cursor.getString(5));
                    secondaryNameDump.add(cursor.getString(5));
                    secondaryFlag.add(cursor.getInt(4));
                }
                else {
                    if(cursor.getInt(1) == primaryNum)
                        setTitle(cursor.getString(5));
                }
            }
        }
        countNum = secondaryNameDump.size()+1;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        SelectFunc selectFunc = new SelectFunc(secondaryLayout,secondaryRandom, getBaseContext()
                ,secondaryName,1,primaryNum,0,0,width);
        selectFunc.SetSelector();
        selectFunc = null;
        db.close();
        cursor.close();
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int curId = item.getItemId();
        Intent intent;
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
                                , primaryNum, countNum, 0, 1, et.getText().toString());
                       // Toast.makeText(getBaseContext(), countNum+"", Toast.LENGTH_SHORT).show();
                        db.execSQL(sql);
                        db.close();
                        secondaryName.add(et.getText().toString());
                        secondaryNameDump.add(et.getText().toString());
                        countNum = secondaryNameDump.size()+1;
                        secondaryFlag.add(1);
                        SelectFunc selectFunc = new SelectFunc(secondaryLayout,secondaryRandom,  getBaseContext()
                                ,secondaryName,1,primaryNum,0,0,width);
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
                final String[] items = secondaryNameDump.toArray(new String[secondaryNameDump.size()]);
                final boolean[] booleen = new boolean[secondaryFlag.size()];
                for(int i=0; i < secondaryFlag.size(); i++){
                    if(secondaryFlag.get(i) == 1)
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
                                                    "'WHERE MenuName='" +  secondaryNameDump.get(which)+ "';";
                                            db.execSQL(sql);
                                        } else {
                                            sql = "UPDATE MenuList SET SelectFlag='" +  0+
                                                    "'WHERE MenuName='" +  secondaryNameDump.get(which)+ "';";
                                            db.execSQL(sql);
                                        }
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                secondaryName.clear();
                                secondaryNameDump.clear();
                                secondaryFlag.clear();
                                countNum=1;
                                sql = "SELECT * FROM MenuList WHERE Category_1='"+primaryNum+"' AND Category_3='0';";
                                cursor = db.rawQuery(sql,null);
                                if(cursor.getCount()>0) {
                                    while (cursor.moveToNext()) {
                                        if(cursor.getInt(2) != 0) {
                                            if (cursor.getInt(4) == 1)
                                                secondaryName.add(cursor.getString(5));
                                            if (!secondaryName.isEmpty())
                                                countNum++;
                                            secondaryNameDump.add(cursor.getString(5));
                                            secondaryFlag.add(cursor.getInt(4));
                                        }
                                    }
                                }
                                SelectFunc selectFunc = new SelectFunc(secondaryLayout,secondaryRandom, getBaseContext()
                                        ,secondaryName,1,primaryNum,0,0,width);
                                selectFunc.SetSelector();
                                secondaryName.clear();
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
