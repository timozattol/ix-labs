package ix.lab06.clustering;

import ix.lab06.utils.DataUtils;
import ix.lab06.utils.Point2d;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Kmeans {	
    /** Number of clusters */
    protected int k;

    /** Data points */
    protected Point2d[] data;

    /** Centers of clusters */
    protected Point2d[] centers;

    /** Assignment of points to clusters */
    protected int[] assignments;
    
    
    private List<Color> sampleColors = Arrays.asList(
    		new Color(255, 0, 0), 
    		new Color(0, 255, 0), 
    		new Color(0, 0, 255),
    		new Color(255, 255, 0), 
    		new Color(0, 255, 255), 
    		new Color(255, 0, 255));

    public Kmeans(int k) {
        if (k < 2) {
            throw new IllegalArgumentException(
                    "The number of clusters k must be greater than 1");
        }
        this.k = k;
        this.centers = new Point2d[k];
        this.assignments = new int[k];
    }

    public Kmeans(Point2d[] data, int k) {
        this(k);
        this.setData(data);
    }

    public void setData(Point2d[] data) {
        this.data = data;
        this.assignments = new int[data.length];
    }

    /**
     * Initializes the clusters by randomly assigning a data point to each
     * center.
     */
    public void initialize() {
        // Create a random collection containing the data-points
        ArrayList<Integer> index = new ArrayList<Integer>();
        for (int i = 0; i < this.data.length; i++) {
            index.add(i);
        }
        Collections.shuffle(index);

        for (int c = 0; c < this.k; c++) {
            Point2d p = this.data[index.get(c)];
            this.centers[c] = new Point2d(p);
        }
    }

    /**
     * Expectation step of k-means
     */
    public void eStep() {
        // We assign each data point to the closest cluster center
        for (int p = 0; p < this.data.length; p++) {
            double distanceMin = Double.MAX_VALUE;

            for (int c = 0; c < this.k; c++) {
                double distance = this.data[p].distanceTo(this.centers[c]);

                if (distance < distanceMin) {
                    distanceMin = distance;
                    this.assignments[p] = c;
                }
            }
        }
    }

    /**
     * Maximization step of k-means.
     */
    public void mStep() {
        // According to the assignment, we update the center of each cluster
    	Point2d[] sumPoints = new Point2d[centers.length];
    	int[] nPoints = new int[centers.length];
    	
    	for (int i = 0; i < sumPoints.length; i++) {
			sumPoints[i] = new Point2d(0, 0);
		}

        for (int i = 0; i < data.length; i++) {
			int k = assignments[i];
			Point2d currentSum = sumPoints[k];
			Point2d currentData = data[i];
			currentSum.set(
					currentSum.getX() + currentData.getX(),
					currentSum.getY() + currentData.getY());
			++nPoints[k];
		}
        
        // Divide each sum by the number of points
        for (int i = 0; i < sumPoints.length; i++) {
			sumPoints[i].set(
					sumPoints[i].getX() / nPoints[i], 
					sumPoints[i].getY() / nPoints[i]);
		}
        
        centers = sumPoints;
    }

    /**
     * Computes the total distortion, i.e., the sum of the squares of the
     * distances of each data point to the center of its cluster
     * 
     * @return The total distortion
     */
    public double distortionMeasure() {
        double distortion = 0;
        for (int p = 0; p < this.data.length; p++) {
            Point2d point = this.data[p];
            int assignment = this.assignments[p];
            Point2d center = this.centers[assignment];

            double distance = point.distanceTo(center);
            distortion += Math.pow(distance, 2);
        }
        return distortion;
    }

    /**
     * Runs the EM iterations for a fixed number of iterations.
     * 
     * @param nbIterations
     *            Number of iterations to run.
     */
    public void run(int nbIterations) {
    	double epsilon = 0.01;
    	double prevDistortion = Double.MAX_VALUE;

        for (int i = 0; i < nbIterations; i++) {
            this.eStep();
            this.mStep();
            double distortion = distortionMeasure();
            System.out.format("Distortion: %f%n", distortion);
            
            if (prevDistortion - distortion < distortion * epsilon) {
				break;
			}
            
            prevDistortion = distortion;
        }
    }

    /**
     * Prints the clusters' centers.
     */
    public void print() {
        for (int c = 0; c < this.k; c++) {
            Point2d center = this.centers[c];
            System.out.format("Center of cluster %d: %s\n", c, center);
        }
    }

    public void plot() {
        Random rnd = new Random();
        List<Point2d[]> points = new ArrayList<Point2d[]>();
        List<Color> colors = new ArrayList<Color>();
                
        for (int c = 0; c < this.k; ++c) {
            Color col = sampleColors.get(Math.min(c, sampleColors.size()));
            
            List<Point2d> clusterPoints = new ArrayList<Point2d>();
            for (int p = 0; p < this.data.length; ++p) {
                if (this.assignments[p] == c) {
                    clusterPoints.add(this.data[p]);
                }
            }
            
            points.add(clusterPoints.toArray(new Point2d[0]));
            colors.add(col);
        }
        
        DataUtils.scatterPlot(points, colors);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.format(
                    "Usage: %s /path/to/data/points.txt nb_clusters nb_iter\n",
                    Kmeans.class.getName());
            System.exit(-1);
        }

        String fileName = args[0];
        int nbClusters = Integer.parseInt(args[1]);
        int nbIter = Integer.parseInt(args[2]);

        Point2d[] data = DataUtils.readFromFile(fileName);

        Kmeans kmeans = new Kmeans(data, nbClusters);
        kmeans.initialize();
        kmeans.run(nbIter);
        kmeans.print();
        kmeans.plot();
    }

}
