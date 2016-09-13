package top.defaults.fm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
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
 * @version 1.0, 9/13/16 11:48 AM
 */
public class AlbumTracksFragment extends BaseFragment {

    public static final String ARGUMENT_ALBUM = "album";

    private Album album;
    private TracksAdapter adapter;
    private TrackList trackList;

    private boolean mLoading = false;
    private int pageId = 1;
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
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_tracks, container, false);
        PlayerFragment playerFragment = new PlayerFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.player_container, playerFragment).commit();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        album = new Gson().fromJson(getArguments().getString(ARGUMENT_ALBUM), Album.class);

        ListView listView = (ListView) view.findViewById(R.id.fragment_album_tracks_list_view);
        adapter = new TracksAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    int count = view.getCount();
                    count = count - 5 > 0 ? count - 5 : count - 1;
                    if (view.getLastVisiblePosition() > count && (trackList == null || pageId <= trackList.getTotalPage())) {
                        loadTracks();
                        ViewUtils.showToast(getActivity(), "正在加载更多声音...");
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
                playerManager.playList(trackList, position);
            }
        });

        loadTracks();
    }

    private void loadTracks() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, "" + album.getId());
        map.put(DTransferConstants.SORT, "desc");
        map.put(DTransferConstants.PAGE, "" + pageId);
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList list) {
                ViewUtils.showToast(getActivity(), "加载声音列表成功");
                if (list != null && list.getTracks() != null && list.getTracks().size() != 0) {
                    pageId++;
                    if (trackList == null) {
                        trackList = list;
                    } else {
                        trackList.getTracks().addAll(list.getTracks());
                    }
                    adapter.setData(trackList);
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

    @Override
    public void onDestroyView() {
        LogUtils.i("onDestroyView");
        if (playerManager != null) {
            playerManager.removePlayerStatusListener(playerStatusListener);
        }
        super.onDestroyView();
    }
}
