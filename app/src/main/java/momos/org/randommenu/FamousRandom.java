package momos.org.randommenu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.widget.Toast.makeText;


public class FamousRandom extends AppCompatActivity {
    Spinner meterSelector;
    EditText AreaName;
    Button famousButton;
    Button GPSButton;
    Button fRandom;
    ListView listView;
    String AreaString;
    LocationManager lm;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    double longitude;
    double latitude;
    String[] itemRegion;
    int meter = 500;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<HashMap<String,String>> hashArray = new ArrayList<HashMap<String,String>>();
    int hashArray_size = 0;
    ListViewAdapter adapter;
    SimpleAdapter simpleAdapter;
    Random random;
    boolean gpsFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous);
        adapter = new ListViewAdapter();

        random = new Random();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        final String[] from = new String[] { "title", "category" };
        final int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try{
                            if(isGPSEnabled) {
                                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                        100, // 통지사이의 최소 시간간격 (miliSecond)
                                        1, // 통지사이의 최소 변경거리 (m)
                                        mLocationListener);
                            }
                            if(isNetworkEnabled) {
                                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                        100, // 통지사이의 최소 시간간격 (miliSecond)
                                        1, // 통지사이의 최소 변경거리 (m)
                                        mLocationListener);
                            }
                            else{
                                lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                            }
                        }catch(SecurityException ex){
                        }
                    }
                });
            }
        }).start();

        AreaName = (EditText)findViewById(R.id.AreaName);
        famousButton = (Button)findViewById(R.id.famousButton);
        meterSelector = (Spinner)findViewById(R.id.meterSelector);
        listView = (ListView)findViewById(R.id.listView);
        itemRegion = new String[]{"500m 내", "1km 내"};
        ArrayAdapter<String> sRAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                itemRegion);
        sRAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meterSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(itemRegion[position].equals("500m 내"))
                    meter = 500;
                else if(itemRegion[position].equals("1km 내"))
                    meter = 1000;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        meterSelector.setAdapter(sRAdapter);

        famousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AreaString = AreaName.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try{
                            NaverAPIFunc naverAPIFunc = new NaverAPIFunc(getBaseContext(),false);
                            //text =  naverAPIFunc.ParserFunc(AreaString);
                            //arrayList = naverAPIFunc.ParserFunc(AreaString);
                            hashArray = naverAPIFunc.ParserFunc(AreaString);
                            hashArray_size = hashArray.size();
                        }catch(SecurityException ex){
                            makeText(getBaseContext(), "오류"+ex, Toast.LENGTH_SHORT).show();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               /* adapter.clearItem();
                                for(int i = 0; i<arrayList.size();i++){
                                    adapter.addItem(arrayList.get(i),"","");
                                }*/
                               // famousText.setText(text);
                                simpleAdapter = new SimpleAdapter(getBaseContext(), hashArray, android.R.layout.simple_list_item_2, from, to);
                                listView.setAdapter(simpleAdapter);
                            }
                        });
                    }
                }).start();
            }
        });
        GPSButton = (Button)findViewById(R.id.GPSButton);
        GPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    makeText(getBaseContext(), "GPS를 활성화 시켜주세요.", Toast.LENGTH_SHORT).show();
                    createGpsDisabledAlert();
                }
                else {
                    //latitude , longitude 들어가야함. 서울이나 도시에서 넣을것.
                    if(gpsFlag) {
                        String subString[] = findGedAddress(getBaseContext(), latitude, longitude).split(" ");
                        AreaString = subString[1] + " " + subString[2] + " " + subString[3];
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    NaverAPIFunc naverAPIFunc = new NaverAPIFunc(getBaseContext(), true);
                                    naverAPIFunc.SetPosition(latitude, longitude);
                                    naverAPIFunc.SetRegion(meter);
                                    // text = naverAPIFunc.ParserFunc(AreaString);
                                    //arrayList = naverAPIFunc.ParserFunc(AreaString);
                                    hashArray = naverAPIFunc.ParserFunc(AreaString);
                                } catch (SecurityException ex) {
                                    makeText(getBaseContext(), "오류" + ex, Toast.LENGTH_SHORT).show();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                   /* adapter.clearItem();
                                    for(int i = 0; i<arrayList.size();i++){
                                        adapter.addItem(arrayList.get(i),"","");
                                    }*/
                                        //   famousText.setText(text);
                                        //makeText(getBaseContext(),longitude + "   "+latitude, Toast.LENGTH_SHORT).show();
                                        simpleAdapter = new SimpleAdapter(getBaseContext(), hashArray, android.R.layout.simple_list_item_2, from, to);
                                        listView.setAdapter(simpleAdapter);
                                    }
                                });
                            }
                        }).start();
                    }
                    else{
                        makeText(getBaseContext(), "GPS가 위치를 잡고 있습니다. 다시 눌러주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> tempMap=hashArray.get(position);
                makeText(getBaseContext(), tempMap.get("title"), Toast.LENGTH_LONG).show();
            }
        });
        fRandom = (Button)findViewById(R.id.fRandom);
        fRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hashArray_size > 0) {
                    int ranNum = random.nextInt(hashArray_size);
                    HashMap<String, String> tempMap=hashArray.get(ranNum);
                    makeText(getBaseContext(), tempMap.get("title"), Toast.LENGTH_LONG).show();
                }
                else
                    makeText(getBaseContext(), "데이터 없음", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private String findGedAddress(Context context,double latitude, double longitude ){
        Geocoder mGeoCoder = new Geocoder(context);
        Address a;
        String address;
        try{
            List<Address> mResultList = mGeoCoder.getFromLocation(latitude,longitude,1);
            a = mResultList.get(0);
            address = mResultList.get(0).getAddressLine(0);//a.getLocality()+ " " +a.getThoroughfare()//
        }catch (IOException e){
            address = e+"";
        }
        return address;
    }
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            gpsFlag = true;
        }
        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    // GPS Disabled Alert
    private void createGpsDisabledAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS가 꺼져있습니다. \nGPS를 켜시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("GPS 키기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showGpsOptions();
                            }
                        })
                .setNegativeButton("그대로 두기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // show GPS Options
    private void showGpsOptions() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }
    @Override
    public void onDestroy() { // 종료시 실행
        super.onDestroy();
        if(isGPSEnabled)
            lm.removeUpdates(mLocationListener);
        finish();
    }

}
