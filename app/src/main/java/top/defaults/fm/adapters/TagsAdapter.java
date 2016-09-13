package top.defaults.fm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

import top.defaults.fm.R;

/**
 * @author duanhong
 * @version 1.0, 9/12/16 4:56 PM
 */
public class TagsAdapter extends BaseAdapter {
    private Context context;
    private TagList tagList;

    public TagsAdapter(Context context) {
        this.context = context;
    }

    public void setData(TagList list) {
        tagList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (tagList == null || tagList.getTagList() == null) {
            return 0;
        }
        return tagList.getTagList().size();
    }

    @Override
    public Object getItem(int position) {
        return tagList.getTagList().get(position);
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
        Tag tag = (Tag) getItem(position);
        vh.name.setText(tag.getTagName());
        return convertView;
    }

    private static final class ViewHolder {
        TextView name;
    }
}
