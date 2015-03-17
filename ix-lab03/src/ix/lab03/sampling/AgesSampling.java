package ix.lab03.sampling;

import java.util.List;
import java.util.Random;

import ix.utils.SocialAPI;
import ix.utils.SocialNode;

public class AgesSampling {

    public static int N = 10000;
    public static int GRAPH = SocialAPI.AGES;

    public static void main(String[] args) {    	
    	SocialAPI api = new SocialAPI();
    	
        int i = 0;
        double ageSum = 0;
        double inverseDegreeSum = 0;
        SocialNode currentNode = api.getNode(GRAPH, SocialAPI.SEED_U);
        //Seed U: 63.644 10x more iterations: 63.75
        //Seed V: 82.581 10x more iterations: 70.42
        //Seed W: 60.773
        List<String> currentNeighbors;
        Random r = new Random();


        while (i < N) {

        	currentNeighbors = currentNode.neighbors;
        	double numNeighbours = currentNeighbors.size();

        	ageSum += currentNode.age / numNeighbours;

        	inverseDegreeSum += 1 / numNeighbours;

        	currentNode = api.getNode(GRAPH, currentNeighbors.get(r.nextInt((int)numNeighbours)));

        	if(i % 200 == 0) {
        		System.out.println(ageSum / inverseDegreeSum);
        	}

        	++i;
        }

        System.out.println(ageSum / inverseDegreeSum);
    }
}
