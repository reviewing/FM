package top.defaults.fm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.defaults.fm.R;

/**
 * @author duanhong
 * @version 1.0, 9/8/16 4:55 PM
 */
public class TracksFragment extends BaseFragment {
    private ViewHolder vh = new ViewHolder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracks, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
    }

    private static class ViewHolder {

    }
}
