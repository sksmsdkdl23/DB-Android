package ca.bcit.ass3.choi_lowenstein;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Phili on 11/9/2017.
 */

public class EventDetailAdapter extends ArrayAdapter<EventDetail> {
    private Context mContext;

    public EventDetailAdapter(Context context, ArrayList<EventDetail> details) {
        super(context, 0, details);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final Activity activity = (Activity) mContext;

        EventDetail detail = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.detail_list_row_layout, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.item_Name);
        TextView itemUnit = convertView.findViewById(R.id.item_Unit);
        TextView itemQuantity = convertView.findViewById(R.id.item_Quantity);

        itemName.setText(detail.get_itemName());
        itemUnit.setText(detail.get_itemUnit());
        itemQuantity.setText(detail.get_itemQuantity());

        return convertView;
    }
}
