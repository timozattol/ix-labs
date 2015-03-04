package ix.lab03.extraction;

import ix.utils.TextArrayWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

@SuppressWarnings("unused")
public class CoActorGraphMapper extends Mapper<Text, // input key: movie title
                                          Text, // input value: actors list
                                          Text, // output key: actor name
                                          TextArrayWritable> { // output value: list of his co-actors in this movie

    private static final Pattern YEAR_PATTERN = Pattern.compile(".+ \\((\\d{4})\\)");

    private Text outputKey = new Text();
    private TextArrayWritable outputValue = new TextArrayWritable();

    private int startingYear;
    private int endingYear;

    public void map(Text inputKey, Text inputValue, Context context)
            throws IOException, InterruptedException {

        // Starting year of the period of interest.
        this.startingYear = context.getConfiguration().getInt("startingYear", -1);
        // Ending year of the period of interest
        this.endingYear = context.getConfiguration().getInt("endingYear", -1);

        Matcher m = YEAR_PATTERN.matcher(inputKey.toString());
        if (!m.matches()) {
            throw new RuntimeException(String.format("Failed to process key ''", inputKey.toString()));
        }

        int movieYear = Integer.parseInt(m.group(1));
        if(movieYear > endingYear || movieYear < startingYear){
        	// outside of year range
        	return;
        }
        
        /* split stringlist of actors into set */
//        Collection<String> movieActors = new HashSet<String>();
//        StringTokenizer tokenizer = new StringTokenizer(inputValue.toString(), ",");
//        while(tokenizer.hasMoreTokens()) {
//        	movieActors.add(tokenizer.nextToken());
//        }
        
        Collection<String> movieActors = Arrays.asList(inputValue.toString().split(","));
        
        
        /* for each actor in movie, write his co-actors to output */
        for(String actor : movieActors) {
        	Collection<String> actorsWithoutActor = new ArrayList<String>(movieActors);
        	actorsWithoutActor.remove(actor);
        	outputValue.setStringCollection(actorsWithoutActor);
        	
        	outputKey.set(actor);
        	context.write(outputKey, outputValue);
        }
        
    }

}
