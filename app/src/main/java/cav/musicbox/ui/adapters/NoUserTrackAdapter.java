package cav.musicbox.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.storage.models.MainTrackModel;


public class NoUserTrackAdapter extends ArrayAdapter<MainTrackModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public NoUserTrackAdapter(Context context, int resource, List<MainTrackModel> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        resLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        ViewHolder holder;
        View row=convertView;
        if(row==null){
            row = mInflater.inflate(resLayout,parent,false);
            holder = new ViewHolder();
            holder.mArtist = (TextView) row.findViewById(R.id.play_list_track_artict);
            holder.mTrack = (TextView) row.findViewById(R.id.platy_list_track_track);
            row.setTag(holder);
        }else {
            holder = (ViewHolder)row.getTag();
        }

        MainTrackModel record = getItem(position);
        holder.mArtist.setText(record.getArtist());
        holder.mTrack.setText(record.getTrack());
        return row;
    }

    class ViewHolder{
        public TextView mArtist;
        public TextView mTrack;

    }
}
