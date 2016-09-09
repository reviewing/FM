package top.defaults.fm.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackHotList;

import java.util.Locale;

import top.defaults.fm.R;
import top.defaults.fm.utils.ImageUtils;

/**
 * @author duanhong
 * @version 1.0, 9/9/16 9:19 AM
 */
public class TracksAdapter extends BaseAdapter {
    private Context context;
    private TrackHotList trackHotList;

    public TracksAdapter(Context context) {
        this.context = context;
    }

    public void setData(TrackHotList trackHotList) {
        this.trackHotList = trackHotList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (trackHotList == null || trackHotList.getTracks() == null) {
            return 0;
        }
        return trackHotList.getTracks().size();
    }

    @Override
    public Object getItem(int position) {
        return trackHotList.getTracks().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false);
            ViewHolder vh = new ViewHolder();
            vh.rank = (TextView) convertView.findViewById(R.id.item_track_rank);
            vh.cover = (SimpleDraweeView) convertView.findViewById(R.id.item_track_cover);
            vh.title = (TextView) convertView.findViewById(R.id.item_track_title);
            vh.author = (TextView) convertView.findViewById(R.id.item_track_author);
            convertView.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) convertView.getTag();
        Track track = (Track) getItem(position);
        vh.rank.setText(String.format(Locale.US, "%d", position + 1));
        ImageUtils.setImageUri(vh.cover, Uri.parse(track.getCoverUrlLarge()), context.getResources().getDimensionPixelSize(R.dimen.item_track_cover_size));
        vh.title.setText(String.format(Locale.US, "标题：%s", track.getTrackTitle()));
        vh.author.setText(String.format(Locale.US, "作者：%s", track.getAnnouncer() == null ? "" : track.getAnnouncer().getNickname()));
        return convertView;
    }

    private static final class ViewHolder {
        TextView rank;
        SimpleDraweeView cover;
        TextView title;
        TextView author;
    }
}
