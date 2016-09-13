package top.defaults.fm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import top.defaults.fm.R;
import top.defaults.fm.adapters.TagsAdapter;

/**
 * @author duanhong
 * @version 1.0, 9/12/16 4:56 PM
 */
public class TagsFragment extends BaseFragment {
    public static final String ARGUMENT_CATEGORY_ID = "category.id";
    public static final String ARGUMENT_CATEGORY_NAME = "category.name";

    private TextView hint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hint = (TextView)view.findViewById(R.id.fragment_category_hint);
        ListView listView = (ListView) view.findViewById(R.id.fragment_category_list_view);
        final TagsAdapter adapter = new TagsAdapter(getActivity());
        listView.setAdapter(adapter);

        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.CATEGORY_ID, "" + getArguments().getLong(ARGUMENT_CATEGORY_ID));
        map.put(DTransferConstants.TYPE, "0");
        CommonRequest.getTags(map, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(TagList object) {
                adapter.setData(object);
            }

            @Override
            public void onError(int code, String message) {
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        hint.setText(String.format(Locale.US, getActivity().getString(R.string.please_select_tag),
                getArguments().getString(ARGUMENT_CATEGORY_NAME)));
    }
}
