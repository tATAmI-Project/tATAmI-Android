/*******************************************************************************
 * Copyright (C) 2015 Andrei Olaru, Marius-Tudor Benea, Nguyen Thi Thuy Nga, Amal El Fallah Seghrouchni, Cedric Herpson.
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

import com.tatami.tatami_android.core.util.platformUtils.PlatformUtils;

import java.io.Serializable;
import java.util.Collection;

import net.xqhs.util.config.Config;
import net.xqhs.util.logging.Logger;

/**
 * The class holds a configuration for the agent's GUI, which is independent of the platform the GUI will run on.
 * <p>
 * Beside parameters such as the window's name, type, etc, the class handles the creation of the class name for the
 * actual GUI -- deciding, based on platform and other indicators, what class extending {@link AgentGui} to load.
 * <p>
 * While the class contains GUI settings that contain the term 'window' (name and type), which is inappropriate for some
 * platforms (e.g. Android), those settings are of general value and can be used on other platforms as well, with
 * similar purposes.
 * 
 * @author Andrei Olaru
 */
public class AgentGuiConfig extends Config implements Serializable
{
	/**
	 * The class UID.
	 */
	private static final long	serialVersionUID	= -5605500962863357519L;
	
	/**
	 * The default type of the window.
	 */
	public static final String	DEFAULT_WINDOW_TYPE	= "agent";
	/**
	 * The name (to be part of the GUI class name) for the default GUI.
	 */
	private static final String	DEFAULT_AGENT_GUI	= "DefaultAgentGui";
	/**
	 * The root package for project classes.
	 */
	// FIXME should be elsewhere
	private static final String	ROOT_PACKAGE		= "tatami";
	/**
	 * The default package path for GUI classes (to be under the root package).
	 */
	private static final String	DEFAULT_GUI_PATH	= "agent.visualization";
	
	/**
	 * If set to non <code>null</code>, specifies the fully qualified name for the class to load for the GUI, overriding
	 * any decision in <code>setGuiClass()</code>.
	 */
	protected String			overrideClassName;
	/**
	 * The fully qualified name of the class to load as GUI.
	 */
	protected String			guiClassName;
	/**
	 * The name of the window containing the GUI.
	 */
	protected String			windowName;
	/**
	 * The type of the window containing the GUI.
	 */
	protected String			windowType;
	
	@Override
	public AgentGuiConfig makeDefaults()
	{
		super.makeDefaults();
		setClassNameOverride(null);
		setWindowName(null);
		setWindowType(DEFAULT_WINDOW_TYPE);
		setGuiClass(null, null, null);
		return this;
	}
	
	/**
	 * Sets the name of the window containing the agent GUI.
	 * 
	 * @param name
	 *            - the name of the window.
	 * @return the instance itself, for chained calls.
	 */
	public AgentGuiConfig setWindowName(String name)
	{
		windowName = name;
		return this;
	}
	
	/**
	 * Sets the type of the window containing the agent GUI. This may be used by a window layout class.
	 * 
	 * @param type
	 *            - the type of the window.
	 * @return the instance itself, for chained calls.
	 */
	public AgentGuiConfig setWindowType(String type)
	{
		windowType = type;
		return this;
	}
	
	/**
	 * Sets the GUI class name override. This name will be used to load the GUI, without further intervention.
	 * 
	 * @param className
	 *            - the fully qualified name of the class. It is expected that the class extends {@link AgentGui}.
	 * @return the instance itself, for chained calls.
	 */
	public AgentGuiConfig setClassNameOverride(String className)
	{
		overrideClassName = className;
		return this;
	}
	
	/**
	 * Sets the class name for the GUI. The method further adjust the full name of the class depending on the platform.
	 * <p>
	 * The usual scenarios for a GUI class are:
	 * <ul>
	 * <li>the decision mechanism is completely overridden, and the class name is set through
	 * <code>setClassNameOverride()</code>. FIXME: not completely.
	 * <li>the given className is null, and the default GUI for the platform will be considered.
	 * <li>a fully qualified name for the class is given, which will be allowed only if it is a class in the core
	 * implementation (i.e. the package of the class contains the default GUI path.
	 * <li>a name is given, that can be found in the collection of packages in the second parameter, under the name of
	 * the current platform. E.g. for the name MyGui and packages containing my_package, on an Android platform, the
	 * searched class will be my_package.android.MyGui
	 * </ul>
	 * 
	 * @param className
	 *            - the class for the GUI.
	 * @param packages
	 *            - a {@link Collection} of packages into which to look for the class.
	 * @param log
	 *            - log to use for logging messages.
	 * @return the instance itself, for chained calls.
	 */
	public AgentGuiConfig setGuiClass(String className, Collection<String> packages, Logger log)
	{
		PlatformUtils.Platform platform = PlatformUtils.getPlatform();
		// the default path for GUI classes is in ROOT.lowercase_platform.DEFAULT_GUI_PATH
		// e.g. tatami.pc.agent.visualization
		String defaultGuiPath = ROOT_PACKAGE + "." + platform.toString().toLowerCase() + "." + DEFAULT_GUI_PATH;
		guiClassName = null;
		// if overridden, start from the value of the override; otherwise, from the method argument.
		String cName = (overrideClassName != null) ? overrideClassName : className;
		if(cName == null) // no class name given -> load default. e.g. PCDefaultAgentGui.
		{
			guiClassName = defaultGuiPath + "." + platform + DEFAULT_AGENT_GUI;
			if(log != null)
				log.trace("GUI: using default class ", guiClassName);
		}
		else if(cName.indexOf(defaultGuiPath) >= 0)
		{ // if the class contains package information and is inside the default package, it is left unchanged.
			guiClassName = cName;
			if(log != null)
				log.trace("GUI: using as is class ", guiClassName);
		}
		else
		{
			for(String pack : packages)
			{ // iterate over all given packages to locate the class, as package.platform.className
				String path = null;
				try
				{
					path = pack + "." + platform + "." + cName;
					if(log != null)
						log.trace("GUI: trying: []", path);
					Class.forName(path);
					guiClassName = path;
					break;
				} catch(ClassNotFoundException e)
				{
					// do nothing; go forth
				}
			}
			// if an appropriate class has not been found, revert to the default GUI class.
			if(guiClassName == null)
			{
				guiClassName = defaultGuiPath + "." + platform + DEFAULT_AGENT_GUI;
				if(log != null)
					log.trace("GUI not found; reverted to default: [] ", guiClassName);
			}
			else if(log != null)
				log.trace("GUI found: []", guiClassName);
		}
		return this;
	}

	/**
	 * @return the windowName
	 */
	public String getWindowName()
	{
		return windowName;
	}

	/**
	 * @return the windowType
	 */
	public String getWindowType()
	{
		return windowType;
	}
	
	/**
	 * @return the computed class name for the GUI.
	 */
	public String getGuiClass()
	{
		return guiClassName;
	}
}