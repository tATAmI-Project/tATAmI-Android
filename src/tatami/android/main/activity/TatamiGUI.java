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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import jade.android.AgentContainerHandler;
import jade.android.RuntimeCallback;
import jade.android.RuntimeUICallback;
import jade.android.RuntimeService;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import android.app.Activity;
import tatami.android.R;
import tatami.android.agent.visualization.ANDROIDDefaultAgentGui;
import tatami.android.agent.visualization.ANDROIDDefaultAgentGui.AndroidAgentGuiComponent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.TabSpec;

import tatami.core.interfaces.AgentGui;


public class TatamiGUI extends Activity{
	
	private Button connectButton;
	private Button disconnectButton;
	private Button showAgentButton;
	private EditText container;
	private EditText host;
	private EditText port;
	private EditText localhost;
	private EditText localport;
	
	TextView logScreen;
	//TextView agentsLogScreen;
	
	ScrollView logScroll;
	ScrollView agentsLogScroll;
	
	//fields used by the agents spinner:
	private Spinner agentsList;
	private ArrayAdapter<String> agentsAdapter;
	private Vector<String> vectorOfAgentNames;
	private HashMap<String,ANDROIDDefaultAgentGui> agentGUIs;
	
	//hashtable that stores the log of the agents
	//Hashtable<String,VisualizableAgent> agentsLog;
	
	RuntimeService rs;
	AgentContainerHandler containerHandler;
	
	/**
	 * updater of the GUI
	 */
	TatamiGUIUpdater updater;
	
	public Handler handler;
	
	private TabHost tabHost;

	//keys to index the tabs
	private final String CONNECTION_TAB_TAG="connectionTab";
	private final String LOG_TAB_TAG="logTab";
	private final String AGENTS_TAB_TAG="agentsTab";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        handler = new Handler();
        agentGUIs = new HashMap<String, ANDROIDDefaultAgentGui>();
        
        //Registering with GUIUpdater
        new TatamiGUIUpdater(this);
        
        //Set the xml layout from resource
		setContentView(R.layout.tatami_layout);
		
		
		//PREPARE TAB HOST
		tabHost = (TabHost) findViewById(R.id.tabs);
		tabHost.setup();

		//Connection Tab
		TabSpec ConnectionSpecs = tabHost.newTabSpec(CONNECTION_TAB_TAG);
		ConnectionSpecs.setIndicator(getText(R.string.connection_tab_indicator),getResources().getDrawable(R.drawable.ic_tab_connection));
		ConnectionSpecs.setContent(R.id.connectionTab);
		tabHost.addTab(ConnectionSpecs);

		//Log Tab
		TabSpec LogSpecs = tabHost.newTabSpec(LOG_TAB_TAG);
		LogSpecs.setIndicator(getText(R.string.log_tab_indicator),getResources().getDrawable(R.drawable.ic_tab_log));
		LogSpecs.setContent(R.id.logTab);
		tabHost.addTab(LogSpecs);

		//Agents Tab
		TabSpec AgentsSpecs = tabHost.newTabSpec(AGENTS_TAB_TAG);
		AgentsSpecs.setIndicator(getText(R.string.agents_tab_indicator),getResources().getDrawable(R.drawable.ic_tab_agents));
		AgentsSpecs.setContent(R.id.agentsTab);
		tabHost.addTab(AgentsSpecs);

		tabHost.setCurrentTab(0);
		
        container = (EditText) findViewById(R.id.container);
        host = (EditText) findViewById(R.id.host);
        port = (EditText) findViewById(R.id.port);
        localhost = (EditText) findViewById(R.id.local_host);
        localport = (EditText) findViewById(R.id.local_port);
	
        logScreen = (TextView) findViewById(R.id.logScreen);
        //agentsLogScreen = (TextView) findViewById(R.id.agentsLogScreen);

        logScroll = (ScrollView) findViewById(R.id.logScroll);
        agentsLogScroll = (ScrollView) findViewById(R.id.agentsLogScroll);

