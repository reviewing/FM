package top.defaults.fm.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;

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

                registerTagsFragmentListener(tagsFragment, category);

                Bundle bundle = new Bundle();
                bundle.putString(TagsFragment.ARGUMENT_CATEGORY, new Gson().toJson(category));
                tagsFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.fragment_explore_container, tagsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void registerTagsFragmentListener(TagsFragment tagsFragment, final Category category) {
        tagsFragment.setOnTagSelectedListener(new TagsFragment.OnTagSelectedListener() {
            @Override
            public void onSelected(Tag tag) {
                AlbumsFragment albumsFragment = new AlbumsFragment();

                registerAlbumsFragmentListener(albumsFragment);

                Bundle bundle = new Bundle();
                bundle.putString(AlbumsFragment.ARGUMENT_CATEGORY, new Gson().toJson(category));
                bundle.putString(AlbumsFragment.ARGUMENT_TAG, new Gson().toJson(tag));
                albumsFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.fragment_explore_container, albumsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void registerAlbumsFragmentListener(AlbumsFragment albumsFragment) {
        albumsFragment.setOnAlbumSelectedListener(new AlbumsFragment.OnAlbumSelectedListener() {
            @Override
            public void onSelected(Album album) {
                AlbumTracksFragment albumTracksFragment = new AlbumTracksFragment();
                Bundle bundle = new Bundle();
                bundle.putString(AlbumTracksFragment.ARGUMENT_ALBUM, new Gson().toJson(album));
                albumTracksFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.fragment_explore_container, albumTracksFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
