package com.RRG.usbninja;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.RRG.usbninja.app.MainApplication;
import com.wise.wisekit.activity.BaseActivity;

import java.util.ArrayList;

public class LogActivity extends BaseActivity {

    ListView mListView;
    InfoListAdapter mInfoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_log, topContentView);

        initView();

        mInfoListAdapter.setInfoList(MainApplication.instance.logList);
    }

    void initView()  {

        topLeftBtn.setVisibility(View.VISIBLE);
        setTitle("Log");
        topRightBtn.setVisibility(View.VISIBLE);
        setRightText("Clear");

        topRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication.instance.logList.clear();
                mInfoListAdapter.setInfoList(MainApplication.instance.logList);
            }
        });


        mListView = findViewById(R.id.list);

        mInfoListAdapter = new InfoListAdapter(this);
        mListView.setAdapter(mInfoListAdapter);

    }

    // Adapter for holding devices found through scanning.
    private class InfoListAdapter extends BaseAdapter
    {
        private ArrayList<LogInfo> mInfoList;
        private LayoutInflater mInflator;

        public InfoListAdapter(Context context) {
            super();
            mInfoList = new ArrayList<LogInfo>();
            mInflator = LayoutInflater.from(context);
        }

        public void setInfoList(ArrayList<LogInfo> infoList) {

            mInfoList = infoList;
            notifyDataSetChanged();
        }

        public void clear() {
            mInfoList.clear();
        }

        @Override
        public int getCount()
        {
            return mInfoList.size();
        }

        @Override
        public Object getItem(int i)
        {
            return mInfoList.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null)
            {
                view = mInflator.inflate(R.layout.log_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.typeTextView = (TextView) view.findViewById(R.id.type);
                viewHolder.contentTextView = (TextView) view.findViewById(R.id.content);
                view.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) view.getTag();
            }


            viewHolder.typeTextView.setText(mInfoList.get(i).getType());
            viewHolder.contentTextView.setText(mInfoList.get(i).getContent());
            if (mInfoList.get(i).getType().equals("send")) {
                viewHolder.typeTextView.setTextColor(Color.BLUE);
            }
            else if (mInfoList.get(i).getType().equals("receive")) {
                viewHolder.typeTextView.setTextColor(Color.rgb(255, 0, 255));
            }
            else if (mInfoList.get(i).getType().equals("error")) {
                viewHolder.typeTextView.setTextColor(Color.RED);
            }
            else {
                viewHolder.typeTextView.setTextColor(Color.BLACK);
            }

            return view;
        }
    }

    static class ViewHolder
    {
        TextView typeTextView;
        TextView contentTextView;
    }
}
