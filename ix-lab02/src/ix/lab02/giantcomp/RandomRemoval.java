package ix.lab02.giantcomp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ix.lab02.giantcomp.GiantComponent;
import ix.lab02.giantcomp.RandomRemoval;
import ix.utils.EdgeRemovalResults;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


/** Remove edges at random and observe how the size of the giant component evolves. */
public class RandomRemoval implements EdgeRemovalStrategy {

    /**
     * Removes 100 edges at random until the giant component reaches 20%
     * of its initial size.
     *
     * @param graph  the graph
     **/
    @Override
    public EdgeRemovalResults apply(SimpleGraph<String, DefaultEdge> graph) {
        EdgeRemovalResults results = new EdgeRemovalResults();
        int initSize = GiantComponent.gcSize(graph);
        results.add(initSize);
        List<DefaultEdge> listEdges = new LinkedList<DefaultEdge>(graph.edgeSet());
        Collections.shuffle(listEdges);
        
        int currentSize = initSize;

        while (currentSize > 0.2 * initSize) {
        	List<DefaultEdge> chunk = listEdges.subList(0, 100);
        	graph.removeAllEdges(chunk);
        	
        	// Deletes chunk from listEdges
        	chunk.clear();

        	currentSize = GiantComponent.gcSize(graph);
        	results.add(currentSize);
        }

        return results;
    }

    /** Run the edge removal experiment. */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <input file> <result file>",
                    RandomRemoval.class.getName()));
            System.exit(-1);
        }
        GiantComponent.edgeRemovalExperiment(new RandomRemoval(), args[0], args[1]);
        System.out.println(String.format("Done. Results stored in '%s'.", args[1]));
    }
}
