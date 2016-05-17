package android.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tatami.tatami_android.BackgroundThread;
import com.tatami.tatami_android.R;
import com.tatami.tatami_android.core.simulation.AgentLoader;
import com.tatami.tatami_android.core.simulation.external_interface.ExtAgentInfo;
import com.tatami.tatami_android.core.simulation.external_interface.ExternalInterfaceListener;

import java.util.ArrayList;
import java.util.Vector;

class OnAgentClickListener implements AdapterView.OnItemClickListener {

    AgentsActivity context;

    public OnAgentClickListener(AgentsActivity context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3)
    {
        Intent intent = new Intent(context, AgentLogActivity.class);
        Bundle b = new Bundle();
        b.putString("name", context.getList().get(position));
        intent.putExtras(b);

        context.startActivity(intent);

    }

}


public class AgentsActivity extends AppCompatActivity implements ExternalInterfaceListener.AgentRelated{

    ListView agentsList;
    Vector<String> agentsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents);
        agentsList = (ListView) findViewById(R.id.agents_manager_list);

        BackgroundThread.getBoot().getSimulationManager().ext().registerListener("AgentsActivity", this);
    }

    void refresh(){
        agentsName = BackgroundThread.getBoot().getSimulationManager().ext().getAgentsNames();


        String[] indexes = new String[agentsName.size()];

        Vector<AgentEntryInfo> entries = new Vector<AgentEntryInfo>();
        for(int i = 0; i < agentsName.size(); ++i){
            indexes[i] = agentsName.get(i);
            entries.add(new AgentEntryInfo(agentsName.get(i)));
        }

        AgentsManagerListAdaptor adaptor = new AgentsManagerListAdaptor(this, indexes, entries);

        agentsList.setAdapter(adaptor);
        agentsList.setOnItemClickListener(new OnAgentClickListener(this));
    }

    public Vector<String> getList(){
        return agentsName;
    }

    void onAgentsManagerRefresh(View v){
        refresh();
    }

    void onNewAgentClicked(View v){
        BackgroundThread.getBoot().getSimulationManager().createAgent();
    }

    void onAgentManagerStartAgents(View v){
        BackgroundThread.getBoot().getSimulationManager().startAgents();
    }

    public void onAgentLog(String agentName, String newLog){

    }

    public void onAgentAdded(ExtAgentInfo agentInfo){
        Log.v("smth", "Agent added called.................");
        refresh();
    }

}
