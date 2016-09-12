package top.defaults.fm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.defaults.fm.R;

/**
 * @author duanhong
 * @version 1.0, 9/8/16 4:55 PM
 */
public class ExploreFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CategoriesFragment fragment = new CategoriesFragment();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_explore_container, fragment).commit();
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
