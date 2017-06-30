package cav.musicbox.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.ui.adapters.UserPlayListAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private List<MainTrackModel> mTrackData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrackData = new ArrayList<>();

        // debug data
        mTrackData.add(new MainTrackModel("Серьга","Свет в оконце"));
        mTrackData.add(new MainTrackModel("Канлер ГИ","Романд кардинала"));
        mTrackData.add(new MainTrackModel("Modern Talking","Brother Lui"));

        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.track_list);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        UserPlayListAdapter adapter = new UserPlayListAdapter(mTrackData);
        mRecyclerView.setAdapter(adapter);
    }
}
