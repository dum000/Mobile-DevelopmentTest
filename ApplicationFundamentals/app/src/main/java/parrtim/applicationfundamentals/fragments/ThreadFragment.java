package parrtim.applicationfundamentals.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.helper.SMSUtil;
import parrtim.applicationfundamentals.adapters.ThreadListAdapter;

public class ThreadFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ThreadListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ThreadListAdapter(getContext(), SMSUtil.getSMSThreads(getContext()));
        return inflater.inflate(R.layout.fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("address", adapter.getItem(position).Sender);

        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame1, fragment)
                .addToBackStack(null)
                .commit();
    }
}
