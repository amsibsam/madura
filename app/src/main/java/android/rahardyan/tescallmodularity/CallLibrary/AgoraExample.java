package android.rahardyan.tescallmodularity.CallLibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.rahardyan.tescallmodularity.AGEventHandler;
import android.rahardyan.tescallmodularity.ClassHelper;
import android.rahardyan.tescallmodularity.Constant;
import android.rahardyan.tescallmodularity.ConstantApp;
import android.rahardyan.tescallmodularity.EngineConfig;
import android.rahardyan.tescallmodularity.MyEngineEventHandler;
import android.rahardyan.tescallmodularity.R;
import android.rahardyan.tescallmodularity.UserStatusData;
import android.rahardyan.tescallmodularity.VideoInfoData;
import android.rahardyan.tescallmodularity.WorkerThread;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.rahardyan.tescallmodularity.ui.GridVideoViewContainer;
import android.rahardyan.tescallmodularity.ui.RtlLinearLayoutManager;
import android.rahardyan.tescallmodularity.ui.SmallVideoViewAdapter;
import android.rahardyan.tescallmodularity.ui.SmallVideoViewDecoration;
import android.rahardyan.tescallmodularity.ui.VideoViewEventListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import static com.opentok.client.DeviceInfo.getApplicationContext;

/**
 * Created by rahardyan on 17/12/16.
 */

public class AgoraExample extends CallLibrary implements AGEventHandler {
    private RelativeLayout rootContainer;
    private RelativeLayout smallVideoView;

    private CallEvent listener;
    private Context mContext;
    private String mTarget;
    private final HashMap<Integer, SoftReference<SurfaceView>> mUidsList = new HashMap<>();
    private RelativeLayout mSmallVideoViewDock;
    private ArrayList<View> childs = new ArrayList<>();
    private Activity mActivity;
    SurfaceView localVideo;
    SurfaceView remoteVideo;

    private volatile boolean mAudioMuted = false;

    public int mLayoutType = LAYOUT_TYPE_DEFAULT;
    public static final int LAYOUT_TYPE_DEFAULT = 0;
    public static final int LAYOUT_TYPE_SMALL = 1;

    private GridVideoViewContainer mGridVideoViewContainer;


    public AgoraExample(Context context) {
        mContext = context;
        ClassHelper.initWorkerThread(context);
        event().addEventHandler(this);
    }

    public void setListener(CallEvent listener){
        this.listener = listener;
    }

    public void setRootContainer(Activity activity, RelativeLayout container, RelativeLayout smallVideo, String encryptionKey, String encryptionMode){
        mActivity = activity;
        doConfigEngine(encryptionKey, encryptionMode);
        rootContainer = container;
        smallVideoView = smallVideo;
//        mGridVideoViewContainer = new GridVideoViewContainer(mContext);
//        mGridVideoViewContainer.setLayoutParams(new
//                RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        mGridVideoViewContainer.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        localVideo = RtcEngine.CreateRendererView(mContext);
        rtcEngine().setupLocalVideo(new VideoCanvas(localVideo, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        localVideo.setZOrderOnTop(false);
        localVideo.setZOrderMediaOverlay(false);
        mUidsList.put(0, new SoftReference<>(localVideo)); // get first surface view

//        mGridVideoViewContainer.initViewContainer(mActivity, 0, mUidsList); // first is now full view
//        rootContainer.addView(mGridVideoViewContainer);
        rootContainer.addView(localVideo);
        worker().preview(true, localVideo, 0);

        Log.d("amsibsam", "root set");
    }

    private void doConfigEngine(String encryptionKey, String encryptionMode) {
        int vProfile = ConstantApp.VIDEO_PROFILES[getVideoProfileIndex()];

        worker().configEngine(vProfile, encryptionKey, encryptionMode);
    }

    public RtcEngine rtcEngine() {
        return ClassHelper.getWorkerThread().getRtcEngine();
    }

    public final WorkerThread worker() {
        return ClassHelper.getWorkerThread();
    }

    public final EngineConfig config() {
        return ClassHelper.getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ClassHelper.getWorkerThread().eventHandler();
    }

    @Override
    public void startCall(String target) {
        worker().joinChannel(target, config().mUid);
        mTarget = target;
    }

    @Override
    public void endCall() {
        worker().leaveChannel(mTarget);
        listener.onEndCall("agora");
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
    }

    private void doRenderRemoteUi(final int uid) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity.isFinishing()) {
                    return;
                }

                if (mUidsList.containsKey(uid)) {
                    return;
                }

                remoteVideo = RtcEngine.CreateRendererView(mActivity);
                mUidsList.put(uid, new SoftReference<>(remoteVideo));

                boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 2;

                remoteVideo.setZOrderOnTop(!useDefaultLayout);
                remoteVideo.setZOrderMediaOverlay(!useDefaultLayout);

                rtcEngine().setupRemoteVideo(new VideoCanvas(remoteVideo, VideoCanvas.RENDER_MODE_HIDDEN, uid));

                rootContainer.removeAllViews();
                smallVideoView.removeAllViews();
                rootContainer.addView(remoteVideo);
                smallVideoView.addView(localVideo);
            }
        });

    }

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock.setVisibility(View.GONE);
        }
        mGridVideoViewContainer.initViewContainer(mActivity, config().mUid, mUidsList);

        mLayoutType = LAYOUT_TYPE_DEFAULT;
    }



    private void requestRemoteStreamType(final int currentHostCount) {
        Log.d("amsibsam", "requestRemoteStreamType " + currentHostCount);
    }