        //initializing the agents dropdown list
        agentsList = (Spinner) findViewById(R.id.agentsList);
        vectorOfAgentNames = new Vector<String>();
        
        agentsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new String[0]);        
		agentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		agentsList.setAdapter(agentsAdapter);
 
		//agentsLog = new Hashtable<String, VisualizableAgent>();
		
		//writeToAgentsLogUnStamped("");
        
		rs = new RuntimeService();
       
        connectButton = (Button) findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(connectButton.isEnabled())
				{
					disable(connectButton);
					disable(container);
					disable(host);
					disable(port);
					disable(localhost);
					disable(localport);
					
					Properties props = new Properties();

					props.setProperty(Profile.MAIN, Boolean.FALSE.toString());
					props.setProperty(Profile.MAIN_HOST, host.getText().toString());
					props.setProperty(Profile.MAIN_PORT, port.getText().toString());
					props.setProperty(Profile.JVM, Profile.ANDROID);
					props.setProperty(Profile.LOCAL_HOST,localhost.getText().toString());
					props.setProperty(Profile.LOCAL_PORT, localport.getText().toString());
					props.setProperty(Profile.CONTAINER_NAME,container.getText().toString());
					
					Profile profile = new ProfileImpl(props);

					rs.createAgentContainer(profile, new RuntimeUICallback<AgentContainerHandler>() {

						@Override
						public void onFailure(Throwable arg0) {
							// TODO Auto-generated method stub
							appendToLog("The application could not be connected to the server. Reason: "+arg0.toString());
						}

						@Override
						public void onSuccess(AgentContainerHandler arg0) {
							// TODO Auto-generated method stub
							
							writeToLog("The application was sucessfully connected to the server.");
							
							containerHandler = arg0;
		
//							rs.createNewAgent(arg0, "androidAgent", "testing.tudor.TestAgent", null, new RuntimeUICallback<AgentHandler>()
//									{
//
//								@Override
//								public void onFailure(Throwable arg0) {
//									// TODO Auto-generated method stub
//									String error = arg0.toString();
//									error.toString();
//								}
//								@Override
//								public void onSuccess(AgentHandler arg0) {
//									// TODO Auto-generated method stub
//
//									rs.startAgent(arg0, new RuntimeUICallback<Void>() {
//
//										@Override
//										public void onFailure(Throwable throwable) {
//											// TODO Auto-generated method stub
//
//										}
//
//										@Override
//										public void onSuccess(Void result) {
//											// TODO Auto-generated method stub
//										}
//									});
//								}
//									});
						}
					});
					
					enable(disconnectButton);
				}
			}
		});
        
        disconnectButton = (Button) findViewById(R.id.disconnect);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(disconnectButton.isEnabled())
				{
					disable(disconnectButton);
					disable(showAgentButton);
					
					rs.killAgentContainer(containerHandler, new RuntimeCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub
							
							appendToLog("The application was successfully disconnected.");
						}
						
						@Override
						public void onFailure(Throwable throwable) {
							// TODO Auto-generated method stub
							appendToLog("The application could not be correctly disconnected from the server. Reason: "+throwable.toString());
						}
					});
					
					enable(container);
					enable(host);
					enable(port);
					enable(localhost);
					enable(localport);
					enable(connectButton);
					clearAgentsList();
					//writeToAgentsLogUnStamped("");
				}
			}
		});
        
        showAgentButton = (Button) findViewById(R.id.showAgent);
        showAgentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
/*				if(showAgentButton.isEnabled()){
					Intent intent = new Intent(getApplicationContext(), agentGUIs.get(agentsList.getSelectedItem().toString()).getClass());
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					
					startActivity(intent);
				}*/
			}
		});
        
        agentsList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(parent.equals(agentsList)) {
					//writeToAgentsLogUnStamped(Log.getLoggerOutput(parent.getSelectedItem().toString(), true));
					
					String selectedAgentName = parent.getSelectedItem().toString();
					/*for (String agentName:vectorOfAgentNames)
						if(!agentName.equals(selectedAgentName))
							agentGUIs.get(agentName).erase();*/
					
					agentGUIs.get(selectedAgentName).paint();
						
				//TODO to implement the "Show Agent" button's action and to uncomment the next line
				//enable(showAgentButton);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
//				writeToAgentsLogStamped("");
				disable(showAgentButton);
			}
		});
    }
	
    public void appendToLog(String info)
    {
    	logScreen.append(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis()))
    			+": "+info+"\n");
