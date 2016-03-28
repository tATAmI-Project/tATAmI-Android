/*******************************************************************************
 * Copyright (C) 2013 Andrei Olaru, Marius-Tudor Benea, Nguyen Thi Thuy Nga, Amal El Fallah Seghrouchni, Cedric Herpson.
 * 
 * This file is part of tATAmI-PC.
 * 
 * tATAmI-PC is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * tATAmI-PC is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with tATAmI-PC.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.tatami.tatami_android.core.agent.visualisation;

import com.tatami.tatami_android.core.agent.io.AgentActiveIO;

import java.util.Vector;


/**
 * This class models the GUI of an agent (and a GUI in general) so as to make it platform-independent, based on
 * {@link AgentActiveIO}.
 * <p>
 * It models the GUI as a set of GUI components (I/O ports) that can perform input and/or output. These components are
 * different from agent components. GUI components are logical units that can contain one or more controls and that can
 * correspond to non-disjoint sets of controls.
 * <p>
 * There are three types of functionalities that can be associated to a GUI component:
 * <ul>
 * <li>output -- the component is able to change appearance according to set of {@link Object} instances;
 * <li>'passive' input -- the component is able to store information entered by the user (or collected from the user),
 * that can be retrieved as a set of {@link Object} instances.
 * <li>'active' input -- the component is able to notify a {@link tatami.core.agent.io.AgentActiveIO.InputListener}
 * instance about the user's activity, also transmitting a set of {@link Object} instances along with the notification.
 * </ul>
 * Basic examples of the functionalities are as follows: output in a text field; get passive input from a text field;
 * receive active input / notifications when a button is pressed.
 * <p>
 * GUI components are identified by {@link String} names. It is strongly recommended that the names are stored as
 * constants in enumerations and are treated as lower case. For instance, default components exists in a GUI, which get
 * their names from the {@link DefaultComponent} enumeration.
 * 
 * @author Andrei Olaru
 */
public interface AgentGui extends AgentActiveIO
{
	/**
	 * Default components of a GUI.
	 * 
	 * @author Andrei Olaru
	 */
	public enum DefaultComponent {
		/**
		 * The name of the agent.
		 */
		AGENT_NAME,
		
		/**
		 * The log of the agent (as an output component).
		 */
		AGENT_LOG
	}
	
	/**
	 * The interface enables the GUI to perform long / background tasks.
	 * <p>
	 * This also must be used by Java Swing implementations to take control off from the EDT.
	 * 
	 * @see <a href="http://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html">Swing Concurrency
	 *      Tutorial</a>
	 * 		
	 * @author Andrei Olaru
	 */
	public interface AgentGuiBackgroundTask
	{
		/**
		 * Executes the task.
		 * 
		 * @param arg
		 *            - argument for the task.
		 * @param resultListener
		 *            - {@link ResultNotificationListener} instance to receive the result of the task. This may be
		 *            <code>null</code> if the result is not needed elsewhere.
		 */
		public void execute(Object arg, ResultNotificationListener resultListener);
	}
	
	/**
	 * The interface functions together with {@link AgentGuiBackgroundTask} to receive the result of the task performed
	 * in background.
	 * 
	 * @author Andrei Olaru
	 */
	public interface ResultNotificationListener
	{
		/**
		 * The method is invoked when the background task completes, with the result of the task.
		 * 
		 * @param result
		 *            - the result of the task.
		 */
		public void receiveResult(Object result);
	}
	
	/**
	 * Sends information to a component meant to convey that information to the GUI.
	 * 
	 * @param componentName
	 *            - the name of the component.
	 * @param arguments
	 *            - the information to transmit.
	 */
	@Override
	public void doOutput(String componentName, Vector<Object> arguments);
	
	/**
	 * Retrieves information from a component. The information is expected to come from the GUI.
	 * 
	 * @param componentName
	 *            - the name of the component.
	 * @return the received information.
	 */
	@Override
	public Vector<Object> getInput(String componentName);
	
	/**
	 * Instructs the GUI to unload, effectively closing the GUI.
	 */
	public void close();
	
	/**
	 * The method executes a task in the background of the GUI, as specified by the implementation. This is useful, for
	 * instance, for taking tasks off the Swing EDT.(DemointeractivGUI.setTitle(getAgentName());
	 * 
	 * <p>
	 * Implementations are expected to start another thread for executing the work.
	 * 
	 * @see AgentGuiBackgroundTask
	 * 		
	 * @param agentGuiBackgroundTask
	 *            - the task to perform.
	 * @param argument
	 *            - the argument to pass to the task.
	 * @param resultListener
	 *            - listener for the result of the task.
	 */
	public void background(AgentGuiBackgroundTask agentGuiBackgroundTask, Object argument,
						   ResultNotificationListener resultListener);
			
}