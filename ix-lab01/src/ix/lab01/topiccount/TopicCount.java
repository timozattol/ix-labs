package ix.lab01.topiccount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * This job counts the number of occurrences of each topic in the given input file.
 * Copied pasted from WordCount
 */
public class TopicCount {

    public static void main(String[] args) throws Exception {
        // check if both input file and output folder were given as arguments
        if (args.length != 2) {
            System.err.println("Usage: ix.lab01.topiccount.TopicCount <input path> <output path>");
            System.exit(-1);
        }

        // create Hadoop job
        Job job = Job.getInstance();
        job.setJarByClass(TopicCount.class);
        job.setJobName("Topic count");

        // set mapper/reducer classes
        job.setMapperClass(TopicCountMapper.class);
        job.setReducerClass(TopicCountReducer.class);
        job.setCombinerClass(TopicCountReducer.class);

        // define output types: here, output will be "<word>	<nbOccurrences>"
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // define input and output folders
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // launch job with verbose output and wait until it finishes
        job.waitForCompletion(true);
    }

}
