package android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.tatami.tatami_android.BackgroundThread;
import com.tatami.tatami_android.R;
import com.tatami.tatami_android.core.simulation.external_interface.ExtAgentInfo;
import com.tatami.tatami_android.core.simulation.external_interface.ExternalInterfaceListener;

import java.util.ArrayList;
import java.util.Vector;

public class AgentsActivity extends AppCompatActivity implements ExternalInterfaceListener.AgentRelated{

    ListView agentsList;


    void refresh(){
        Vector<String> agentsName =
                BackgroundThread.getBoot().getSimulationManager().ext().getAgentsNames();


        String[] indexes = new String[agentsName.size()];

        Vector<AgentEntryInfo> entries = new Vector<AgentEntryInfo>();
        for(int i = 0; i < agentsName.size(); ++i){
            indexes[i] = agentsName.get(i);
            entries.add(new AgentEntryInfo(agentsName.get(i)));
        }

        AgentsManagerListAdaptor adaptor = new AgentsManagerListAdaptor(this, indexes, entries);

        agentsList.setAdapter(adaptor);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents);
        agentsList = (ListView) findViewById(R.id.agents_manager_list);
    }

    public void onAgentAdded(ExtAgentInfo agentInfo){
        refresh();
    }

}
