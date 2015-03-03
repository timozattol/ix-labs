package ix.lab02.giantcomp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ix.utils.EdgeRemovalResults;

import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.google.common.base.Functions;
import com.google.common.collect.Ordering;


/** Remove edges in a clever way and observe how the size of the giant component evolves. */
public class TargetedRemoval implements EdgeRemovalStrategy {

    /**
     * Removes 100 edges in a clever way until the giant component reaches 20%
     * of its initial size.
     * This algorithm only computes the strength of vertices at the beginning,
     * it would be too expensive to recalculate each time we remove edges from the graph.
     *
     * @param graph  the graph
     **/
    @Override
    public EdgeRemovalResults apply(SimpleGraph<String, DefaultEdge> graph) {
        EdgeRemovalResults results = new EdgeRemovalResults();
        
        NeighborIndex<String, DefaultEdge> cachedGraph = 
        		new NeighborIndex<String, DefaultEdge>(graph);

        Map<DefaultEdge, Integer> mapEdgeStrength = new HashMap<DefaultEdge, Integer>();
        
        /* Construct the mapping of edge - strength, with the strength
        being the number of common neighbors between the vertices of the edge.
        Compute this mapping only once, and suppose it stays relevant even when
        edges are removed, for the sake of simplicity.
        */
        for (DefaultEdge edge: graph.edgeSet()) {
			String sourceVertex = graph.getEdgeSource(edge);
			String targetVertex = graph.getEdgeTarget(edge);
			mapEdgeStrength.put(edge, GiantComponent.numCommonNeighbours(
							cachedGraph, sourceVertex, targetVertex));
		}
        
        // Order incrementally regarding strength
        List<DefaultEdge> orderedEdges = TargetedRemoval.orderedEdges(mapEdgeStrength);
        
        // Compute giant component initial size
        int initSize = GiantComponent.gcSize(graph);

        int currentSize = initSize;

        results.add(initSize);
        
        int i = 0;

        while (currentSize > 0.2 * initSize) {
        	List<DefaultEdge> chunk = orderedEdges.subList(100*i, 100*i + 100);
        	graph.removeAllEdges(chunk);

        	currentSize = GiantComponent.gcSize(graph);
        	results.add(currentSize);
        	
        	++i;
		}

        return results;
    }

    /**
     * Helper function that takes a mapping between edges and strength values (of
     * any comparable type) and returns the edges sorted by increasing values.
     *
     * @param strengths  a Map between edges and their strength
     * @return  a list of edges, sorted according to their strength (weakest first.)
     */
    public static <E extends Comparable<E>> List<DefaultEdge> orderedEdges(Map<DefaultEdge, E> strengths) {
        return Ordering.natural().onResultOf(Functions.forMap(strengths))
                .immutableSortedCopy(strengths.keySet());
    }

    /** Run the edge removal experiment. */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <input file> <result file>",
                    TargetedRemoval.class.getName()));
            System.exit(-1);
        }
        GiantComponent.edgeRemovalExperiment(new TargetedRemoval(), args[0], args[1]);
        System.out.println(String.format("Done. Results stored in '%s'.", args[1]));
    }
}
