package ix.lab02.degdist;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * The reduce operation sums all the degree counts to get the total number of articles for
 * each degree.
 *
 * Example:
 *
 * Input:
 * 8	(1, 2, 1, 1, 1, 1)
 *
 * Output:
 * 8	7
 */
public class DegreeDistributionReducer extends Reducer<IntWritable, IntWritable,  IntWritable, IntWritable> {

    private IntWritable outputValue = new IntWritable(); // total number of articles with this degree

    /**
     * The reduce operation simply sums all individual counts.
     *
     * @param inputKey The degree
     * @param inputValues The list of counts of articles with this degree
     */
    public void reduce(IntWritable inputKey, Iterable<IntWritable> inputValues, Context context)
            throws IOException, InterruptedException {
    	
    	int outDegree = 0;

        for (IntWritable intWritable : inputValues) {
			outDegree += intWritable.get();
		}
        
        outputValue.set(outDegree);
        
        context.write(inputKey, outputValue);

    }

}
