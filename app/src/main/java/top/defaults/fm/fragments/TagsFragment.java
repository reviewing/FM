package top.defaults.fm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import top.defaults.fm.R;
import top.defaults.fm.adapters.TagsAdapter;
import top.defaults.fm.utils.ViewUtils;

/**
 * @author duanhong
 * @version 1.0, 9/12/16 4:56 PM
 */
public class TagsFragment extends BaseFragment {
    public static final String ARGUMENT_CATEGORY = "category";
    private Category category;

    private TextView hint;

    public interface OnTagSelectedListener {
        void onSelected(Tag tag);
    }

    private OnTagSelectedListener listener;

    public void setOnTagSelectedListener(OnTagSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        category = new Gson().fromJson(getArguments().getString(ARGUMENT_CATEGORY), Category.class);
        hint = (TextView)view.findViewById(R.id.fragment_categories_hint);

        ListView listView = (ListView) view.findViewById(R.id.fragment_categories_list_view);
        final TagsAdapter adapter = new TagsAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onSelected((Tag) adapter.getItem(position));
                }
            }
        });

        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.CATEGORY_ID, "" + category.getId());
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
        hint.setText(String.format(Locale.US, getActivity().getString(R.string.please_select_tag), category.getCategoryName()));
    }
}
