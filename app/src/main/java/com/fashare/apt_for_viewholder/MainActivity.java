package com.fashare.apt_for_viewholder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.annotation.Adapter;
import com.example.annotation.Field;
import com.example.annotation.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv) RecyclerView mRv;

    @Adapter(
            moduleName = "Main",
            viewHolder = @ViewHolder(
                    layoutRes = android.R.layout.simple_list_item_1,
                    fields = {
                            @Field(idRes = android.R.id.text1, clazz = TextView.class),
                    }
            )
    )
    mAdapter$$Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRv.setAdapter(mAdapter = new mAdapter$$Adapter(this));
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setDataList(Arrays.asList("1", "2", "3", "4", "5"));
    }

    @ViewHolder(
            layoutRes = android.R.layout.simple_list_item_1,
            fields = {
                    @Field(idRes = android.R.id.text1, clazz = TextView.class),
            }
    )
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter$$ViewHolder> {
        Context mContext;
        List<String> mDataList;

        public void setDataList(List<String> dataList) {
            mDataList = dataList;
            notifyDataSetChanged();
        }

        public MyAdapter(Context context) {
            mContext = context;
            mDataList = new ArrayList<>();
        }

        @Override
        public MyAdapter$$ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyAdapter$$ViewHolder(mContext);
        }

        @Override
        public void onBindViewHolder(MyAdapter$$ViewHolder holder, int position) {
            holder.bind(mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataList!=null? mDataList.size(): 0;
        }
    }
}
