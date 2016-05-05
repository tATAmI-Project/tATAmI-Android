package android.ui;

import java.util.Vector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tatami.tatami_android.R;

/**
 * Created by uidk9631 on 25.04.2016.
 */
public class AgentsManagerListAdaptor extends ArrayAdapter<String> {

    Activity mContext;
    private final String[] appNames;

    Vector<AgentEntryInfo> mEntries;

    public AgentsManagerListAdaptor(Activity context, String[] web, Vector<AgentEntryInfo> entries) {
        super(context, R.layout.agents_manager_list_item, web);
        mContext = context;
        appNames = web;
        mEntries = entries;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.agents_manager_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.agents_manager_item_agent_name);
        txtTitle.setText(mEntries.get(position).getAgentName());

        return rowView;
    }

}
