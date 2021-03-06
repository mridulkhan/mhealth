package com.example.jack.brainwaves;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;

import com.astuetz.PagerSlidingTabStrip;
import com.example.jack.brainwaves.fragments.AboutFragment;
import com.example.jack.brainwaves.fragments.HomeScoreFragment;
import com.example.jack.brainwaves.fragments.LogPlotFragment;
import com.example.jack.brainwaves.fragments.MuseFragment;
import com.example.jack.brainwaves.fragments.PSSFragment;
import com.example.jack.brainwaves.fragments.RealtimeLineChartFragment;
import com.example.jack.brainwaves.fragments.SettingFragment;
import com.example.jack.brainwaves.fragments.SuperAwesomeCardFragment;
import com.example.jack.brainwaves.helper.AccountHelper;
import com.example.jack.brainwaves.helper.LockableViewPager;

public class MainActivity extends FragmentActivity implements
        SettingFragment.onSettingListener,
        MuseFragment.onMuseListener,
        LogPlotFragment.onLogPlotListener,
        RealtimeLineChartFragment.onRealtimeplotListener{

    private final static String[] TITLES = {
            "Log over time",
            "PSS Questionnaires",
            "Home",
            "Real time plot",
            "Muse Config",
            "Settings",
            "About",
    };

    final static int LOGPLOT    = 0;
    final static int PSS        = 1;
    final static int HOME       = 2;
    final static int REALTIME   = 3;
    final static int MUSE       = 4;
    final static int SETTING    = 5;
    final static int ABOUT      = 6;
    final static int START_PAGE = HOME;


    private final Handler handler = new Handler();

    private AccountHelper useraccount;

    private PagerSlidingTabStrip tabs;
    private LockableViewPager pager;
    private MyPagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;

    private boolean demoMode = true;
    private HomeScoreFragment homeFrag = null;
    private MuseFragment muserFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        useraccount = new AccountHelper();

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (LockableViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(START_PAGE, true);
        tabs.setViewPager(pager);

        // Classifier
        BrainwaveClassifier.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
    }

    @Override
    public void onDemomodeSelected(boolean isDemo) {
        demoMode = isDemo;
    }

    @Override
    public void onUserIDSubmitted(String id) {
        useraccount.setLogin(id);
        if(muserFrag != null) {
            muserFrag.updateSessionName(id);
        }
    }

    @Override
    public String tryGetLoginState() {
        if(useraccount.isLogin()) {
            return useraccount.getUsername();
        } else {
            return null;
        }
    }

    @Override
    public boolean tryGetIsdemo() {
        return demoMode;
    }

    @Override
    public void sendClassificationOutput(boolean stressFul) {
        if(homeFrag != null) {
            homeFrag.plotTwoClassification(stressFul);
        }
    }

    @Override
    public void onMuseGetSessionname() {
        if(useraccount.isLogin()) {
            muserFrag.updateSessionName(useraccount.getUsername());
        }
    }

    @Override
    public void onSettingPageLocker(boolean b) {
        pager.setPagingEnabled(!b);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        /*@TODO: return different fragment based on the postiion
            Apparently onCreateView in Fragment is corresponding to onCreate in Activity
         */
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case HOME:
                    homeFrag = HomeScoreFragment.newInstance(position);
                    return homeFrag;
                case MUSE:
                    muserFrag = MuseFragment.newInstance(position);
                    return muserFrag;
                case PSS:
                    return PSSFragment.newInstance(position);
                case ABOUT:
                    return AboutFragment.newInstance(position);
                case SETTING:
                    return SettingFragment.newInstance(position);
                case LOGPLOT:
                    return LogPlotFragment.newInstance(position);
                case REALTIME:
                    return RealtimeLineChartFragment.newInstance(position);
                default:
                    return SuperAwesomeCardFragment.newInstance(position);
            }
        }

    }

}