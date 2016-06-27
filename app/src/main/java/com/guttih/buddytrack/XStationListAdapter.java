package com.guttih.buddytrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guttih.buddytrack.Entity.StationViewItem;

import java.util.List;

/**
 * Created by GuðjónHólm on 26.6.2016.
 */
public class XStationListAdapter extends ArrayAdapter<StationViewItem> {

    Context context;
    int resource;
    public XStationListAdapter(Context context, int resource, List<StationViewItem> stationList) {

        super(context, resource, stationList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView =  li.inflate(R.layout.listitem, parent, false);
        }

        StationViewItem station = getItem(position);

        TextView text1 = (TextView) itemView.findViewById(R.id.tvTextItem1);
        TextView text2 = (TextView) itemView.findViewById(R.id.tvTextItem2);
        TextView text3 = (TextView) itemView.findViewById(R.id.tvTextItem3);
        ImageView image = (ImageView) itemView.findViewById(R.id.imageViewCompany) ;
        text1.setText(station.getName());
        text2.setText(station.getBensin95());

             /*    double dist = geo.distance(
                    mLocation.latitude,
                    mLocation.longitude,
                    station.getLatitude(),
                    station.getLongitude(), 'M');

            long distance = Math.round(dist);

            String strDist = Long.toString(distance);
            text3.setText(strDist);*/
        text3.setText(station.formatDistance());
        image.setImageResource(station.getIconID());

        return itemView;
    }
}
