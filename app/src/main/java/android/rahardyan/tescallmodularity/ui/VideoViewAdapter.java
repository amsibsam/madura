package android.rahardyan.tescallmodularity.ui;

import android.app.Activity;
import android.content.Context;
import android.rahardyan.tescallmodularity.R;
import android.rahardyan.tescallmodularity.UserStatusData;
import android.rahardyan.tescallmodularity.VideoInfoData;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class VideoViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final LayoutInflater mInflater;
    protected final Context mContext;

    protected final ArrayList<UserStatusData> mUsers;

    protected final VideoViewEventListener mListener;

    protected int mLocalUid;

    public VideoViewAdapter(Context context, int localUid, HashMap<Integer, SoftReference<SurfaceView>> uids, VideoViewEventListener listener) {
        mContext = context;
        mInflater = ((Activity) context).getLayoutInflater();

        mLocalUid = localUid;

        mListener = listener;

        mUsers = new ArrayList<>();

        init(uids);
    }

    protected int mItemWidth;
    protected int mItemHeight;

    private int mDefaultChildItem = 0;

    private void init(HashMap<Integer, SoftReference<SurfaceView>> uids) {
        mUsers.clear();

        customizedInit(uids, true);
    }

    protected abstract void customizedInit(HashMap<Integer, SoftReference<SurfaceView>> uids, boolean force);

    public abstract void notifyUiChanged(HashMap<Integer, SoftReference<SurfaceView>> uids, int uidExtra, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume);

    protected HashMap<Integer, VideoInfoData> mVideoInfo; // left user should removed from this HashMap

    public void addVideoInfo(int uid, VideoInfoData video) {
        if (mVideoInfo == null) {
            mVideoInfo = new HashMap<>();
        }
        mVideoInfo.put(uid, video);
    }

    public void cleanVideoInfo() {
        mVideoInfo = null;
    }

    public void setLocalUid(int uid) {
        mLocalUid = uid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.video_view_container, parent, false);
        v.getLayoutParams().width = mItemWidth;
        v.getLayoutParams().height = mItemHeight;
        mDefaultChildItem = v.getChildCount();
        return new VideoUserStatusHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoUserStatusHolder myHolder = ((VideoUserStatusHolder) holder);

        final UserStatusData user = mUsers.get(position);

        Log.d("VideoViewAdapter", "onBindViewHolder " + position + " " + user + " " + myHolder + " " + myHolder.itemView + " " + mDefaultChildItem);

        FrameLayout holderView = (FrameLayout) myHolder.itemView;

        holderView.setOnTouchListener(new OnDoubleTapListener(mContext) {
            @Override
            public void onDoubleTap(View view, MotionEvent e) {
                if (mListener != null) {
                    mListener.onItemDoubleClick(view, user);
                }
            }

            @Override
            public void onSingleTapUp() {
            }
        });

        if (holderView.getChildCount() == mDefaultChildItem) {
            SurfaceView target = user.mView.get();
            VideoViewAdapterUtil.stripView(target);
            holderView.addView(target, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        VideoViewAdapterUtil.renderExtraData(mContext, user, myHolder);
    }

    @Override
    public int getItemCount() {
        Log.d("VideoViewAdapter", "getItemCount " + mUsers.size());
        return mUsers.size();
    }

    @Override
    public long getItemId(int position) {
        UserStatusData user = mUsers.get(position);

        SurfaceView view = user.mView.get();
        if (view == null) {
            throw new NullPointerException("SurfaceView destroyed for user " + user.mUid + " " + user.mStatus + " " + user.mVolume);
        }

        return (String.valueOf(user.mUid) + System.identityHashCode(view)).hashCode();
    }
}
