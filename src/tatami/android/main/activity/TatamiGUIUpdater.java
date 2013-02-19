/*******************************************************************************
 * Copyright (C) 2013 Andrei Olaru, Marius-Tudor Benea, Nguyen Thi Thuy Nga, Amal El Fallah Seghrouchni, Cedric Herpson.
 * 
 * This file is part of tATAmI-Android.
 * 
 * tATAmI-Android is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * tATAmI-Android is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with tATAmI-Android.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package tatami.android.main.activity;

import tatami.android.agent.visualization.ANDROIDDefaultAgentGui.AndroidAgentGuiComponent;
import tatami.core.agent.visualization.VisualizableAgent;
import tatami.core.interfaces.AgentGui;
import android.app.Activity;

/**
 * Class used in order to update the main GUI of S-CLAIM.
 *
 */
public class TatamiGUIUpdater{
	
	private static TatamiGUI act;	
	private String agentName;
	private AgentGui agentGui;
	
	public TatamiGUIUpdater(TatamiGUI baseActivity) {
		
		if(baseActivity!=null)
		{
			act = baseActivity;
		}
		
	}
	
	public TatamiGUIUpdater(String agentName, AgentGui agentGui) {
		this.agentName = agentName;
		this.agentGui = agentGui;
		//arrivedOnDevice();
	}
	
	public static TatamiGUI getAct() {
		return act;
	}

	public void updateLog(String logMessage) {
		LogUpdater up = new LogUpdater(act,logMessage);
		act.handler.post(up);
	}
	
	/*public void updateAgentsLog(String agentName, String str) {
		AgentsLogUpdater up = new AgentsLogUpdater(act,agentName,str);
		act.handler.post(up);
	}*/
	
	/**
	 * Updates the view of the specified GUI component according to the its content.
	 * @param agentName - name of the agent 
	 * @param str
	 */
	public void updateAgentGuiComponent(AndroidAgentGuiComponent component) {
		AgentGuiComponentUpdater up = new AgentGuiComponentUpdater(act, component);
		act.handler.post(up);
	}

	
    /**
     * adds the specified agent to the GUI
     * @param ag - agent
     */
    public void arrivedOnDevice()
    {
    	updateLog("Agent "+agentName+" arrived on the device.");
//    	updateAgentsLog(ag.getLocalName()," Glad to be on the device!");
    	
    	addAgentToList(agentName, agentGui);
    }
    
    /**
     * removes te specified agent from the GUI
     * @param ag - agent
     */
    public void goneFromDevice()
    {
    	updateLog("Agent "+agentName+" left the device.");
    	
    	removeAgentFromList(agentName);
    }

    private void addAgentToList(String agentName/*,VisualizableAgent agent*/, AgentGui agentGUI){
    	AgentsListUpdater up = new AgentsListUpdater(act, agentName/*, agent*/, agentGUI);
    	act.handler.post(up);
    }
    
    private void removeAgentFromList(String agentName){
    	AgentsListUpdaterRemove up = new AgentsListUpdaterRemove(act, agentName);
    	act.handler.post(up);
    }
    
    //methods to be used by the Logger of an agent, which are similar to the TextArea equivalents:
    
    /*public void setText(String text)
    {
    	AgentsLogUpdaterForAgent up = new AgentsLogUpdaterForAgent(act, agentName, text);
    	act.handler.post(up);
    }*/
    
    //runnable classes for the activity's handler
    
	private class LogUpdater implements Runnable {
		
		private TatamiGUI claim2Act;
		private String message;
		
		
		public LogUpdater(TatamiGUI sm, String logMessage) { 
			claim2Act = sm;
			message = logMessage;
			
		}
		
		public void run() {
			claim2Act.appendToLog(message);
		}
	}

/*	private class AgentsLogUpdater implements Runnable {
		
		private TatamiGUI claim2Act;
		private String message;
		
		
		public AgentsLogUpdater(TatamiGUI sm, String agentName, String log) { 
			claim2Act = sm;
			message = agentName+" said: "+log;
			
		}
		
		public void run() {
			//claim2Act.appendToAgentsLog(message);
		}
	}*/

private class AgentGuiComponentUpdater implements Runnable {
	
	private TatamiGUI mainActivity;
	private AndroidAgentGuiComponent component;
	
	
	public AgentGuiComponentUpdater(TatamiGUI sm, AndroidAgentGuiComponent component) { 
		mainActivity = sm;
		this.component = component;
		
	}
	
	public void run() {
		mainActivity.updateAgentGuiComponent(component);
	}
}

	private class AgentsListUpdater implements Runnable {
		
		private TatamiGUI tatamiActivity;
		private String agentName;
//		private VisualizableAgent agent;
		private AgentGui agentGUI;
		
		
		public AgentsListUpdater(TatamiGUI sm, String agentName/*, VisualizableAgent agent*/, AgentGui agentGUI) { 
			tatamiActivity = sm;
			this.agentName = agentName;
			//this.agent = agent;
			this.agentGUI = agentGUI;
		}
		
		public void run() {
			tatamiActivity.addAgentToList(agentName/*, agent*/, agentGUI);
		}
	}

	private class AgentsListUpdaterRemove implements Runnable {
		
		private TatamiGUI mainActivity;
		private String agentName;
		
		
		public AgentsListUpdaterRemove(TatamiGUI sm, String agentName) { 
			mainActivity = sm;
			this.agentName = agentName;
			
		}
		
		public void run() {
			mainActivity.removeAgentFromList(agentName);
		}
	}

/*	private class AgentsLogUpdaterForAgent implements Runnable {
		
		private TatamiGUI claim2Act;
		private String agentName;
		private String text;
		
		
		public AgentsLogUpdaterForAgent(TatamiGUI sm, String agentName, String text) { 
			claim2Act = sm;
			this.agentName = agentName;
			this.text = text;
		}
		
		public void run() {
			//claim2Act.updateAgentsLog(agentName, text);
		}
	}*/
}
	
