package ix.utils;

/** Simple POJO to hold review attributes. */
public class DataPoint {

    final private String review;

    /** Label ('truthful' or 'deceptive') of a review. */
    final public Label type;

    public DataPoint(String review, Label type) {
        this.review = review;
        this.type = type;
    }

    /** Words that make up the review, after preprocessing. */
    public Iterable<String> words() {
        return DocumentTokenization.stream(this.review);
    }
}
