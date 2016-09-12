package top.defaults.fm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.fragment_category_list_view);
        final CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
        listView.setAdapter(adapter);

        Map<String, String> map = new HashMap<>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(CategoryList object) {
                adapter.setData(object);
            }

            @Override
            public void onError(int code, String message) {
            }

        });
    }
}
