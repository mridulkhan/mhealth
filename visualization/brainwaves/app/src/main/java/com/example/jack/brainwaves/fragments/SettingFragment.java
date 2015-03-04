package com.example.jack.brainwaves.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.brainwaves.R;
import com.example.jack.brainwaves.helper.OrientationHelper;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * Created by jack on 3/2/15.
 */
public class SettingFragment extends SuperAwesomeCardFragment {

    private SwitchButton mDemosb, mSyncsb;
    private Button loginbtn, logoutbtn;
    private EditText usernameEditText, passwordEditText;
    private TextView youridshowTextView;

    public static SettingFragment newInstance(int position) {
        SettingFragment f = new SettingFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = i;
        container = c;
        mMainActivity = getActivity();
        isLandscape = OrientationHelper.isLandsacpe(mMainActivity);
        inflateLayout2Fragment(R.layout.fragment_setting);

        // Init views
        mDemosb = (SwitchButton) findViewById(R.id.mDemosb);
        mSyncsb = (SwitchButton) findViewById(R.id.mSyncsb);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        youridshowTextView = (TextView) findViewById(R.id.youridshow);
        mDemosb.setChecked(true);
        mSyncsb.setChecked(true);
        logoutbtn.setVisibility(View.GONE);
        usernameEditText.setSingleLine();
        passwordEditText.setSingleLine();
        passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // Setup events
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = usernameEditText.getText().toString();
                if(id == null || id.equals("")) {
                    ((onSettingListener) mMainActivity).onUserIDSubmitted("[default_name]");
                    Toast.makeText(mMainActivity, "Please provide a username!", Toast.LENGTH_SHORT).show();
                } else {
                    ((onSettingListener) mMainActivity).onUserIDSubmitted(id);
                    usernameEditText.setEnabled(false);
                    passwordEditText.setEnabled(false);
                    youridshowTextView.setText(id);
                    logoutbtn.setVisibility(View.VISIBLE);
                    loginbtn.setVisibility(View.GONE);
                }
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEditText.setText("");
                youridshowTextView.setText("[Please login]");
                usernameEditText.setEnabled(true);
                passwordEditText.setEnabled(true);
                logoutbtn.setVisibility(View.GONE);
                loginbtn.setVisibility(View.VISIBLE);
                ((onSettingListener) mMainActivity).onUserIDSubmitted("[default_name]");
            }
        });

        return mMainView;
    }

    // Container Activity must implement this interface
    public interface onSettingListener {
        public void onDemomodeSelected(boolean isDemo);
        public void onUserIDSubmitted(String id);
    }
}
