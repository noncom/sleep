/* 
 * Copyright (C) 2002-2012 Raphael Mudge (rsmudge@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package sleep.interfaces;
 
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;

/**
 * <p>A variable bridge is a container for storing scalars.  A variable bridge is nothing more than a container.  It is 
 * possible to use a new variable container to alter how scalars are stored and accessed.  All scalars, scalar arrays, and 
 * scalar hashes are stored using this system. </p>
 *  
 * <p>A Variable bridge is installed by creating a new script variable manager with the new variable bridge.   The variable 
 * manager is then installed into a given script.</p>
 * 
 * <pre>
 * ScriptVariables variableManager = new ScriptVariable(new MyVariable());
 * script.setScriptVariables(variableManager);
 * </pre>
 * 
 * <p>Sleep scripts can share variables by using the same instance of ScriptVariables.  A Variable bridge can be used to 
 * create built in variables.  Every time a certain scalar is accessed the bridge might call a method and return the value 
 * of the method as the value of the accessed scalar.</p>
 * 
 */
public interface Variable extends java.io.Serializable
{
    /** true if a scalar named key exists in this variable environment */
    public boolean    scalarExists(String key); 

    /** returns the specified scalar, if scalarExists says it is in the environment, this method has to return a scalar */
    public Scalar     getScalar(String key);

    /** put a scalar into this variable environment */
    public Scalar     putScalar(String key, Scalar value);
 
    /** remove a scalar from this variable environment */
    public void       removeScalar(String key);

    /** returns which variable environment is used to temporarily store local variables.  */
    public Variable createLocalVariableContainer();

    /** returns which variable environment is used to store non-global / non-local variables.  this is also used to create the global scope for a forked script environment. */
    public Variable createInternalVariableContainer();
}
