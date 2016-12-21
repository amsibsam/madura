package android.rahardyan.tescallmodularity.CallLibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.rahardyan.tescallmodularity.CallLibraryHelper;
import android.rahardyan.tescallmodularity.event.AGEventHandler;
import android.rahardyan.tescallmodularity.AgoraSampleReferences.model.ConstantApp;
import android.rahardyan.tescallmodularity.AgoraSampleReferences.model.EngineConfig;
import android.rahardyan.tescallmodularity.event.MyEngineEventHandler;
import android.rahardyan.tescallmodularity.AgoraSampleReferences.threadhelper.WorkerThread;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

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


    public AgoraExample (Context context) {
        mContext = context;
        CallLibraryHelper.initWorkerThread(context);
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
        return CallLibraryHelper.getWorkerThread().getRtcEngine();
    }

    public final WorkerThread worker() {
        return CallLibraryHelper.getWorkerThread();
    }

    public final EngineConfig config() {
        return CallLibraryHelper.getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return CallLibraryHelper.getWorkerThread().eventHandler();
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

    @Override
    public void onExtraCallback(int type, Object... data) {

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
