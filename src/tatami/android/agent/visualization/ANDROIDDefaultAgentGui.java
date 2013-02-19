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
package tatami.android.agent.visualization;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tatami.android.main.activity.TatamiGUIUpdater;
import tatami.core.interfaces.AgentGui;

public class ANDROIDDefaultAgentGui implements AgentGui {
	
	/**
	 * Contains the content of each Android GUI component and a reference to the Android View representing it. 
	 * @author beneamt
	 *
	 */
	public class AndroidAgentGuiComponent {
		public AndroidAgentGuiComponent() {
			
		}
		
		public AndroidAgentGuiComponent(Vector<Object> content, View view) {
			this.content = content;
			this.view = view;
		}
		
		private Vector<Object> content	= null;
		/**
		 * null when the agent is not selected in the spinner.
		 */
		private View view				= null;
		public Vector<Object> getContent() {
			return content;
		}
		public void setContent(Vector<Object> content) {
			this.content = content;
		}
		public View getView() {
			return view;
		}
		public void setView(View view) {
			this.view = view;
		}
	}

	protected AgentGuiConfig				config					= null;
	
	protected TatamiGUIUpdater				main_window_updater		= null;

	protected Map<String, AndroidAgentGuiComponent>	components		= null;
	protected Map<String, InputListener>	inputConnections		= null;

	protected LinearLayout agentsGuiSlot							= null;
	
	//protected VisualizableAgent				va						= null;
	
/*	private Button sClaimButton;
	private TextView agentName;
	private TextView logScreen;*/

	

/*	public AndroidDefaultAgentGui(AgentGuiConfig configuration, VisualizableAgent va)
	{
		config = configuration;
		
		main_window_updater = new SClaimGUIUpdater(va);
		main_window_updater.arrivedOnDevice(va);
		this.va = va;
	}
*/	
	
    public ANDROIDDefaultAgentGui(AgentGuiConfig configuration) {
		
    	//System.out.println("default gui");
    	
        config = configuration;
		//this.va = ag;
		
		components = new Hashtable<String, AndroidAgentGuiComponent>();
		inputConnections = new HashMap<String, AgentGui.InputListener>();

		main_window_updater = new TatamiGUIUpdater(config.windowName, this);
		main_window_updater.arrivedOnDevice();
		
		
		Vector<Object> agentLogContent = new Vector<Object>();
		agentLogContent.add(new String());
		AndroidAgentGuiComponent agentLog = new AndroidAgentGuiComponent( agentLogContent, null);
		components.put(DefaultComponent.AGENT_LOG.toString(), agentLog);
		
		Activity mainActivity = main_window_updater.getAct();
		int agentsGuiSlotID = mainActivity.getResources().getIdentifier("agentsGuiSlot", "id", "tatami.android");
		agentsGuiSlot = (LinearLayout) mainActivity.findViewById(agentsGuiSlotID);
		

		
/*		agent_window_updater = new DefaultAgentGUIUpdater(this);
		
		setContentView(R.layout.default_agent_layout);
		
		agentName = (TextView) findViewById(R.id.agentName);
		agentName.setText(va.getLocalName());
		
		sClaimButton = (Button) findViewById(R.id.SClaim);
		sClaimButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), TatamiGUI.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				startActivity(intent);
			}
		});

		logScreen = (TextView) findViewById(R.id.agentsLogScreen);*/
	}

    @Override
	public void doOutput(String componentName, Vector<Object> arguments) {
		if(!components.containsKey(componentName))
		{
			System.err.println("component [" + componentName + "] not found."); // FIXME: get a log from somewhere
			return;
		}
		
		AndroidAgentGuiComponent component = components.get(componentName);
		
		component.setContent(arguments);
		main_window_updater.updateAgentGuiComponent(component);
	}

	@Override
	public Vector<Object> getinput(String componentName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connectInput(String componentName, InputListener listener) {
		if(!components.containsKey(componentName))
		{
			System.err.println("component [" + componentName + "] not found."); // FIXME: get a log from somewhere
			return;
		}
		inputConnections.put(componentName, listener);
	}

	@Override
	public void close() {
		main_window_updater.goneFromDevice();
	}

	/**
	 * Paints the GUI of the agent under the spinner in the "Agents" tab, when the name of the agent is selected.
	 */
	public void paint() {
		agentsGuiSlot.removeAllViews();
		
		AndroidAgentGuiComponent component = components.get(DefaultComponent.AGENT_LOG.toString());

		TextView myLog = new TextView(main_window_updater.getAct().getApplicationContext());
		//myLog.setText(config.windowName);
		//myLog.setText((String) component.getContent().get(0));
		myLog.setPadding(25, 10, 25, 10);
		myLog.setTextColor(0xFF000000);
		myLog.setHorizontallyScrolling(false);
		agentsGuiSlot.addView(myLog);
		component.setView(myLog);
		
		doOutput(DefaultComponent.AGENT_LOG.toString(), component.getContent());
	}
	
	/**
	 * Erases the GUI of the agent under the spinner in the "Agents" tab, when the name of other agent is selected.
	 */
	public void erase() {
		
		// To be repeted for each component of the gui of the agent:
		AndroidAgentGuiComponent component = components.get(DefaultComponent.AGENT_LOG.toString());
		View currentView = component.getView();
		if(currentView != null) {
			component.setView(null);
			agentsGuiSlot.removeView(currentView);
		}
	}
}
