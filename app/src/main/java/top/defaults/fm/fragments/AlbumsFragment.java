package top.defaults.fm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import top.defaults.fm.R;
import top.defaults.fm.adapters.AlbumsAdapter;
import top.defaults.fm.utils.LogUtils;
import top.defaults.fm.utils.ViewUtils;

/**
 * @author duanhong
 * @version 1.0, 9/13/16 9:32 AM
 */
public class AlbumsFragment extends BaseFragment {
    public static final String ARGUMENT_CATEGORY = "category";
    public static final String ARGUMENT_TAG = "tag";

    private Category category;
    private Tag tag;

    private AlbumsAdapter adapter;
    private AlbumList albumList;
    private TextView hint;

    private boolean mLoading = false;
    private int pageId = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_albums, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        category = new Gson().fromJson(getArguments().getString(ARGUMENT_CATEGORY), Category.class);
        tag = new Gson().fromJson(getArguments().getString(ARGUMENT_TAG), Tag.class);

        hint = (TextView) view.findViewById(R.id.fragment_albums_hint);
        ListView listView = (ListView) view.findViewById(R.id.fragment_albums_list_view);
        adapter = new AlbumsAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    int count = view.getCount();
                    count = count - 5 > 0 ? count - 5 : count - 1;
                    if (view.getLastVisiblePosition() > count && (albumList == null || pageId < albumList.getTotalPage())) {
                        loadAlbums();
                        ViewUtils.showToast(getActivity(), "正在加载更多专辑...");
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        loadAlbums();
    }

    @Override
    public void onResume() {
        super.onResume();
        hint.setText(String.format(Locale.US, getActivity().getString(R.string.please_select_album), category.getCategoryName(), tag.getTagName()));
    }

    private void loadAlbums() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        Map<String ,String> map = new HashMap<>();
        map.put(DTransferConstants.CATEGORY_ID, "" + category.getId());
        map.put(DTransferConstants.CALC_DIMENSION, "1");
        map.put(DTransferConstants.TAG_NAME, tag != null ? tag.getTagName() : "");
        map.put(DTransferConstants.PAGE, "" + pageId);
        map.put(DTransferConstants.PAGE_SIZE, "" + CommonRequest.getInstanse().getDefaultPagesize());
        CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(AlbumList list) {
                ViewUtils.showToast(getActivity(), "加载专辑列表成功");
                if (list != null && list.getAlbums() != null && list.getAlbums().size() != 0) {
                    pageId++;
                    if (albumList == null) {
                        albumList = list;
                    } else {
                        albumList.getAlbums().addAll(list.getAlbums());
                    }
                    adapter.setData(albumList);
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
