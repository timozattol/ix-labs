package ix.lab07.reviews;

import ix.utils.DataPoint;
import ix.utils.ReviewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class Evaluator {

    private static final int NB_FOLDS = 10;


    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(String.format("Usage: %s path/to/reviews", Evaluator.class.getName()));
            System.exit(0);
        }
        String path = args[0];

        // Read, shuffle and split the data.
        List<DataPoint> data = ReviewUtils.readData(path);
        Set<String> vocab = ReviewUtils.vocabFromData(data);
        Collections.shuffle(data);
        List<List<DataPoint>> splits = new ArrayList<List<DataPoint>>();
        for (int i = 0; i < NB_FOLDS; ++i) {
            int begin = (int)(((double)i / NB_FOLDS) * data.size());
            int end = (int)(((double)(i+1) / NB_FOLDS) * data.size());
            splits.add(data.subList(begin, end));
        }

        // 10-fold cross-validated accuracy.
        List<Double> accuracies = new ArrayList<Double>();
        for (int i = 0; i < NB_FOLDS; ++i) {
            List<DataPoint> train = new ArrayList<DataPoint>();
            for (int j = 0; j < NB_FOLDS; ++j) {
                if (i != j) {
                    train.addAll(splits.get(j));
                }
            }
            ReviewClassifier classifier = new ReviewClassifier(vocab);
            classifier.fit(train);

            List<DataPoint> test = splits.get(i);
            int hits = 0;
            for (DataPoint datum : test) {
                if (classifier.predict(datum.words()) == datum.type) {
                    ++hits;
                }
            }
            accuracies.add((double)hits / test.size());
        }

        // Computing average accuracy and standard deviation.
        double sum = 0.0;
        double ss = 0.0;
        for (double val : accuracies) {
            sum += val;
            ss += val * val;
        }
        double mean = sum / accuracies.size();
        double std = Math.sqrt((ss / accuracies.size()) - (mean * mean));

        // Print out the results.
        System.out.println(String.format("Classifier accuracy: %.2f%%", 100.0 * mean));
        System.out.println(String.format("%d-fold cross-validation (mu=%.3f, sigma=%.3f)",
                NB_FOLDS, mean, std));
    }
}
