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


public class PrimarySelect extends AppCompatActivity {
    LinearLayout primaryLayout;
    Button primaryRandom;
    String dbName = "ListDB.db";
    String sql;
    Cursor cursor;
    int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<String> primaryName;
    ArrayList<String> primaryNameDump;
    ArrayList<Integer> primaryFlag;
    private int countNum = 1;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        db=dbHelper.getWritableDatabase();
        setTitle("대분류");
        //String[] stringDump =  {"한식","중식","일식","양식","분식","패스트푸드"};
        primaryName =  new ArrayList<String>();
        primaryNameDump =  new ArrayList<String>();
        primaryFlag = new ArrayList<Integer>();
        sql = "SELECT * FROM MenuList WHERE Category_2='0' AND Category_3='0';";
        cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                if(cursor.getInt(4) == 1)
                    primaryName.add(cursor.getString(5));
                primaryNameDump.add(cursor.getString(5));
                primaryFlag.add(cursor.getInt(4));
            }
        }
        countNum = primaryNameDump.size()+1;

        cursor.close();
        db.close();
        //dbHelper=null;
        primaryLayout = (LinearLayout)findViewById(R.id.primaryLayout);
        primaryRandom = (Button) findViewById(R.id.primaryRandom);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        SelectFunc selectFunc = new SelectFunc(primaryLayout,primaryRandom,  getBaseContext()
                ,primaryName, 0,0,0,0,width);
        selectFunc.SetSelector();
        selectFunc = null;
        cursor.close();
        db.close();
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
                //dialog 띄워서 글 쓰고 위에 변수값 이용해서 데이터베이스에 추가하기

                DialogSingleton singleton = new DialogSingleton(this);
                AlertDialog.Builder ad = singleton.getAD();
                final EditText et = singleton.getET();
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  Toast.makeText(getBaseContext(), et.getText().toString() + "  "+countNum, Toast.LENGTH_SHORT).show();
                        db=dbHelper.getWritableDatabase();
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , countNum, 0, 0, 1, et.getText().toString());
                      //  Toast.makeText(getBaseContext(), countNum+"", Toast.LENGTH_SHORT).show();
                        db.execSQL(sql);
                        db.close();
                        primaryName.add(et.getText().toString());
                        primaryNameDump.add(et.getText().toString());
                        countNum = primaryNameDump.size()+1;
                        primaryFlag.add(1);
                        SelectFunc selectFunc = new SelectFunc(primaryLayout,primaryRandom,  getBaseContext()
                                ,primaryName, 0,0,0,0,width);
                        selectFunc.SetSelector();
                        selectFunc=null;
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
                final String[] items = primaryNameDump.toArray(new String[primaryNameDump.size()]);
                final boolean[] booleen = new boolean[primaryFlag.size()];
                for(int i=0; i < primaryFlag.size(); i++){
                    if(primaryFlag.get(i) == 1)
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
                                                    "'WHERE MenuName='" +  primaryNameDump.get(which)+ "';";
                                            db.execSQL(sql);
                                        } else {
                                            sql = "UPDATE MenuList SET SelectFlag='" +  0+
                                                    "'WHERE MenuName='" +  primaryNameDump.get(which)+ "';";
                                            db.execSQL(sql);
                                        }
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                primaryName.clear();
                                primaryNameDump.clear();
                                primaryFlag.clear();
                                countNum=1;
                                sql = "SELECT * FROM MenuList WHERE Category_2='0' AND Category_3='0';";
                                cursor = db.rawQuery(sql,null);
                                if(cursor.getCount()>0) {
                                    while (cursor.moveToNext()) {
                                        if(cursor.getInt(4) == 1)
                                            primaryName.add(cursor.getString(5));
                                        countNum++;
                                        primaryNameDump.add(cursor.getString(5));
                                        primaryFlag.add(cursor.getInt(4));
                                    }
                                }
                                SelectFunc selectFunc = new SelectFunc(primaryLayout,primaryRandom,  getBaseContext()
                                        ,primaryName, 0,0,0,0,width);
                                selectFunc.SetSelector();
                                primaryName.clear();
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
