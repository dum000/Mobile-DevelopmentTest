package parrtim.fragmentexample;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesFragment extends ListFragment {

    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, Rock.ARTIST);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        showDetails(position);
    }

    boolean isDualPane()
    {
        View detailsFrame = getActivity().findViewById(R.id.details);
        return detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
    }

    void showDetails(int index)
    {
        if (isDualPane())
        {
            getListView().setItemChecked(index, true);

            DetailsFragment fragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);
            if (fragment == null)
            {
                fragment = DetailsFragment.newInstance(index);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.details, fragment).commit();
            }
            else
            {

            }
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
}
