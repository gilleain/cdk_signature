package org.openscience.cdk.structgen.deterministic;

import java.util.List;

import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Handle results from the structure enumerator as they are created. For 
 * example, to simply print out the structures an anonymous handler can easily
 * be defined like:<pre>
 * 
 * DeterministicEnumerator enumerator = new DeterministicEnumerator("C3H8");
 * SmilesGenerator smilesGenerator = new SmilesGenerator();
 * enumerator.setHandler(new EnumeratorResultHandler() {
 *      public void handle(IAtomContainer result) {
 *          System.out.println(smilesGenerator.generateSmiles(result));
 *      }
 * });
 * enumerator.generateToHandler();
 * </pre>
 * 
 * this handler will just print the smiles string straight to standard out, but
 * more complex implementations could store the structure to a database,
 * draw a diagram, and so on. Note that the 
 * {@link DefaultEnumeratorResultHandler} has the same behaviour as the simple
 * example defined above.
 * 
 * @author maclean
 *
 */
public interface IEnumeratorResultHandler {
    
    public void handle(IAtomContainer result);
    
    public List<IAtomContainer> getResults();

}
