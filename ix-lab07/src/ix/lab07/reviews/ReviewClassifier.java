package ix.lab07.reviews;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import ix.utils.DataPoint;
import ix.utils.Label;

/** Binary classifier used to label hotel reviews as 'truthful' or 'deceptive'. */
public class ReviewClassifier {

	
	Map<String, Integer> wordDeceptiveOccurrences = new HashMap<String, Integer>();
	int wordDeceptiveNumber = 0;
	
	Map<String, Integer> wordTruthOccurrences = new HashMap<String, Integer>();
	int wordTruthNumber = 0;
	
    /**
     * Initializes an instance of classifier with the vocabulary. The vocabulary
     * contains all the words contained in any of the reviews.
     *
     * @param vocab the vocabulary
     */
    public ReviewClassifier(Iterable<String> vocab) {
        // TODO
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
        // TODO
        return null;
    }
    
    
    private void incrementMap(Map<String, Integer> map, String key) {
    	int count = map.containsKey(key) ? map.get(key) : 0;
    	map.put(key, count + 1);
    }
}
