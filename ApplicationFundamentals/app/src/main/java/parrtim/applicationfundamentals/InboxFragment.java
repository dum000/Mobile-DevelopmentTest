package parrtim.applicationfundamentals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

public class InboxFragment extends ListFragment implements AdapterView.OnItemClickListener {

    InboxListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new InboxListAdapter(getContext(), SMSUtil.getSMSInbox(getContext()));
        return inflater.inflate(R.layout.fragment_inbox_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    public void Filter(String filterText)
    {
        adapter.getFilter().filter(filterText);
    }
}

/*
0 = "body"
1 = "person"
2 = "text_only"
3 = "sub"
4 = "subject"
5 = "retr_st"
6 = "type"
7 = "date"
8 = "ct_cls"
9 = "sub_cs"
10 = "_id"
11 = "read"
12 = "ct_l"
13 = "tr_id"
14 = "st"
15 = "msg_box"
16 = "thread_id"
17 = "reply_path_present"
18 = "m_cls"
19 = "read_status"
20 = "ct_t"
21 = "status"
22 = "retr_txt_cs"
23 = "d_rpt"
24 = "error_code"
25 = "m_id"
26 = "date_sent"
27 = "m_type"
28 = "v"
29 = "exp"
30 = "pri"
31 = "service_center"
32 = "address"
33 = "rr"
34 = "rpt_a"
35 = "resp_txt"
36 = "locked"
37 = "resp_st"
38 = "m_size"
*/