//    private void bindToSmallVideoView(int exceptUid) {
//        if (mSmallVideoViewDock == null) {
//            ViewStub stub = new ViewStub(mContext);
//            stub.setInflatedId(R.id.small_video_view_container);
//            stub.setLayoutResource(R.layout.small_video_view_dock);
//            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
//        }
//
//        boolean twoWayVideoCall = mUidsList.size() == 2;
//
//        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);
//
//        boolean create = false;
//
//        if (mSmallVideoViewAdapter == null) {
//            create = true;
//            mSmallVideoViewAdapter = new SmallVideoViewAdapter(mContext, config().mUid, exceptUid, mUidsList, new VideoViewEventListener() {
//                @Override
//                public void onItemDoubleClick(View v, Object item) {
//                    switchToDefaultVideoView();
//                }
//            });
//            mSmallVideoViewAdapter.setHasStableIds(true);
//        }
//        recycler.setHasFixedSize(true);
//
//        Log.d("amsibsam", "bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid & 0xFFFFFFFFL));
//
//        if (twoWayVideoCall) {
//            recycler.setLayoutManager(new RtlLinearLayoutManager(mContext, RtlLinearLayoutManager.HORIZONTAL, false));
//        } else {
//            recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//        }
//        recycler.addItemDecoration(new SmallVideoViewDecoration());
//        recycler.setAdapter(mSmallVideoViewAdapter);
//
//        recycler.setDrawingCacheEnabled(true);
//        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
//
//        if (!create) {
//            mSmallVideoViewAdapter.setLocalUid(config().mUid);
//            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
//        }
//        recycler.setVisibility(View.VISIBLE);
//        mSmallVideoViewDock.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        Log.d("Example", "onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
        SoftReference<SurfaceView> local = mUidsList.remove(0);

        if (local == null) {
            return;
        }

        mUidsList.put(uid, local);

        rtcEngine().muteLocalAudioStream(mAudioMuted);

        worker().getRtcEngine().setEnableSpeakerphone(true);

        listener.onConversationStart();
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        doRenderRemoteUi(uid);
    }

    private void doRemoveRemoteUi(final int uid) {


        Object target = mUidsList.remove(uid);
        if (target == null) {
            return;
        }

        int bigBgUid = -1;
        if (mSmallVideoViewAdapter != null) {
            bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
        }

        Log.d("amsibsam", "doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL) + " " + mLayoutType);

        if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
            switchToDefaultVideoView();
        } else {
//            switchToSmallVideoView(bigBgUid);
        }
    }

    @Override
    public void onExtraCallback(int type, Object... data) {
        doHandleExtraCallback(type, data);
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                doHideTargetView(peerUid, muted);

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.receivedFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoProfileIndex();
                        String resolution = mContext.getResources().getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = mContext.getResources().getStringArray(R.array.string_array_frame_rate)[profileIndex];
                        String bitrate = mContext.getResources().getStringArray(R.array.string_array_bit_rate)[profileIndex];

                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);

                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
                                width > height ? height : width,
                                0, Integer.valueOf(fps), Integer.valueOf(bitrate)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }

                break;
        }
    }

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
                Log.w("AgoraCallActivity", "SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid & 0xFFFFFFFFL) + " taget: " + (targetUid & 0xFFFFFFFFL) + "==" + targetUid + " " + status);
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }

    private int getVideoProfileIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        int profileIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (profileIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            profileIndex = ConstantApp.DEFAULT_PROFILE_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, profileIndex);
            editor.apply();
        }
        return profileIndex;
    }

}
