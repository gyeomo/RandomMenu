package momos.org.randommenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

public class SelectFunc{
    LinearLayout layout;
    Button button;
    int startNum;
    int rotationNum=0;
    int repeatFlag=0;
    LinearLayout horizonLinear;
    Random random;
    int ranNum;
    Context context;
    ArrayList<String> primaryName;
    int selectFlag;
    int primaryNum;
    int secondaryNum;
    int TertiaryNum;
    int screenWidth;
    public SelectFunc(LinearLayout layout, Button button, Context context,
                      ArrayList<String> primaryName, int flag,int primaryNum,int secondaryNum,int TertiaryNum, int screenWidth){
        this.layout = layout;
        this.button = button;
        random = new Random();
        this.startNum = primaryName.size();
        this.context = context;
        this.primaryName = primaryName;
        selectFlag = flag;
        this.primaryNum = primaryNum;
        this.secondaryNum = secondaryNum;
        this.TertiaryNum = TertiaryNum;
        this.screenWidth = screenWidth;
    }


        public void SetSelector(){
            layout.removeAllViews();
            LinearLayout.LayoutParams paramsB1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsB1.weight = 1;
            paramsB1.setMargins(10,5,10,5);
            paramsB1.height = 300;
            LinearLayout.LayoutParams paramsB2 = new LinearLayout.LayoutParams(
                    screenWidth/2, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsB2.setMargins(10,5,10,5);
            paramsB2.height = 300;
            LinearLayout.LayoutParams paramsL = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(startNum%2 == 1)
                repeatFlag = 1;
            for(int i =0; i<startNum/2+repeatFlag;i++){
                horizonLinear = new LinearLayout(context);
                horizonLinear.setOrientation(LinearLayout.HORIZONTAL);
                horizonLinear.setLayoutParams(paramsL);
                for(int j = 0; j<2;j++) {
                    if(repeatFlag ==1){
                        if(i==startNum/2 && j==1)
                            break;
                    }
                    Button selectButton = new Button(context);
                    selectButton.setText(primaryName.get(rotationNum));
                    selectButton.setLayoutParams(paramsB1);
                    selectButton.setTextColor(Color.WHITE);
                    selectButton.setBackground(context.getDrawable(R.drawable.button_backgound));
                    if(repeatFlag ==1){
                        if(i==startNum/2 && j==0)
                            selectButton.setLayoutParams(paramsB2);
                    }
                    selectButton.setTag(rotationNum);
                    selectButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //Toast.makeText(context, "클릭한 버튼:" + primaryName.get((Integer) v.getTag()), Toast.LENGTH_SHORT).show();
                            if(selectFlag == 0) {
                                Intent intent;
                                intent = new Intent(context, SecondarySelect.class);
                                intent.putExtra("primaryNum", (Integer) v.getTag());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(intent);
                            }
                            else if(selectFlag == 1){
                                Intent intent;
                                intent = new Intent(context, TertiarySelect.class);
                                intent.putExtra("primaryNum", primaryNum);
                                intent.putExtra("secondaryNum", (Integer) v.getTag());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(intent);
                            }
                            else{
                                //Toast.makeText(context, "클릭한 버튼:" + primaryName.get((Integer) v.getTag()), Toast.LENGTH_SHORT).show();
                                Intent intent;
                                intent = new Intent(context, LastPage.class);
                                intent.putExtra("primaryNum", primaryNum);
                                intent.putExtra("secondaryNum", secondaryNum);
                                intent.putExtra("tertiaryNum",(Integer) v.getTag()+1);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(intent);
                            }
                        }
                    });
                    rotationNum++;
                    horizonLinear.addView(selectButton);
                }
                layout.addView(horizonLinear);

            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ranNum = random.nextInt(startNum);
                    if(selectFlag == 0) {
                        Intent intent;
                        intent = new Intent(context, SecondarySelect.class);
                        intent.putExtra("primaryNum", ranNum);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                    }
                    else if(selectFlag == 1){
                        Intent intent;
                        intent = new Intent(context, TertiarySelect.class);
                        intent.putExtra("primaryNum", primaryNum);
                        intent.putExtra("secondaryNum", ranNum);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                    }
                    else{
                        //Toast.makeText(context, "클릭한 버튼:" + primaryName.get(ranNum) , Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent(context, LastPage.class);
                        intent.putExtra("primaryNum", primaryNum);
                        intent.putExtra("secondaryNum", secondaryNum);
                        intent.putExtra("tertiaryNum",ranNum+1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                    }
                }
            });
        }
}
