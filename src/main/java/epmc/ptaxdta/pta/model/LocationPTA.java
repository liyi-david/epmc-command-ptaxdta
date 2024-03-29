/**
 * 
 */
package epmc.ptaxdta.pta.model;

import java.util.ArrayList;

import epmc.error.EPMCException;
import epmc.expression.Expression;

/**
 * @author liyi
 *
 */
public interface LocationPTA extends ElementPTA{
	
	/**
	 * returns the name of the location
	 * forms of names depend on the specific implementation
	 * <hr />
	 * <ul>
	 *   <li> it's <b>important</b> to notice that names are not required to be distinct</li>
	 *   <li> you may want to use Object.hashCode() instead
	 * </ul>
	 * @return
	 */
	public String getName();
//	public boolean equals(LocationPTA l);
	
	public ArrayList<String> getVariables();
	public ArrayList<Integer> getScopeSizes();
	public ArrayList<Integer> getSerialized();
	
	public Expression getInvariant() throws EPMCException;
	public Expression getCurrLoc();

}
