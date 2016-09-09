package top.defaults.fm.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.TrackHotList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.HashMap;
import java.util.Map;

import top.defaults.fm.R;
import top.defaults.fm.adapters.TracksAdapter;
import top.defaults.fm.utils.LogUtils;
import top.defaults.fm.utils.ViewUtils;

/**
 * @author duanhong
 * @version 1.0, 9/8/16 4:55 PM
 */
public class TracksFragment extends BaseFragment {

    TracksAdapter adapter;
    TrackHotList trackHotList;
    private boolean mLoading = false;
    private int pageId = 1;
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
        return inflater.inflate(R.layout.fragment_tracks, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        ximalaya = CommonRequest.getInstanse();

        ListView listView = (ListView) rootView.findViewById(R.id.fragment_tracks_list_view);
        adapter = new TracksAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    int count = view.getCount();
                    count = count - 5 > 0 ? count - 5 : count - 1;
                    if (view.getLastVisiblePosition() > count && (trackHotList == null || pageId < trackHotList.getTotalPage())) {
                        loadTracks();
                        ViewUtils.showToast(getActivity(), "正在拉取更多热门声音...");
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        playerManager = XmPlayerManager.getInstance(getActivity());
        playerManager.addPlayerStatusListener(playerStatusListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playerManager.playList(trackHotList, position);
            }
        });

        loadTracks();
    }

    @Override
    public void onDestroyView() {
        LogUtils.i("onDestroyView");
        if (playerManager != null) {
            playerManager.removePlayerStatusListener(playerStatusListener);
        }
        super.onDestroyView();
    }

    private void loadTracks() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        Map<String, String> param = new HashMap<>();
        param.put(DTransferConstants.CATEGORY_ID, "" + 0);
        param.put(DTransferConstants.PAGE, "" + pageId);
        param.put(DTransferConstants.PAGE_SIZE, "" + ximalaya.getDefaultPagesize());
        CommonRequest.getHotTracks(param, new IDataCallBack<TrackHotList>() {

            @Override
            public void onSuccess(TrackHotList object) {
                LogUtils.d("onSuccess " + (object != null));
                ViewUtils.showToast(getActivity(), "拉取声音列表成功");
                if (object != null && object.getTracks() != null && object.getTracks().size() != 0) {
                    pageId++;
                    if (trackHotList == null) {
                        trackHotList = object;
                    } else {
                        trackHotList.getTracks().addAll(object.getTracks());
                    }
                    adapter.setData(trackHotList);
                }
                mLoading = false;
            }

            @Override
            public void onError(int code, String message) {
                LogUtils.e("onError " + code + ", " + message);
                mLoading = false;
            }
        });
    }
}