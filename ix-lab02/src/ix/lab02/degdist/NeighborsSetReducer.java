package ix.lab02.degdist;

import ix.utils.TextArrayWritable;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reduce operation of the NeighbSet job. The reducer gathers all the neighbors
 * of an article outputs its set of linked articles. For example:
 *
 * ## Input:
 * Article1 (Article2, Article3, Article2)
 *
 * ## Output:
 * Article1 Article2, Article3
 */
public class NeighborsSetReducer extends Reducer<Text, Text, Text, TextArrayWritable> {

    private TextArrayWritable outputValue = new TextArrayWritable();

    /**
     * Each reduce operation takes an article and its list of linked articles,
     * and extract the set of unique linked articles.
     *
     * Hint: use the setStringCollection() method of {@link TextArrayWritable}
     * once you have obtained the set of linked articles.
     *
     * @param inputKey The name of the articles
     * @param inputValue A list of linked articles (possibly with duplicates)
     */
    public void reduce(Text sourceArticle, Iterable<Text> coeditedArticles, Context context)
            throws IOException, InterruptedException {

        
    	Set<String> outArticles = new TreeSet<String>();
    	
    	for (Text article : coeditedArticles) {
			outArticles.add(article.toString());
		}
    	
    	outputValue.setStringCollection(outArticles);
    	
    	context.write(sourceArticle, outputValue);
    }

}
