package parrtim.applicationfundamentals.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import parrtim.applicationfundamentals.R;


public class SMSMessageFragment extends Fragment implements View.OnClickListener {


    public SMSMessageFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smsmessage, container, false);

        v.findViewById(R.id.sendMessageButton).setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        TextView textView = (TextView) getView().findViewById(R.id.sendMessageText);
        switch (v.getId())
        {
            case R.id.sendMessageButton: Toast.makeText(getActivity(), "Sent Text: " + textView.getText(), Toast.LENGTH_SHORT).show(); break;
        }
    }
}
