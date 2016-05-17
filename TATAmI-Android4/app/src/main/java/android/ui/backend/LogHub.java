package android.ui.backend;

import android.util.Log;

import com.tatami.tatami_android.core.simulation.external_interface.ExtAgentInfo;
import com.tatami.tatami_android.core.simulation.external_interface.ExternalInterfaceListener;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by uidk9631 on 17.05.2016.
 */
public class LogHub implements  ExternalInterfaceListener.AgentRelated{

    static HashMap<String, AgentLogBackend> agentsOutput = new HashMap<String, AgentLogBackend>();

    static Vector<LogHubListener> allListeners = new Vector<LogHubListener>() ;

    public LogHub(){

    }

    public synchronized  static AgentLogBackend getAgentLog(String name){
        return agentsOutput.get(name);
    }

    synchronized public void onAgentLog(String agentName, String newLog){
        agentsOutput.get(agentName).add(newLog);
        for(LogHubListener listener : allListeners){
            listener.onLogUpdated();
        }
    }

    synchronized public void onAgentAdded(ExtAgentInfo agentInfo){
        agentsOutput.put(agentInfo.getName(), new AgentLogBackend());
    }

    static synchronized public void registerListener(LogHubListener listener){
        allListeners.add(listener);
    }

    public void clear(String name){
        agentsOutput.get(name).clear();
    }
}
