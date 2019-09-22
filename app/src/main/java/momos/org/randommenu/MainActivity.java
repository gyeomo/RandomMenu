package momos.org.randommenu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    Button surroundingMenuButton;
    Intent intent;
    String dbName = "ListDB.db";
    String sql;
    int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    boolean dbExist;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("랜덤메뉴");
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        db=dbHelper.getWritableDatabase();
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(),PrimarySelect.class);
                intent.putExtra("startNum", 0);
                startActivity(intent);
                //finish();
            }
        });

        surroundingMenuButton = (Button)findViewById(R.id.surroundingMenuButton);
        surroundingMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(),FamousRandom.class);
                startActivity(intent);
                //finish();
            }
        });
        Cursor cursor;
        sql = "SELECT * FROM MenuList;";
        cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();

        if(cursor.getCount()>0){
            dbExist =  true;
           // Toast.makeText(this, "Exist", Toast.LENGTH_SHORT).show();
        }else{
            dbExist = false;
          //  Toast.makeText(this, "Not Exist", Toast.LENGTH_SHORT).show();
        }
        if(!dbExist) {
            dbHelper.onReCreate(db);
            db.beginTransaction();
            try {
                sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                        , 1, 0, 0, 1, "한식");
                db.execSQL(sql);
                {
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 1, 1, 0, 1, "한정식");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 1, 1, 1, "쌈밥정식");
                        db.execSQL(sql);

                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 1, 2, 1, "고등어정식");
                        db.execSQL(sql);

                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 1, 3, 1, "갈비정식");
                        db.execSQL(sql);
                    }
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 1, 2, 0, 1, "탕,찌개");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 2, 1, 1, "김치찌개");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 2, 2, 1, "된장찌개");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 2, 3, 1, "감자탕");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 2, 4, 1, "해물탕");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 1, 3, 0, 1, "해장국");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 3, 1, 1, "순대국밥");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 3, 2, 1, "뼈해장국");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 3, 3, 1, "선지해장국");
                        db.execSQL(sql);
                    }
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 1, 4, 0, 1, "면");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 4, 1, 1, "물냉면");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 4, 2, 1, "비빔냉면");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 4, 3, 1, "잔치국수");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 1, 4, 4, 1, "막국수");
                        db.execSQL(sql);
                    }
                }
                sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                        , 2, 0, 0, 1, "중식");
                db.execSQL(sql);
                {
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 2, 1, 0, 1, "짜장면");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 2, 1, 1, 1, "짜장면");
                        db.execSQL(sql);
                    }
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 2, 2, 0, 1, "짬뽕");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 2, 2, 1, 1, "짬뽕");
                        db.execSQL(sql);
                    }
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 2, 3, 0, 1, "볶음밥");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 2, 3, 1, 1, "볶음밥");
                        db.execSQL(sql);
                    }
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 2, 4, 0, 1, "탕수육");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 2, 4, 1, 1, "탕수육");
                        db.execSQL(sql);
                    }
                }


                sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                        , 3, 0, 0, 1, "일식");
                db.execSQL(sql);
                {
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 3, 1, 0, 1, "사시미");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 3, 1, 1, 1, "광어회");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 3, 1, 2, 1, "우럭회");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 3, 2, 0, 1, "면");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 3, 2, 1, 1, "라멘");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 3, 2, 2, 1, "소바");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 3, 3, 0, 1, "튀김 부침");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 3, 3, 1, 1, "돈까스");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 3, 3, 2, 1, "새우튀김");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 3, 3, 3, 1, "오코노미야끼");
                        db.execSQL(sql);
                    }
                }

                sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                        , 4, 0, 0, 1, "양식");
                db.execSQL(sql);
                {
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 4, 1, 0, 1, "파스타");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 1, 1, 1, "토마토 파스타");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 1, 2, 1, "크림소스 파스타");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 1, 3, 1, "알리오 올리오 파스타");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 4, 2, 0, 1, "스테이크");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 2, 1, 1, "뉴욕스테이크");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 2, 2, 1, "서로인스테이크");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 2, 3, 1, "티본스테이크");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 4, 3, 0, 1, "샐러드");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 3, 1, 1, "감자 샐러드");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 3, 2, 1, "그린 샐러드");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 3, 3, 1, "시저 샐러드");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 3, 4, 1, "치킨 샐러드");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 4, 3, 5, 1, "연어 샐러드");
                        db.execSQL(sql);
                    }

                }


                sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                        , 5, 0, 0, 1, "분식");
                db.execSQL(sql);
                {
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 5, 1, 0, 1, "떡볶이");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 1, 1, 1, "떡볶이");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 1, 2, 1, "라볶이");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 5, 2, 0, 1, "김밥");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 2, 1, 1, "김밥");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 2, 2, 1, "치즈김밥");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 2, 3, 1, "참치김밥");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 5, 3, 0, 1, "라면");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 3, 1, 1, "라면");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 3, 2, 1, "김치라면");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 3, 3, 1, "치즈라면");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 3, 4, 1, "만두라면");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 5, 4, 0, 1, "덮밥");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 4, 1, 1, "쇠고기덮밥");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 4, 2, 1, "오징어덮밥");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 4, 3, 1, "제육덮밥");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 5, 4, 4, 1, "김치덮밥");
                        db.execSQL(sql);
                    }
                }

                sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                        , 6, 0, 0, 1, "패스트푸드");
                db.execSQL(sql);
                {
                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 6, 1, 0, 1, "치킨");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 1, 1, 1, "후라이드치킨");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 1, 2, 1, "양념치킨");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 1, 3, 1, "반반치킨");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 1, 4, 1, "간장치킨");
                        db.execSQL(sql);
                    }


                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 6, 2, 0, 1, "피자");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 2, 1, 1, "불고기피자");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 2, 2, 1, "페퍼로니피자");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 2, 3, 1, "리치골드피자");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 2, 4, 1, "고구마피자");
                        db.execSQL(sql);
                    }

                    sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                            , 6, 3, 0, 1, "햄버거");
                    db.execSQL(sql);
                    {
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 3, 1, 1, "치즈버거");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 3, 2, 1, "치킨버거");
                        db.execSQL(sql);
                        sql = String.format("INSERT INTO MenuList VALUES(NULL,'%s','%s','%s','%s','%s');"
                                , 6, 3, 3, 1, "새우버거");
                        db.execSQL(sql);
                    }

                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
            } finally {
                db.endTransaction();
                db.close();
                dbHelper = null;
            }
        }
    }
}
