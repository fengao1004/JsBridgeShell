package com.dayang.browsemediafileslibrary.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayang.browsemediafileslibrary.R;

import java.io.IOException;
import java.util.Date;

/**
 * Created by 冯傲 on 2017/3/20.
 * e-mail 897840134@qq.com
 */

public class PlayAudioFragment extends Fragment {
    String path;
    private View inflate;
    private Button playButton;
    private Button stopButton;
    private TextView alltimelen;
    private Chronometer mChronometer;
    private MediaPlayer mPlayer;
    boolean playStatus = false;
    boolean isPlaying = false;
    long startPauseTime;
    long endPauseTime;
    private RelativeLayout rl_audio_loading;

    public static PlayAudioFragment newInstance(String path) {
        PlayAudioFragment newFragment = new PlayAudioFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        path = getArguments().getString("path");
        inflate = inflater.inflate(R.layout.fragment_browse_audio, container, false);
        playButton = (Button) inflate.findViewById(R.id.window_playaudio);
        stopButton = (Button) inflate.findViewById(R.id.window_stopaudio);
        alltimelen = (TextView) inflate.findViewById(R.id.window_alltimelen);
        mChronometer = (Chronometer) inflate.findViewById(R.id.window_chronometer);
        rl_audio_loading = (RelativeLayout) inflate.findViewById(R.id.rl_audio_loading);
        init();
        return inflate;
    }

    private void init() {
        rl_audio_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setFormat("%s");
        mChronometer.setText("00:00");
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer ch) {
                if (ch.getText().equals(alltimelen.getText())) {
                    ch.stop();
                }
            }
        });
    }


    private void stop() throws IOException {
        if (mPlayer != null && isPlaying) {
            mPlayer.seekTo(0);
            mPlayer.pause();
            isPlaying = false;
            playStatus = false;
            playButton.setBackgroundResource(R.drawable.window_play);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            endPauseTime = 0;
            startPauseTime = 0;
        }
    }

    private void play() throws IOException {
        if (!playStatus) {
            playButton.setBackgroundResource(R.drawable.pause2);
            if (mPlayer == null) {
                rl_audio_loading.setVisibility(View.VISIBLE);
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(path);
                mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        Log.i("fengao", "onBufferingUpdate: " + percent);
                    }
                });
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.i("fengao", "onPrepared: ");
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        mPlayer.start();
                        mChronometer.start();
                        int duration = mPlayer.getDuration();
                        String totalTime = getHHMMSS(duration);
                        alltimelen.setText(totalTime);
                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                playStatus = false;
                                isPlaying = false;
                                playButton.setBackgroundResource(R.drawable.window_play);
                                mChronometer.stop();
                                endPauseTime = 0;
                                startPauseTime = 0;
                                mChronometer.setBase(SystemClock.elapsedRealtime());
                            }
                        });
                        rl_audio_loading.setVisibility(View.GONE);
                    }
                });
                mPlayer.prepareAsync();
            } else {
                endPauseTime = new Date().getTime();
                if (isPlaying) {
                    mChronometer.setBase(mChronometer.getBase() + (endPauseTime - startPauseTime));
                } else {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                }
                mPlayer.start();
                mChronometer.start();
            }
            playStatus = true;
            isPlaying = true;
        } else {
            playButton.setBackgroundResource(R.drawable.window_play);
            startPauseTime = new Date().getTime();
            playStatus = false;
            mPlayer.pause();
            mChronometer.stop();
        }

    }

    public String getHHMMSS(int duration) {
        int second = (duration / 1000) % 60;
        int minute = ((duration / 1000) / 60) % 60;
        return minute + " : " + second;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mPlayer != null && !isVisibleToUser) {
            playButton.setBackgroundResource(R.drawable.window_play);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mPlayer.stop();
            mPlayer.release();
            playStatus = false;
            isPlaying = false;
            mPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayer != null ) {
            playButton.setBackgroundResource(R.drawable.window_play);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mPlayer.stop();
            mPlayer.release();
            playStatus = false;
            isPlaying = false;
            mPlayer = null;
        }
    }
}

