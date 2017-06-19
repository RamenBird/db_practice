package com.ramenbird.example.bigfireball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ramenbird.GeneratedSqlClass;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Test> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ListAdapter());

        final MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(this);
        final EditText editText1 = (EditText) findViewById(R.id.input1);
        final EditText editText2 = (EditText) findViewById(R.id.input2);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test test = new Test();
                test.setA(editText1.getText().toString());
                test.setC2(Integer.parseInt(editText2.getText().toString()));
                GeneratedSqlClass.addTest(helper.getWritableDatabase(), test);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.clear();
                mDataList.addAll(GeneratedSqlClass.getAllTest(helper.getReadableDatabase()));
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Test getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, null);
            ((TextView) (view.findViewById(R.id.t1))).setText(getItem(position).getA());
            ((TextView) (view.findViewById(R.id.i2))).setText(getItem(position).getC2() + "");
            return view;
        }
    }

}
