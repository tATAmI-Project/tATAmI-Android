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
package agent_packages.example.chat.ANDROID;

import java.util.Iterator;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Path.FillType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import tatami.android.agent.visualization.ANDROIDDefaultAgentGui;
import tatami.android.agent.visualization.ANDROIDDefaultAgentGui.AndroidAgentGuiComponent;
import tatami.core.interfaces.AgentGui.DefaultComponent;
import tatami.core.interfaces.AgentGui.InputListener;

public class ChatAgentGui extends ANDROIDDefaultAgentGui {

	enum ChatComponents {
		SEND, CHATTEXT, CHATLOG, MESSAGEINPUT
	}

	InputListener	inputListener	= null;
	
	public ChatAgentGui(AgentGuiConfig configuration) {
		super(configuration);
		
		//Adding the CHATTEXT component:
		Vector<Object> chatTextContent = new Vector<Object>();
		chatTextContent.add(new String());
		AndroidAgentGuiComponent chatText = new AndroidAgentGuiComponent( chatTextContent, null);
		components.put(ChatComponents.CHATTEXT.toString(), chatText);

		//Adding the MESSAGEINPUT component:
		Vector<Object> messageInputContent = new Vector<Object>();
		messageInputContent.add(new String());
		AndroidAgentGuiComponent messageInput = new AndroidAgentGuiComponent( messageInputContent, null);
		components.put(ChatComponents.MESSAGEINPUT.toString(), messageInput);
		
		//Adding the CHATLOG component:
		Vector<Object> chatLogContent = new Vector<Object>();
		//chatLogContent.add(new String(config.windowName+"'s chat log:\n\n"));
		AndroidAgentGuiComponent chatLog = new AndroidAgentGuiComponent( chatLogContent, null);
		components.put(ChatComponents.CHATLOG.toString(), chatLog);
		
		//Adding the SEND component:
		Vector<Object> sendContent = new Vector<Object>();
		sendContent.add(new String());
		AndroidAgentGuiComponent send = new AndroidAgentGuiComponent( sendContent, null);
		components.put(ChatComponents.SEND.toString(), send);
		
		//System.out.println("user chat gui");
	}

	/**
	 * Paints the GUI of the agent under the spinner in the "Agents" tab, when the name of the agent is selected.
	 */
	public void paint() {
		Activity mainActivity = main_window_updater.getAct();
		
		//clearing the agents GUI slot:
		agentsGuiSlot.removeAllViews();
		
		//drawing the chat screen:
		AndroidAgentGuiComponent component = components.get(ChatComponents.CHATLOG.toString());
		
		TextView chatLog = new TextView(mainActivity.getApplicationContext());
		chatLog.setPadding(25, 10, 25, 10);
		chatLog.setTextColor(0xFF000000);
		chatLog.setHorizontallyScrolling(false);
		agentsGuiSlot.addView(chatLog);
		component.setView(chatLog);
		
		doOutput(ChatComponents.CHATLOG.toString(), component.getContent());
		
		
		//drawing the chat input (CHATTEXT):
		component = components.get(ChatComponents.CHATTEXT.toString());
			
		EditText chatText = new EditText(mainActivity.getApplicationContext());
		chatText.setHorizontallyScrolling(true);
		chatText.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(23, 0, 23, 0);
		chatText.setLayoutParams(params);
		agentsGuiSlot.addView(chatText);
		component.setView(chatText);
			
		//drawing the send button:
		component = components.get(ChatComponents.SEND.toString());
			
		Button sendButton = new Button(mainActivity.getApplicationContext());
		sendButton.setText("Send");
		sendButton.setLayoutParams(params);
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(inputListener != null)
				{
					Vector<Object> args = new Vector<Object>(1);
					args.add(((EditText) components.get(ChatComponents.CHATTEXT.toString()).getView()).getText().toString());
					inputListener.receiveInput(ChatComponents.MESSAGEINPUT.toString().toLowerCase(), args);
					((TextView) components.get(ChatComponents.CHATTEXT.toString()).getView()).setText("");
				}
				else
					System.out.println("nobody to receive the input");
				// FIXME else, a log should pick up an error
			}
		});
		agentsGuiSlot.addView(sendButton);
		component.setView(sendButton);
		
		//drawing the agent log:
		component = components.get(DefaultComponent.AGENT_LOG.toString());

		TextView myLog = new TextView(mainActivity.getApplicationContext());
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
		
		Iterator it = components.values().iterator();
		
		while(it.hasNext()) {
			AndroidAgentGuiComponent component = (AndroidAgentGuiComponent) it.next();
			View currentView = component.getView();
			if(currentView != null) {
				component.setView(null);
				agentsGuiSlot.removeView(currentView);
			}
		}
	}
	
	@Override
	public void connectInput(String componentName, InputListener listener)
	{
		System.out.println("connecting input [" + componentName + "]..");
		if(componentName.equals(ChatComponents.MESSAGEINPUT.toString().toLowerCase()))
		{
			inputListener = listener;
			System.out.println("...done");
		}
		else
			super.connectInput(componentName, listener);
	}
	
	@Override
	public void doOutput(String componentName, Vector<Object> arguments)
	{
		if(componentName.compareToIgnoreCase(ChatComponents.CHATLOG.toString()) == 0)
			super.doOutput(ChatComponents.CHATLOG.toString(), arguments);	// FIXME: artificial
		else
			super.doOutput(componentName, arguments);
	}

}
