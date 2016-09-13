package top.defaults.fm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import java.util.HashMap;
import java.util.Map;

import top.defaults.fm.R;
import top.defaults.fm.adapters.CategoriesAdapter;

/**
 * @author duanhong
 * @version 1.0, 9/12/16 3:55 PM
 */
public class CategoriesFragment extends BaseFragment {

    private TextView hint;
    private CategoryList categoryList;

    public interface OnCategorySelectedListener {
        void onSelected(Category category);
    }

    private OnCategorySelectedListener listener;

    public void setOnCategorySelectedListener(OnCategorySelectedListener listener) {
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
        hint = (TextView)view.findViewById(R.id.fragment_categories_hint);
        ListView listView = (ListView) view.findViewById(R.id.fragment_categories_list_view);
        final CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onSelected((Category) adapter.getItem(position));
                }
            }
        });

        if (categoryList == null) {
            Map<String, String> map = new HashMap<>();
            CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
                @Override
                public void onSuccess(CategoryList list) {
                    categoryList = list;
                    adapter.setData(list);
                }

                @Override
                public void onError(int code, String message) {
                }

            });
        } else {
            adapter.setData(categoryList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hint.setText(R.string.please_select_category);
    }
}
