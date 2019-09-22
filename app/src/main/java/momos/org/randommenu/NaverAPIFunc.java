package momos.org.randommenu;

import android.content.Context;
import android.location.Location;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class NaverAPIFunc {
    String clientId = "0Zh3RLBDTBr1WCloQP6j";// 애플리케이션 클라이언트 아이디값";
    String clientSecret = "N2Ob5mOdN5";// 애플리케이션 클라이언트 시크릿값";\
    int display = 30;
    Context context;
    String titleName;
    String category;
    int mapX;
    int mapY;
    boolean searchFlag;
    public static StringBuffer  sb;//
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<HashMap<String,String>> hashArray = new ArrayList<HashMap<String,String>>();
    HashMap<String,String> hashData;
    double latitude, longitude;
    int meter;

    public NaverAPIFunc(Context context, boolean bool){
        this.context = context;
        sb = new StringBuffer();
        searchFlag = bool;
    }
    public void SetPosition(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public void SetRegion(int meter){
        this.meter = meter;
    }

    public ArrayList ParserFunc(String areaName){
        try {
            String text = URLEncoder.encode(areaName+" 맛집", "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local.xml?query=" + text + "&display=" + display+"&start=1&sort=comment";//random
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            con.setRequestProperty("Content-Type", "application/xml");
            con.connect();
          /*  int responseCode = con.getResponseCode();
            if(responseCode==200) { // 정상 호출
                arrayList.add("정상" );
            } else {  // 에러 발생
                arrayList.add("에러" );
            }
            */
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String tag;
            xpp.next();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기
                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("title")) {
                            xpp.next();
                            titleName = (xpp.getText().replace("<b>", "").replace("</b>", "")+ "  ");
                        } else if (tag.equals("category")) {
                            xpp.next();
                            category = (xpp.getText().replace("<b>", "").replace("</b>", ""));
                            String[] split = category.split(">");
                            category = split[1];
                        }

                        /*else if (tag.equals("address")) {
                            xpp.next();
                           sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                        } else if (tag.equals("telephone")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                        }*/ else if (tag.equals("mapx")) { // getmapx value
                            xpp.next();
                            mapX = Integer.parseInt(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            //sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                        } else if (tag.equals("mapy")) { // getmapy valye
                            xpp.next();
                            mapY = Integer.parseInt(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            //sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            if(searchFlag) {
                                GeoTransPoint oKA = new GeoTransPoint(mapX, mapY);
                                GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
                                float lat = (float) (oGeo.getY() * 1E6 / 1000000);
                                //lat = Double.parseDouble(String.format("%.6f",lat));
                                float lng = (float) (oGeo.getX() * 1E6 / 1000000);
                                //lng = Double.parseDouble(String.format("%.6f",lng));
                                if (DistanceByDegreeAndroid(lat, lng, latitude, longitude) <= meter) {
                                    sb.append(titleName + "   " + lat + "  " + lng + "\n");
                                    hashData = new HashMap<String, String>();
                                    hashData.put("title", titleName);
                                    hashData.put("category", category);
                                    hashArray.add(hashData);
                                    arrayList.add(titleName + category);
                                }
                            }
                            else {
                                hashData = new HashMap<String, String>();
                                hashData.put("title", titleName);
                                hashData.put("category", category);
                                hashArray.add(hashData);
                                arrayList.add(titleName + category);
                            }

                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기
                        if (tag.equals("item"))break;

                }
                eventType = xpp.next();
            }
            //return sb.toString();

            return hashArray;
        }catch (Exception e) {
            arrayList.add(e+"");
            return arrayList;
        }
    }
    public double DistanceByDegreeAndroid(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
        Location startPos = new Location("PointA");
        Location endPos = new Location("PointB");

        startPos.setLatitude(_latitude1);
        startPos.setLongitude(_longitude1);
        endPos.setLatitude(_latitude2);
        endPos.setLongitude(_longitude2);

        double distance = startPos.distanceTo(endPos);

        return distance;
    }

}
