package top.defaults.fm.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ximalaya.ting.android.opensdk.model.category.Category;

import top.defaults.fm.R;

/**
 * @author duanhong
 * @version 1.0, 9/8/16 4:55 PM
 */
public class ExploreFragment extends BaseFragment {
    private CategoriesFragment categoriesFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoriesFragment = new CategoriesFragment();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_explore_container, categoriesFragment).commit();
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoriesFragment.setOnCategorySelectedListener(new CategoriesFragment.OnCategorySelectedListener() {
            @Override
            public void onSelected(Category category) {
                TagsFragment tagsFragment = new TagsFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(TagsFragment.ARGUMENT_CATEGORY_ID, category.getId());
                bundle.putString(TagsFragment.ARGUMENT_CATEGORY_NAME, category.getCategoryName());
                tagsFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_explore_container, tagsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
