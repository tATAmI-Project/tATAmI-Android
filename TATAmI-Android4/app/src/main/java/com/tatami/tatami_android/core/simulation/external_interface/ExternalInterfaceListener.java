package com.tatami.tatami_android.core.simulation.external_interface;

/**
 * Created by yonutix on 15/05/2016.
 */
public interface ExternalInterfaceListener {


    interface AgentRelated{
        public void onAgentAdded(ExtAgentInfo agentInfo);
    }
}
