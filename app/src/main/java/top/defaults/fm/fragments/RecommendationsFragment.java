package top.defaults.fm.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.HashMap;
import java.util.Map;

import top.defaults.fm.R;
import top.defaults.fm.utils.LogUtils;

/**
 * @author duanhong
 * @version 1.0, 9/8/16 4:55 PM
 */
public class RecommendationsFragment extends BaseFragment {

    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private CommonRequest ximalaya;
    private XmPlayerManager playerManager;
    private IXmPlayerStatusListener playerStatusListener = new IXmPlayerStatusListener() {
        @Override
        public void onPlayStart() {

        }

        @Override
        public void onPlayPause() {

        }

        @Override
        public void onPlayStop() {

        }

        @Override
        public void onSoundPlayComplete() {

        }

        @Override
        public void onSoundPrepared() {

        }

        @Override
        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {

        }

        @Override
        public void onBufferingStart() {

        }

        @Override
        public void onBufferingStop() {

        }

        @Override
        public void onBufferProgress(int i) {

        }

        @Override
        public void onPlayProgress(int i, int i1) {

        }

        @Override
        public boolean onError(XmPlayerException e) {
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PlayerFragment playerFragment = new PlayerFragment();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.player_container, playerFragment).commit();
        return inflater.inflate(R.layout.fragment_recommendations, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        ximalaya = CommonRequest.getInstanse();

        playerManager = XmPlayerManager.getInstance(getActivity());
        playerManager.addPlayerStatusListener(playerStatusListener);

        viewPager = (ViewPager) rootView.findViewById(R.id.fragment_recommendations_pager);
        adapter = new FragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        //noinspection deprecation
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Map<String, String> map = new HashMap<>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(CategoryList object) {
                adapter.setCategoryList(object);
            }

            @Override
            public void onError(int code, String message) {
            }

        });
    }

    @Override
    public void onDestroyView() {
        LogUtils.i("onDestroyView");
        if (playerManager != null) {
            playerManager.removePlayerStatusListener(playerStatusListener);
        }
        super.onDestroyView();
    }

    class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        private CategoryList categoryList;

        public FragmentPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public void setCategoryList(CategoryList list) {
            categoryList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (categoryList == null || categoryList.getCategories() == null) {
                return 0;
            }
            return categoryList.getCategories().size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new HotTracksFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(HotTracksFragment.ARGUMENT_CATEGORY_ID, categoryList.getCategories().get(position).getId());
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_UNCHANGED;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (categoryList == null || categoryList.getCategories() == null
                    || categoryList.getCategories().size() <= position) {
                return "";
            }
            return categoryList.getCategories().get(position).getCategoryName();
        }
    }
}
