package ix.lab03.prediction;

import java.util.HashSet;
import java.util.Set;

import ix.utils.GraphUtils;
import ix.utils.PredictedEdge;

import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.google.common.collect.MinMaxPriorityQueue;


public class CoActorPrediction {

    public interface PredictionStrategy {
        public double score(SimpleGraph<String, DefaultEdge> graph, String u, String v);
    }
    
    /**
     * Computes the number of common neighbors of source and target vertices using
     * an auxiliary neighborhood cache structure.
     *
     * @param nIndex  neighborhood cache
     * @param source  source vertex
     * @param target  target vertex
     */
    public static int numCommonNeighbours(NeighborIndex<String, DefaultEdge> nIndex,
            String source, String target) {
        Set<String> intersection = new HashSet<String>(nIndex.neighborsOf(source));
        intersection.retainAll(nIndex.neighborsOf(target));
        return intersection.size();
    }


    /** Quantity reflecting the preferential attachment principle. */
    public static class PreferentialAttachment implements PredictionStrategy {
        @Override
        public double score(SimpleGraph<String, DefaultEdge> graph, String u, String v) {
            return graph.degreeOf(v);
        }
    }


    /** The number of common neighbors between u and v. */
    public static class CommonNeighbors implements PredictionStrategy {
        @Override
        public double score(SimpleGraph<String, DefaultEdge> graph, String u, String v) {
        	NeighborIndex<String, DefaultEdge> nIndex = new NeighborIndex<String, DefaultEdge>(graph);
        	
        	return numCommonNeighbours(nIndex, u, v);
        }
    }


    /** Your own scoring function. */
    public static class MyOwnScoring implements PredictionStrategy {
        @Override
        public double score(SimpleGraph<String, DefaultEdge> graph, String u, String v) {
            //TODO
            throw new RuntimeException("Not yet implemented.");
        }
    }


    public static void main(String[] args) throws Exception  {
        if (args.length != 3) {
            System.err.println(String.format("Usage: %s <strategy> <snapshot 1> <snapshot 2> ",
                    CoActorPrediction.class.getName()));
            System.exit(-1);
        }

        PredictionStrategy predictor = null;
        if ("pref-attach".equals(args[0])) {
            predictor = new PreferentialAttachment();
        } else if ("common-neighb".equals(args[0])) {
            predictor = new CommonNeighbors();
        } else if ("personal".equals(args[0])) {
            predictor = new MyOwnScoring();
        } else {
            throw new RuntimeException("strategy not one of {'pref-attach', 'common-neighb', 'personal'}");
        }

        // Creating the training and test graphs from files
        SimpleGraph<String, DefaultEdge> trainGraph = GraphUtils.load(args[1]);
        SimpleGraph<String, DefaultEdge> testGraph = GraphUtils.load(args[2]);

        // Set of new edges: edges that appear in gTest but not in gTrain.
        int nbNewEdges = testGraph.edgeSet().size() - trainGraph.edgeSet().size();

        // Predict edges by keeping those have the highest score.
        MinMaxPriorityQueue<PredictedEdge> queue =
                MinMaxPriorityQueue.maximumSize(nbNewEdges).create();
        for (String u : trainGraph.vertexSet()) {
            for (String v : trainGraph.vertexSet()) {
                if (!trainGraph.containsEdge(u, v) && u.compareTo(v) < 0) {
                    double score = predictor.score(trainGraph, u, v);
                    queue.add(new PredictedEdge(u, v, score));
                }
            }
        }

        // Count the size intersection between the predicted edges and the actual new collaborations.
        int nbCorrectPredictions = 0;
        for (PredictedEdge predicted : queue) {
            if (testGraph.containsEdge(predicted.src, predicted.dst)) {
                ++nbCorrectPredictions;
            }
        }

        int nbNodes = trainGraph.vertexSet().size();
        int nbExistingEdges = trainGraph.edgeSet().size();
        int nbPossibleEdges = (nbNodes * (nbNodes - 1)) / 2 - nbExistingEdges;

        double accuracy = nbCorrectPredictions / (double)nbNewEdges;
        double improvement = accuracy / (nbNewEdges / (double)nbPossibleEdges);

        System.out.println("Number of vertices: " + nbNodes);
        System.out.println("Number of edges in the first snapshot: " + nbExistingEdges);
        System.out.println("Number of new edges in the second snapshot: " + nbNewEdges);
        System.out.println(String.format("\nAccuracy of your prediction: %.3f%%", 100 * accuracy));
        System.out.println(String.format("Improvement over random prediction: %.2fx", improvement));
    }

}