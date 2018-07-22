package reply.timer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

import reply.timer.DateBase.CountDown;

public class TabFragment extends Fragment {

    private static final String PAGE_TYPE = "type";

    private RecyclerView mRecyclerView;

    private List<CountDown> mCountDowns;
    private CountDownAdapter mCountDownAdapter;
    private int position;

    public TabFragment() {

    }

    public static TabFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putCharSequence(PAGE_TYPE, type);

        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(TabFragment.this);
        String type = (String) getArguments().getCharSequence(PAGE_TYPE);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.page);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private class CountDownHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mDateView;
        private TextView mThingView;
        private CountDown mCountDown;

        public CountDownHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_countdown, parent, false));
            mDateView = itemView.findViewById(R.id.Date);
            mThingView = itemView.findViewById(R.id.Thing);
            itemView.setOnClickListener(this);
        }

        public void bind(CountDown countDown) {
            mCountDown = countDown;
            mDateView.setText(countDown.getDate());
            mThingView.setText(countDown.getName());
        }

        @Override
        public void onClick(View v) {
            int countdown_id = mCountDown.getId();
            Intent intent = DetailActivity.newIntent(getActivity(), countdown_id);
            startActivity(intent);
            position = getAdapterPosition();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class CountDownAdapter extends Adapter<CountDownHolder> {

        private List<CountDown> mDowns;

        private CountDownAdapter(List<CountDown> CountDowns) {
            mDowns = CountDowns;
        }

        @NonNull
        @Override
        public CountDownHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CountDownHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CountDownHolder holder, int position) {
            Log.i("bind", "" + position);
            CountDown countDown = mDowns.get(position);
            Log.i("ceshi", countDown.getName() + countDown.getDate());
            holder.bind(countDown);
        }

        @Override
        public void onBindViewHolder(@NonNull CountDownHolder holder, int position, @NonNull List<Object> payloads) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else {
                switch (payloads.get(0).toString()) {
                    case "Date":
                        holder.mDateView.setText(mDowns.get(position).getDate());
                    case "Thing":
                        holder.mThingView.setText(mDowns.get(position).getName());
                }
            }
        }

        @Override
        public int getItemCount() {
            return mDowns.size();
        }
    }

    private void updateUI() {

        if (mCountDownAdapter == null) {
            mCountDowns = LitePal.findAll(CountDown.class);
            mCountDownAdapter = new CountDownAdapter(mCountDowns);
            mRecyclerView.setAdapter(mCountDownAdapter);
        } else {
//            mCountDowns.clear();
//            mCountDowns.addAll(LitePal.findAll(CountDown.class));
            mCountDownAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(MessageEvent messageEvent) {
        String message = messageEvent.message;
        int position = messageEvent.position;
        int type = messageEvent.type;
        switch (type) {
            case 1:{
                //此处要减1是因为Event是从1开始计算的，而mCountDowns是从0开始计算的
                CountDown oldCountDown = mCountDowns.get(position - 1);
                oldCountDown.setName(message);
                mCountDowns.set(position - 1, oldCountDown);
                Log.i("返回的数据位置", position + "");
                break;
            }
            case 2:{
                CountDown oldCountDown = mCountDowns.get(position - 1);
                oldCountDown.setDate(message);
                mCountDowns.set(position - 1, oldCountDown);
                break;
            }
        }
    }
}
