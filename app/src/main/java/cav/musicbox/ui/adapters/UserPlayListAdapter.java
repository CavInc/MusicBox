package cav.musicbox.ui.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.storage.models.MainTrackModel;

public class UserPlayListAdapter extends RecyclerView.Adapter<UserPlayListAdapter.PlayListHoler>{
    private Context mContext;
    private List<MainTrackModel> mData;

    public UserPlayListAdapter(List<MainTrackModel> data){
        mData = data;
    }


    @Override
    public UserPlayListAdapter.PlayListHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item,parent,false);
        return new PlayListHoler(contentView);
    }

    @Override
    public int getItemCount() {
        if (mData!= null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(UserPlayListAdapter.PlayListHoler holder, int position) {
        MainTrackModel data = mData.get(position);
        holder.mTrack.setText(data.getTrack());
        holder.mArtist.setText(data.getArtist());
    }

    public static class PlayListHoler extends RecyclerView.ViewHolder{
        private TextView mArtist;
        private TextView mTrack;

        public PlayListHoler(View itemView) {
            super(itemView);
            mArtist = (TextView) itemView.findViewById(R.id.item_artist);
            mTrack = (TextView) itemView.findViewById(R.id.item_track);

        }
    }
}
