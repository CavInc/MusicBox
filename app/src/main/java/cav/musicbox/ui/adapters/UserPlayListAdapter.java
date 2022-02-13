package cav.musicbox.ui.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.storage.models.MainTrackModel;

public class UserPlayListAdapter extends RecyclerView.Adapter<UserPlayListAdapter.PlayListHoler>{
    private Context mContext;
    private List<MainTrackModel> mData;

    private PlayListHoler.CustomClickListener mCustomClickListener;

    public UserPlayListAdapter(List<MainTrackModel> data,PlayListHoler.CustomClickListener customClickListener){
        mData = data;
        this.mCustomClickListener = customClickListener;
    }


    @Override
    public UserPlayListAdapter.PlayListHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item,parent,false);
        return new PlayListHoler(contentView,mCustomClickListener);
    }

    @Override
    public int getItemCount() {
        if (mData!= null) {
            return mData.size();
        }
        return 0;
    }

    // возвращаем данные трека
    public MainTrackModel getItem(int position) {
        return mData.get(position);
    }

    // устанавливаем данные элемента в позиции
    public void setItem(int position,MainTrackModel data) {
        mData.set(position,data);
        notifyDataSetChanged();
    }

    public void setDataItem(ArrayList<MainTrackModel> model){
        mData.clear();
        mData.addAll(model);
    }

    //https://dribbble.com/shots/2012608-Material-Design-Colors
    //http://www.androidworld.it/2014/12/22/cercate-i-colori-giusti-per-vostra-app-in-material-design-come-trovarli-264846/
    private int[] randomColor={Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GREEN,Color.MAGENTA,Color.RED,Color.rgb(50,18,21),Color.rgb(18,255,23)};

    @Override
    public void onBindViewHolder(UserPlayListAdapter.PlayListHoler holder, int position) {
        MainTrackModel data = mData.get(position);
        if (data.isVisible()) {
            holder.mTrack.setText(data.getTrack());
            holder.mArtist.setText(data.getArtist());
            holder.mCardView.setCardBackgroundColor(randomColor[(int) (Math.random() * +randomColor.length)]);
        }
    }

    public static class PlayListHoler extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String TAG = "MB_HODER";
        private TextView mArtist;
        private TextView mTrack;
        private CardView mCardView;
        private FloatingActionButton mAddButton;
        private FloatingActionButton mCloseButton;

        CustomClickListener mListener;

        public PlayListHoler(View itemView,CustomClickListener customClickListener) {
            super(itemView);
            this.mListener = customClickListener;
            mArtist = (TextView) itemView.findViewById(R.id.item_artist);
            mTrack = (TextView) itemView.findViewById(R.id.item_track);
            mCardView = (CardView) itemView.findViewById(R.id.item_track_card);
            mAddButton = (FloatingActionButton) itemView.findViewById(R.id.button_add_track);
            mCloseButton = (FloatingActionButton) itemView.findViewById(R.id.button_close_track);
            mCloseButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
            mAddButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG,"ITEM CLICKED");
            if (view.getId() == R.id.item_track_card){
                Log.d(TAG,"CARD ITEM");
                mAddButton.setVisibility(View.VISIBLE);
                mCloseButton.setVisibility(View.VISIBLE);
                return;
            }
            if (view.getId()==R.id.button_close_track){
                mAddButton.setVisibility(View.GONE);
                mCloseButton.setVisibility(View.GONE);
            }
            if (view.getId() == R.id.button_add_track){
                mAddButton.setVisibility(View.GONE);
                mCloseButton.setVisibility(View.GONE);
                if (mListener!=null) {
                    mListener.onUserItemClickListener(getAdapterPosition());
                }
            }

        }

        //https://ru.stackoverflow.com/questions/468829/recyclerview-%D0%BF%D1%80%D0%BE%D0%B1%D0%BB%D0%B5%D0%BC%D0%B0-%D0%BF%D1%80%D0%B8-%D0%BE%D0%B1%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%BA%D0%B5-%D0%BA%D0%BB%D0%B8%D0%BA%D0%B0-%D0%BD%D0%B0-item
        //http://java-help.ru/onitemclicklistener-and-onitemlongclicklistener-for-recycleview/
        //https://habrahabr.ru/post/258195/
        //https://ru.stackoverflow.com/questions/423286/%D0%9A%D0%B0%D0%BA-%D0%B4%D0%BE%D0%B1%D0%B0%D0%B2%D0%B8%D1%82%D1%8C-%D0%BE%D0%B1%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%87%D0%B8%D0%BA-%D0%BD%D0%B0%D0%B6%D0%B0%D1%82%D0%B8%D1%8F-%D0%BD%D0%B0-%D1%8D%D0%BB%D0%B5%D0%BC%D0%B5%D0%BD%D1%82-%D0%B2-recyclerview
        //
        public interface CustomClickListener {
            void onUserItemClickListener(int adapterPosition) ;
            void onUserItemOnClickListener(int adapterPosition,MainTrackModel data);
        }
    }
}
