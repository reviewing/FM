package top.defaults.fm.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;

import java.util.Locale;

import top.defaults.fm.R;
import top.defaults.fm.utils.ImageUtils;
import top.defaults.fm.utils.ViewUtils;

/**
 * @author duanhong
 * @version 1.0, 9/13/16 9:34 AM
 */
public class AlbumsAdapter extends BaseAdapter {

    private Context context;
    private AlbumList albumList;

    public AlbumsAdapter(Context context) {
        this.context = context;
    }

    public void setData(AlbumList list) {
        albumList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (albumList == null || albumList.getAlbums() == null) {
            return 0;
        }
        return albumList.getAlbums().size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.getAlbums().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
            ViewHolder vh = new ViewHolder();
            vh.cover = (SimpleDraweeView) convertView.findViewById(R.id.item_album_cover);
            vh.title = (TextView) convertView.findViewById(R.id.item_album_title);
            vh.latestTrackName = (TextView) convertView.findViewById(R.id.item_album_latest_track);
            vh.playCount = (TextView) convertView.findViewById(R.id.item_album_play_count);
            vh.trackCount = (TextView) convertView.findViewById(R.id.item_album_track_count);
            convertView.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) convertView.getTag();
        Album album = (Album) getItem(position);
        ImageUtils.setImageUri(vh.cover, Uri.parse(album.getCoverUrlMiddle()), context.getResources().getDimensionPixelSize(R.dimen.item_album_cover_size));
        vh.title.setText(album.getAlbumTitle());
        vh.latestTrackName.setText(String.format(Locale.US, context.getString(R.string.latest_track), album.getLastUptrack().getTrackTitle()));
        vh.playCount.setText(String.format(Locale.US, context.getString(R.string.play_count), ViewUtils.formatCount(album.getPlayCount())));
        vh.trackCount.setText(String.format(Locale.US, context.getString(R.string.track_count), album.getIncludeTrackCount()));
        return convertView;
    }

    private static final class ViewHolder {
        SimpleDraweeView cover;
        TextView title;
        TextView latestTrackName;
        TextView playCount;
        TextView trackCount;
    }
}
