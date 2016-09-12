package top.defaults.fm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import top.defaults.fm.R;

/**
 * @author duanhong
 * @version 1.0, 9/12/16 4:27 PM
 */
public class CategoriesAdapter extends BaseAdapter {
    private Context context;
    private CategoryList categoryList;

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    public void setData(CategoryList list) {
        categoryList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (categoryList == null || categoryList.getCategories() == null) {
            return 0;
        }
        return categoryList.getCategories().size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.getCategories().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
            ViewHolder vh = new ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.item_category_name);
            convertView.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) convertView.getTag();
        Category category = (Category) getItem(position);
        vh.name.setText(category.getCategoryName());
        return convertView;
    }

    private static final class ViewHolder {
        TextView name;
    }
}