//    	if(!logScroll.hasFocus())
//    		logScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }
    
    public void writeToLog(String info)
    {
    	logScreen.setText(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis()))
    			+": "+info+"\n");
//    	logScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }
    
/*    public void appendToAgentsLog(String info)
    {
    	agentsLogScreen.append(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis()))
    			+": "+info+"\n");
//    	if(!agentsLogScroll.hasFocus())
//    		agentsLogScroll.fullScroll(ScrollView.FOCUS_DOWN);
   }
    
    public void writeToAgentsLogStamped(String info)
    {
    	agentsLogScreen.setText(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis()))
    			+": "+info+"\n");
//    	agentsLogScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }
    
    public void writeToAgentsLogUnStamped(String info)
    {
    	agentsLogScreen.setText(info+"\n");
//    	agentsLogScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }*/
    
    private void disable(TextView widget)
    {
    	widget.setEnabled(false);
    }

    private void enable(TextView widget)
    {
    	widget.setEnabled(true);
    }
    
    public void addAgentToList(String agentName/*, VisualizableAgent agent*/, AgentGui agentGUI)
    {
		vectorOfAgentNames.add(agentName);
		//agentsLog.put(agentName, agent);
		if(agentGUI!=null)
			agentGUIs.put(agentName, (ANDROIDDefaultAgentGui) agentGUI);
		
		resetAgentsList();
    }

    public void removeAgentFromList(String agentName)
    {
		vectorOfAgentNames.remove(agentName);
		//agentsLog.remove(agentName);
		ANDROIDDefaultAgentGui agentGUI = (ANDROIDDefaultAgentGui) agentGUIs.get(agentName);
		if(agentGUI!=null)
			agentGUI.erase();
		agentGUIs.remove(agentName);
		/*if(vectorOfAgentNames.size()==0)
			writeToAgentsLogUnStamped(""); */
		
		resetAgentsList();
    }
    
    public void clearAgentsList()
    {
		for(String agName:vectorOfAgentNames)
		{
			if(agentGUIs.containsKey(agName))
			{
				ANDROIDDefaultAgentGui agentGUI = (ANDROIDDefaultAgentGui) agentGUIs.get(agName);
				if(agentGUI!=null)
				{
					agentGUI.erase();
					agentGUI = null;
				}
			}
				
		}
		agentGUIs.clear();
		vectorOfAgentNames.removeAllElements();
		//agentsLog.clear();
		//writeToAgentsLogUnStamped("");
		
		resetAgentsList();
    }
    
    private void resetAgentsList()
    {
		agentsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, (String[]) vectorOfAgentNames.toArray(new String[0]));        
		agentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		agentsList.setAdapter(agentsAdapter);
    }
    
/*    public void updateAgentsLog(String agentName, String text)
    {
    	Object selectedItem = agentsList.getSelectedItem();
    	if(selectedItem!=null)
    		if(agentsList.getSelectedItem().equals(agentName))
    			writeToAgentsLogUnStamped(text);
    }*/
    
    public void updateAgentGuiComponent(AndroidAgentGuiComponent component) {
    	Vector<Object> arguments = component.getContent();
		View componentView = component.getView();
		if( componentView != null)
			if(componentView instanceof TextView)
			{
				if(arguments.size() > 0)
				{
					TextView tv = (TextView)componentView;
					tv.setText((String)arguments.get(0));
					if(arguments.size() > 1 && ((Boolean)arguments.get(1)).booleanValue())
						tv.append(".");
				}
			}

    }
}
