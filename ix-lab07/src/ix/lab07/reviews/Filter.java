package ix.lab07.reviews;

import ix.utils.DataPoint;
import ix.utils.Label;
import ix.utils.ReviewUtils;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Filter {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println(String.format("Usage: %s path/to/reviews", Filter.class.getName()));
            System.exit(0);
        }
        String path = args[0];

        List<DataPoint> data = ReviewUtils.readData(path);
        String review = prompt("Write (or paste) your review here: ");
        DataPoint test = new DataPoint(review, null);

        Set<String> vocab = ReviewUtils.vocabFromData(data);
        for (String word : test.words()) {
            vocab.add(word);
        }

        ReviewClassifier classifier = new ReviewClassifier(vocab);
        classifier.fit(data);
        Label label = classifier.predict(test.words());

        if (label == Label.TRUTHFUL) {
            System.out.println("Congrats! Your review sounds truthful. You fooled me :-)");
        } else if (label == Label.DECEPTIVE) {
            System.out.println("Your review sounds fake. Try again!");
        } else {
            throw new RuntimeException("??!!");
        }
    }

    private static String prompt(String message) {
        Scanner reader = new Scanner(System.in);
        System.out.print(message);
        return reader.nextLine();
    }
}
