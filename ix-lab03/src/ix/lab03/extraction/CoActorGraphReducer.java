package ix.lab03.extraction;

import ix.utils.TextArrayWritable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

@SuppressWarnings("unused")
public class CoActorGraphReducer extends Reducer<Text, // input key: actor name
                                              TextArrayWritable, // input values: list of co-actors in one movie
                                              Text, // output key: actor name
                                              TextArrayWritable> { // output value: list of co-actors for all movies

    private TextArrayWritable outputValue = new TextArrayWritable();

    /**
     * The reduce operation simply creates a set with the co-actors of all movies.
     *
     * @param inputKey  the actor name
     * @param inputValues  an iterable of list of co-actors
     */
    public void reduce(Text inputKey, Iterable<TextArrayWritable> inputValues, Context context)
            throws IOException, InterruptedException {

    	Set<String> outputValues = new HashSet<String>();
    	
    	for (TextArrayWritable inputValue : inputValues) {
    		for (String value : inputValue.toStrings()) {
    			outputValues.add(value);
    		}
    	}
    	
    	outputValue.setStringCollection(outputValues);
    	
    	
        context.write(inputKey, outputValue);
        

    }

}
