package ix.lab01.wikipedia;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reduce operation of the LastEdit job. The reducer gathers all edits of one
 * article, and simply keeps the most recent one.
 *
 * It simply sums all the individual counts to get the total number of
 * occurrences for this word.
 *
 * Example:
 *
 * Input: AccessibleComputing	"2001-01-21 02:12:21 | link1, link2", "2008-06-29 12:12:21 | link3"
 *
 * Output: AccessibleComputing	"2008-06-29 12:12:21 | link3"
 *
 */
public class LastEditReducer extends Reducer<Text, Text, Text, Text> {

    private Text outputValue = new Text(); // the most recent edit

    /**
     * The reduce operation simply keeps the most recent edit.
     *
     * Hint: each value is of the form "YYYY-MM-DD hh:mm:ss | link1, link2". You
     * can thus directly compare the whole value instead of extracting the
     * timestamp first.
     *
     * @param inputKey
     *            The article name
     * @param inputValues
     *            The list of edits
     */
    public void reduce(Text inputKey, Iterable<Text> inputValues,
            Context context) throws IOException, InterruptedException {
    	
    	Iterator<Text> iterator = inputValues.iterator();
    	
    	Text newestEdit = new Text();
    	Text nextEdit = new Text();
    	
    	newestEdit.set(iterator.next());
    	
    	while(iterator.hasNext()) {
    		nextEdit.set(iterator.next());
    		
    		// Comparison works because the DateTime is written first.
    		// Here checking if nextEdit is newer than newestEdit
    		if (nextEdit.toString().compareTo(newestEdit.toString()) > 0) {
    			newestEdit.set(nextEdit);
    		}
		}
		
		outputValue.set(newestEdit);
		
		// write output key/value to the context
        context.write(inputKey, outputValue);
    }

}
