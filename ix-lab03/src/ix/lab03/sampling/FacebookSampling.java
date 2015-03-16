package ix.lab03.sampling;

import java.util.List;
import java.util.Random;

import ix.utils.SocialAPI;
import ix.utils.SocialNode;

public class FacebookSampling {

    public static int N = 10000;
    public static int GRAPH = SocialAPI.FACEBOOK;

    public static void main(String[] args) {
        SocialAPI api = new SocialAPI();

        int i = 0;
        double ageSum = 0;
        double inverseDegreeSum = 0;
        SocialNode currentNode = api.getNode(GRAPH, SocialAPI.SEED_U);
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
