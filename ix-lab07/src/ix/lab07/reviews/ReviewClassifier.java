package ix.lab07.reviews;

import java.util.HashMap;
import java.util.Map;


import com.google.common.collect.Sets;

import ix.utils.DataPoint;
import ix.utils.Label;

/** Binary classifier used to label hotel reviews as 'truthful' or 'deceptive'. */
public class ReviewClassifier {

	
	Map<String, Double> wordDeceptiveOccurrences = new HashMap<String, Double>();
	int wordDeceptiveNumber = 0;
	
	Map<String, Double> wordTruthOccurrences = new HashMap<String, Double>();
	int wordTruthNumber = 0;
	
    /**
     * Initializes an instance of classifier with the vocabulary. The vocabulary
     * contains all the words contained in any of the reviews.
     *
     * @param vocab the vocabulary
     */
    public ReviewClassifier(Iterable<String> vocab) {
    	// Laplace smoothing
        for (String v : vocab) {
			wordDeceptiveOccurrences.put(v, 1.0);
			wordTruthOccurrences.put(v, 1.0);
		}
    }


    /**
     * Updates the parameters of the classifier, using labeled examples of
     * reviews.
     *
     * @param data the training data=
     */
    public void fit(Iterable<DataPoint> data) {
        // Each element in `data` is an example that contains a review and its
        // associated label.
        // The goal is to learn the label-conditional word probabilities
        // p(w | c), by looking at the frequency of occurrence of each word.

        for(DataPoint point : data) {
        	for(String word : Sets.newHashSet(point.words())) {
        		if(point.type == Label.DECEPTIVE) {
        			incrementMap(wordDeceptiveOccurrences, word);
        			++wordDeceptiveNumber;
        		} else {
        			incrementMap(wordTruthOccurrences, word);
        			++wordTruthNumber;
        		}
        	}
        }
    }


    /**
     * Predicts the label of a review ('truthful' or 'deceptive'.)
     *
     * @param words the words contained in the review.
     * @return the predicted label of the reivew
     */
    public Label predict(Iterable<String> words) {
    	double sumDeceptive = 0.0;
    	double sumTruth = 0.0;
    	
        for(String word : words) {
        	sumDeceptive += Math.log(wordDeceptiveOccurrences.get(word) / (double) wordDeceptiveNumber);
        	sumTruth += Math.log(wordTruthOccurrences.get(word) / (double) wordTruthNumber);
        }
        
        return sumDeceptive > sumTruth ? Label.DECEPTIVE : Label.TRUTHFUL;
    }
    
    
    private void incrementMap(Map<String, Double> map, String key) {
    	double count = map.containsKey(key) ? map.get(key) : 0.0;
    	map.put(key, count + 1);
    }
}
