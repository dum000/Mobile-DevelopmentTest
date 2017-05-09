package parrtim.applicationfundamentals.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import parrtim.applicationfundamentals.adapters.ConversationListAdapter;
import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.helper.SMSUtil;

/**
 * Created by tparr on 4/8/2017.
 */

public class ConversationFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ConversationListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String address = arguments.getString("address");
            Log.d("Address", address);

            adapter = new ConversationListAdapter(getContext(), SMSUtil.getSMSConversations(getActivity().getApplicationContext(), address));
        }
        else {
            adapter = new ConversationListAdapter(getContext(), SMSUtil.getSMSConversations(getActivity().getApplicationContext()));
        }

        return inflater.inflate(R.layout.fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(this);
        getListView().setSelection(adapter.getCount() - 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}

