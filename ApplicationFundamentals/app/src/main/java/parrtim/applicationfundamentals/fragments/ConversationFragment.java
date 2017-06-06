package parrtim.applicationfundamentals.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.PermissionChecker;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import parrtim.applicationfundamentals.activities.GoogleMapActivity;
import parrtim.applicationfundamentals.adapters.ConversationListAdapter;
import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.helper.SMSUtil;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class ConversationFragment extends ListFragment {

    ConversationListAdapter adapter;
    String address;
    EditText input;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            address = arguments.getString("address");
            Log.d("Address", address);

            adapter = new ConversationListAdapter(getContext(), SMSUtil.getSMSConversations(getActivity().getApplicationContext(), address));
        } else {
            adapter = new ConversationListAdapter(getContext(), SMSUtil.getSMSConversations(getActivity().getApplicationContext()));
        }

        View inflate = inflater.inflate(R.layout.fragment_conversation_list, container, false);
        input = (EditText) inflate.findViewById(R.id.sendMessageText);

        inflate.findViewById(R.id.sendMessageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage(v);
            }
        });

        inflate.findViewById(R.id.mapImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setAdapter(adapter);
        getListView().setSelection(adapter.getCount() - 1);
    }

    public void SendMessage(View view) {
        EditText input = (EditText) view.getRootView().findViewById(R.id.sendMessageText);
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(address, "9514155912", input.getText().toString(), null, null);
        Toast.makeText(getContext(), "Message: " + input.getText() + " Sent", Toast.LENGTH_SHORT).show();
    }

    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_DENIED ) {
            Intent intent = new Intent(getContext(), GoogleMapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(0);
            startActivityForResult(intent, 0);
        }
        else
        {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            SetLocation(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                SetLocation((LatLng) data.getParcelableExtra("Location"));
            }
            else
            {
                Log.d("Conversation Fragment", "FAILED TO WAIT");
            }
        }
    }

    private void SetLocation(LatLng location)
    {
        String locationMessage = "http://maps.google.com/?q=" + location.latitude +  "," + location.longitude;
        if (input.getText().length() == 0)
        {
            input.setText("Current Location: " + locationMessage);
        }
        else {
            input.setText(input.getText() + " Current Location: " + locationMessage);
        }
    }
}

