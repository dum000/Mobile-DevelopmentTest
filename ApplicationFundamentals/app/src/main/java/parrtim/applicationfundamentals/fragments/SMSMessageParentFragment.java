package parrtim.applicationfundamentals.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import parrtim.applicationfundamentals.R;

public class SMSMessageParentFragment extends Fragment {

    public SMSMessageParentFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_smsmessage_parent, container, false);
    }

}
