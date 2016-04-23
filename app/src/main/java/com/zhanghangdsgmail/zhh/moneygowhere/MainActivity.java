package com.zhanghangdsgmail.zhh.moneygowhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public final int RECORD_RETURN_OK = 1;

    private List<Map<String, String>> money_cost_list = null;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecorderActivity.class);
                startActivityForResult(intent, RECORD_RETURN_OK);
            }
        });


        ListView listView = (ListView) findViewById(R.id.list_view);
        money_cost_list = getData();
        simpleAdapter = new SimpleAdapter(this, money_cost_list, R.layout.list_view,
                new String[]{RecorderActivity.TIME, RecorderActivity.MONEY},
                new int[]{R.id.list_title, R.id.list_text});
        listView.setAdapter(simpleAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECORD_RETURN_OK && resultCode == RESULT_OK) {
            String line = data.getStringExtra(RecorderActivity.NEW_DATA);
            Map<String, String> map = DataStorage.parseLine(line);
            money_cost_list.add(map);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    private List<Map<String, String>> getData() {

        if (money_cost_list == null) {
            money_cost_list = new LinkedList<Map<String, String>>();
        }

        LinkedList<String> list = DataStorage.getLines();
        for (String i : list) {
            money_cost_list.add(DataStorage.parseLine(i));
        }

        return money_cost_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
