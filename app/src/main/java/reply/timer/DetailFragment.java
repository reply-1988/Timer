package reply.timer;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import reply.timer.DateBase.CountDown;


public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private EditText mEditText;
    private Button mDateButton;

    private CountDown mCountDown;
    private int Id;
    private List ChangedType;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(int param) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Id = (int) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cound_down_item_detail, container, false);
        mEditText = v.findViewById(R.id.Thing);
        mDateButton = v.findViewById(R.id.time);
        mCountDown = LitePal.find(CountDown.class, Id);

        mEditText.setText(mCountDown.getName());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //将修改后的数据存入到数据库中
                mCountDown.setName(s.toString());
                mCountDown.save();
                //发送事情发生变化的事件
                EventBus.getDefault().post(new MessageEvent(s.toString(), Id, 1));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton.setText(mCountDown.getDate());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = null;
                try {
                    datePickerFragment = DatePickerFragment.newInstance(sdf.parse(mCountDown.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert datePickerFragment != null;
                datePickerFragment.setTargetFragment(DetailFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });
        return v;
    }

    //处理从DatePickerFragment中返回的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            //将修改的日期存储到数据库中
            mCountDown.setDate(sdf.format(date));
            mCountDown.save();
            mDateButton.setText(sdf.format(date));
            //发送时间变化的时间
            EventBus.getDefault().post(new MessageEvent(sdf.format(date), Id, 2));
        }
    }
}
