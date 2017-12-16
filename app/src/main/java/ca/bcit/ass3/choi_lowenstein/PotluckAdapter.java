package ca.bcit.ass3.choi_lowenstein;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Phili on 10/31/2017.
 */

public class PotluckAdapter extends ArrayAdapter<Potluck> {
    private Context mContext;

    public PotluckAdapter(Context context, ArrayList<Potluck> potlucks) {
        super(context, 0, potlucks);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final Activity activity = (Activity) mContext;

        Potluck potluck = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_layout, parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.name);
        TextView eventDate = convertView.findViewById(R.id.date);
        TextView eventTime = convertView.findViewById(R.id.time);

        eventName.setText(potluck.getmName());
        eventDate.setText(potluck.getmDate());
        eventTime.setText(potluck.getmTime());

        return convertView;
    }
}
