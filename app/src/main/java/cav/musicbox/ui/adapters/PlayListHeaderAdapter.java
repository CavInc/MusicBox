package cav.musicbox.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.storage.models.PlayListModel;

/**
 * Created by cav on 02.07.17.
 */
public class PlayListHeaderAdapter extends ArrayAdapter<PlayListModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public PlayListHeaderAdapter(Context context, int resource, List<PlayListModel> objects) {
        super(context, resource, objects);
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout,parent,false);
            holder = new ViewHolder();
            holder.mPlayListtitle = (TextView) row.findViewById(R.id.play_list_name);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }
        PlayListModel record = getItem(position);
        holder.mPlayListtitle.setText(record.getTitle());
        return row;
    }

    public void setData(List<PlayListModel> playListModels){
        this.clear();
        this.addAll(playListModels);
    }

    class ViewHolder {
        public TextView mPlayListtitle;

    }
}
