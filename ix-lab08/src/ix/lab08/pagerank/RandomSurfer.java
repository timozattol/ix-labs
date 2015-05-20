package ix.lab08.pagerank;

import java.util.List;
import java.util.Random;

import utils.Graph;
import utils.PageRank;


/**
 * Intuitive implementation of PageRank. The analogy is that of a surfer that wanders
 * through the Web by clicking on random links.
 */
public class RandomSurfer implements PageRankAlgorithm {

    private static final int NB_ITERATIONS = 1000000;

    @Override
    public PageRank compute(Graph graph) {
        int nbNodes = graph.size();
        double[] probabilities = new double[nbNodes];
        Random r = new Random();

        int currentNode = r.nextInt(nbNodes);
        List<Integer> currentNeighbors;

        for (int i = 0; i < NB_ITERATIONS; i++) {
			currentNeighbors = graph.neighbors(currentNode);
			
			if (currentNeighbors.size() == 0 || r.nextDouble() < DAMPING_FACTOR) {
				// Jump to any node at random
				currentNode = r.nextInt(nbNodes);
			} else {
				// Jump to a neighboring node at random
				currentNode = currentNeighbors.get(r.nextInt(currentNeighbors.size()));
			}

			

			++probabilities[currentNode];
		}

        for (int i = 0; i < nbNodes; i++) {
			probabilities[i] /= NB_ITERATIONS;
		}

        return new PageRank(graph, probabilities);
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(String.format(
                    "Usage: %s <graph>", RandomSurfer.class.getName()));
            return;
        }
        Graph graph = Graph.fromFile(args[0]);
        PageRank pr = new RandomSurfer().compute(graph);
        pr.printAll();
    }

}
