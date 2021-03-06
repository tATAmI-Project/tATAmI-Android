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
package tatami.core.util;

public class ContentHolder<T>
{
	T content;
	
	public ContentHolder(T _content)
	{
		content = _content;
	}
	
	public T get()
	{
		return content;
	}
	
	public ContentHolder<T> set(T _content)
	{
		content = _content;
		return this;
	}
	
	@Override
	public String toString()
	{
		return content.toString();
	}
}
