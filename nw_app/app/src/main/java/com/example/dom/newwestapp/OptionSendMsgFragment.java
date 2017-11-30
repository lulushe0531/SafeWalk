package com.example.dom.newwestapp;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lulu on 2017-11-28.
 */

public class OptionSendMsgFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.option_send_msg, container, false);
        getDialog().setTitle("Options");

        TextView sendMsg = (TextView) rootView.findViewById(R.id.msgTextView);
        TextView cancel = (TextView) rootView.findViewById(R.id.cancleTextView);

        sendMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                GPSHelper gps = new GPSHelper(getActivity());
                gps.getMyLocation();
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.SEND_SMS)) {
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.SEND_SMS},
                                MapsActivity.MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }
                StringBuffer smsBody = new StringBuffer();
                smsBody.append("I'm in danger! This is my location: ");
                smsBody.append("http://maps.google.com/?q=");
                smsBody.append(gps.getLatitude());
                smsBody.append(",");
                smsBody.append(gps.getLongitude());

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,smsBody.toString());
                startActivity(i);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;

    }

}
