package cav.musicbox.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cav.musicbox.R;

public class PlayListDialog extends DialogFragment {
    private PlayListDialogListener mListener;
    private EditText mPlayTitle;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.play_list_dialog, null);

        mPlayTitle = (EditText) v.findViewById(R.id.dialog_title_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle(R.string.dialog_title_playlist)
                .setNegativeButton(R.string.dialog_canel,null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null){
                            mListener.onSetPlayList(mPlayTitle.getText().toString());
                        }
                    }
                });

        return builder.create();
    }

    public void setListener(PlayListDialogListener listener){
        mListener = listener;
    }

    public interface PlayListDialogListener {
        void onSetPlayList(String value);
    }
}
