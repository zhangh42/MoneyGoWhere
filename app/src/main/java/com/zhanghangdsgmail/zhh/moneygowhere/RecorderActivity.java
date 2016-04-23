package com.zhanghangdsgmail.zhh.moneygowhere;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


/**
 * 记录类。该类主要用于录入每次消费的各种信息。
 */

// FIXME: 2016/4/14 当note为空时会引起程序崩溃.
// TODO: 2016/4/14 采用数据库储存数据，以便于删除操作

public class RecorderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static String NEW_DATA = "a_new_data";

    public final static String TIME = "时间";
    public final static String NOTE = "备注";
    public final static String PAY_WAY = "支付方式";
    public final static String USE = "用途";
    public final static String MONEY = "金额";

    private static final String TAG = "RecorderActivity";

    private String time;
    private String note;
    private String pay_way = "现金";
    private String use = "";
    private double money;

    private TextView time_view;
    private Spinner spinner;
    private EditText moneyEditText;
    private EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recorder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.recorder_confirm) {
            note = noteEditText.getText().toString();

            String moneyStr = moneyEditText.getText().toString();
            if (moneyStr == null || moneyStr.equals("")) {
                Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
                return true;
            }
            money = Double.parseDouble(moneyEditText.getText().toString());

            String line = TIME + "=" + time + " "
                    + PAY_WAY + "=" + pay_way + " "
                    + USE + "=" + use + " "
                    + MONEY + "=" + money + " "
                    + NOTE + "=" + note;

            Toast.makeText(this, line, Toast.LENGTH_LONG).show();
            DataStorage.writeLine(line);

            Intent intent = new Intent();
            intent.putExtra(NEW_DATA, line);
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    void init() {
        time_view = (TextView) findViewById(R.id.recorder_time_view);
        spinner = (Spinner) findViewById(R.id.recorder_use_spinner);
        noteEditText = (EditText) findViewById(R.id.recorder_note);
        moneyEditText = (EditText) findViewById(R.id.recorder_money);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        this.time = year + "/" + month + "/" + day;
        time_view.setText("Time: " + this.time);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_use_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * 处理用户勾选的选项
     *
     * @param view
     */
    public void onCheckboxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean isChecked = checkBox.isChecked();

        // FIXME: 2016/4/17 此处可能会出现重复输入，可以改为先存在set中，最后在存入use
        if (isChecked) {
            if (use.equals(""))
                use += checkBox.getText();
            else
                use += "+" + checkBox.getText();
        }
    }

    /**
     * 展示时间选择器
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment(time, time_view);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pay_way = parent.getItemAtPosition(position).toString(); // FIXME: 2016/4/9
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "spinner nothing selected", Toast.LENGTH_SHORT).show();
    }

    /**
     * 时间选取器
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private String time;
        private TextView time_view;

        public DatePickerFragment(String time, TextView time_view) {
            this.time = time;
            this.time_view = time_view;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            this.time = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            time_view.setText("Time: " + time);
        }
    }
}
