package top.defaults.fm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    RadioButton tracksRadioButton;
    RadioButton albumsRadioButton;
    RadioButton fmsRadioButton;
    RadioButton columnsRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.activity_main_pager);
        tracksRadioButton = (RadioButton) findViewById(R.id.activity_main_tracks);
        albumsRadioButton = (RadioButton) findViewById(R.id.activity_main_albums);
        fmsRadioButton = (RadioButton) findViewById(R.id.activity_main_fms);
        columnsRadioButton = (RadioButton) findViewById(R.id.activity_main_columns);

        tracksRadioButton.setOnClickListener(this);
        albumsRadioButton.setOnClickListener(this);
        fmsRadioButton.setOnClickListener(this);
        columnsRadioButton.setOnClickListener(this);

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()));
        //noinspection deprecation
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                checkTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void onClick(View view) {
        int title = R.string.tracks;
        switch (view.getId()) {
            case R.id.activity_main_tracks:
                title = R.string.tracks;
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.activity_main_albums:
                title = R.string.albums;
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.activity_main_fms:
                title = R.string.fms;
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.activity_main_columns:
                title = R.string.columns;
                viewPager.setCurrentItem(3, false);
                break;
            default:
                break;
        }
        setTitle(title);
    }

    class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        public FragmentPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment();
                case 1:
                    return new Fragment();
                case 2:
                    return new Fragment();
                case 3:
                    return new Fragment();
                case 4:
                    return new Fragment();
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private void checkTab(int position) {
        RadioButton[] tabs = {tracksRadioButton, albumsRadioButton, fmsRadioButton, columnsRadioButton};
        tabs[position].setChecked(true);
    }
}
