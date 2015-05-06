package ix.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/** Some utilities to read and process the hotel reviews. */
public class ReviewUtils {


    /** Extracts the vocabulary from the review data. */
    public static Set<String> vocabFromData(Collection<DataPoint> data) {
        Set<String> vocab = new HashSet<String>();
        for (DataPoint datum : data) {
            for (String word : datum.words()) {
                vocab.add(word);
            }
        }
        return vocab;
    }


    /** Reads the review files and instantiates the data points. */
    public static List<DataPoint> readData(String path) throws IOException {
        File root = new File(path);
        File deceptive = new File(root, "deceptive");
        File truthful = new File(root, "truthful");

        List<DataPoint> data = new LinkedList<DataPoint>();
        for (File f : deceptive.listFiles()) {
            String review = Files.toString(f, Charsets.UTF_8);
            data.add(new DataPoint(review, Label.DECEPTIVE));
        }
        for (File f : truthful.listFiles()) {
            String review = Files.toString(f, Charsets.UTF_8);
            data.add(new DataPoint(review, Label.TRUTHFUL));
        }
        return data;
    }
}
