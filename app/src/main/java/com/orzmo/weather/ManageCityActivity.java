package com.orzmo.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ManageCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);

        this.initView();
    }

    private void initView() {
        final SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        String list = pref.getString("userWatched","");

        if (!list.equals("")){
            final String[] cityCodes = list.split(",");
            String[] cityNames = new String[cityCodes.length];
            int i = 0;
            for (String item : cityCodes) {
                cityNames[i] = pref.getString(item, "") + " / " + item;
                i++;
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManageCityActivity.this, android.R.layout.simple_list_item_1,cityNames);
            ListView lv = (ListView) findViewById(R.id.list_view3);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int mk, long l) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ManageCityActivity.this);
                    dialog.setTitle("确定删除");
                    dialog.setMessage("您确定要删除，您关注的这个城市吗？该数据不可恢复！🙅‍♂️");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String cityCodesString = "";
                            for (int j=0;j<cityCodes.length;j++){
                                if (j != mk) {
                                    if (cityCodesString.equals("")) {
                                        cityCodesString = cityCodes[j];
                                    } else {
                                        cityCodesString += ","  + cityCodes[j];
                                    }
                                }
                            }
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("userWatched", cityCodesString);
                            editor.commit();
                            reloadCity();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            });
        }


    }

    private void reloadCity() {
        final SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        String list = pref.getString("userWatched","");
        final String[] cityCodes = list.split(",");
        String[] cityNames = new String[cityCodes.length];
        int i = 0;
        for (String item : cityCodes) {
            cityNames[i] = pref.getString(item, "") + " / " + item;
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManageCityActivity.this, android.R.layout.simple_list_item_1,cityNames);
        ListView lv = (ListView) findViewById(R.id.list_view3);
        lv.setAdapter(adapter);
    }
}
