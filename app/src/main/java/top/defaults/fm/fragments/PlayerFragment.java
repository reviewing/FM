package top.defaults.fm.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import top.defaults.fm.R;
import top.defaults.fm.utils.ImageUtils;
import top.defaults.fm.utils.LogUtils;
import top.defaults.fm.utils.ViewUtils;

/**
 * @author duanhong
 * @version 1.0, 9/9/16 2:22 PM
 */
public class PlayerFragment extends BaseFragment {

    ViewHolder vh = new ViewHolder();

    private boolean shouldUpdateProgress = true;

    private XmPlayerManager playerManager;
    private IXmPlayerStatusListener playerStatusListener = new IXmPlayerStatusListener() {
        @Override
        public void onPlayStart() {
            LogUtils.d("onPlayStart");
            vh.play.setImageResource(android.R.drawable.ic_media_pause);
        }

        @Override
        public void onPlayPause() {
            LogUtils.d("onPlayPause");
            vh.play.setImageResource(android.R.drawable.ic_media_play);
        }

        @Override
        public void onPlayStop() {
            LogUtils.d("onPlayStop");
            vh.play.setImageResource(android.R.drawable.ic_media_play);
        }

        @Override
        public void onSoundPlayComplete() {
            LogUtils.d("onSoundPlayComplete");
            vh.play.setImageResource(android.R.drawable.ic_media_play);
        }

        @Override
        public void onSoundPrepared() {
            LogUtils.d("onSoundPrepared");
            vh.progress.setEnabled(true);
        }

        @Override
        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
            LogUtils.d("onSoundSwitch");
            PlayableModel model = playerManager.getCurrSound();
            if (model != null) {
                String title = null;
                String author = null;
                String cover = null;
                if (model instanceof Track) {
                    Track info = (Track) model;
                    title = info.getTrackTitle();
                    author = info.getAnnouncer() == null ? "" : info.getAnnouncer().getNickname();
                    cover = info.getCoverUrlMiddle();
                } else if (model instanceof Schedule) {
                    Schedule program = (Schedule) model;
                    author = program.getRelatedProgram().getProgramName();
                    title = program.getRelatedProgram().getProgramName();
                    cover = program.getRelatedProgram().getBackPicUrl();
                } else if (model instanceof Radio) {
                    Radio radio = (Radio) model;
                    title = radio.getRadioName();
                    author = radio.getRadioDesc();
                    cover = radio.getCoverUrlSmall();
                }
                vh.title.setText(title);
                vh.author.setText(author);
                ImageUtils.setImageUri(vh.cover, Uri.parse(cover), getActivity().getResources().getDimensionPixelSize(R.dimen.player_cover_image_size));
            }

            vh.progress.setEnabled(false);
            vh.progress.setSecondaryProgress(0);
            vh.previous.setEnabled(playerManager.hasPreSound());
            vh.next.setEnabled(playerManager.hasNextSound());
        }

        @Override
        public void onBufferingStart() {
            LogUtils.d("onBufferingStart");
        }

        @Override
        public void onBufferingStop() {
            LogUtils.d("onBufferingStop");
        }

        @Override
        public void onBufferProgress(int position) {
            LogUtils.d("onBufferProgress: %d", position);
            vh.progress.setSecondaryProgress(position);
        }

        @Override
        public void onPlayProgress(int currPos, int duration) {
            LogUtils.d("onPlayProgress: %d, %d", currPos, duration);
            vh.played.setText(ViewUtils.formatTime(currPos));
            vh.remained.setText(ViewUtils.formatTime(duration - currPos));
            if (shouldUpdateProgress && duration != 0) {
                vh.progress.setProgress((int) (100 * currPos / (float) duration));
            }
        }

        @Override
        public boolean onError(XmPlayerException e) {
            LogUtils.e("onError " + e.getMessage());
            vh.play.setImageResource(android.R.drawable.ic_media_play);
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vh.cover = (SimpleDraweeView) view.findViewById(R.id.fragment_player_cover);
        vh.title = (TextView) view.findViewById(R.id.fragment_player_title);
        vh.author = (TextView) view.findViewById(R.id.fragment_player_author);
        vh.played = (TextView) view.findViewById(R.id.fragment_player_time_played);
        vh.remained = (TextView) view.findViewById(R.id.fragment_player_time_remained);
        vh.progress = (SeekBar) view.findViewById(R.id.fragment_player_progress);
        vh.previous = (ImageButton) view.findViewById(R.id.fragment_player_prev);
        vh.play = (ImageButton) view.findViewById(R.id.fragment_player_play);
        vh.next = (ImageButton) view.findViewById(R.id.fragment_player_next);

        vh.progress.setEnabled(false);
        vh.progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerManager.seekToByPercent(seekBar.getProgress() / (float) seekBar.getMax());
                shouldUpdateProgress = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                shouldUpdateProgress = false;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });

        vh.previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                playerManager.playPre();
            }
        });

        vh.play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (playerManager.isPlaying()) {
                    playerManager.pause();
                } else {
                    playerManager.play();
                }
            }
        });

        vh.next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                playerManager.playNext();
            }
        });


        playerManager = XmPlayerManager.getInstance(getActivity());
        playerManager.addPlayerStatusListener(playerStatusListener);
    }

    private static class ViewHolder {
        SimpleDraweeView cover;
        TextView title;
        TextView author;
        TextView played;
        TextView remained;
        SeekBar progress;
        ImageButton previous;
        ImageButton play;
        ImageButton next;
    }
}
