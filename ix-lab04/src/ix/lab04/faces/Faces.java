package ix.lab04.faces;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import Jama.Matrix;
import utils.Common;
import utils.EigenDecomposition;
import utils.FacesUtils;
import utils.NotYetImplementedException;

public class Faces {

    /**
     * Computes the variance of the dataset along each dimension (i.e. each column).
     * @return an array of doubles containing the variance for each column
     */
    public static double[] variance(Matrix data) {
        int N = data.getRowDimension();
    	int M = data.getColumnDimension();
        double[] variances = new double[M];

        // Don't forget to center the data!
        // Hint: util.Common provides a few handy functions.
        
        double[] expectations = Common.getColMean(data);
        
        for (int i = 0; i < M; i++) {
        	double varianceSum = 0;
        	
        	for (int j = 0; j < N; j++) {
        		varianceSum += Math.pow(data.get(j, i) - expectations[i], 2);
    		}
        	
        	variances[i] = varianceSum / N;
		}

        return variances;
    }


    /**
     * Computes the PCA of the dataset (i.e. the eigendecomposition of
     * the covariance matrix.)
     * @return a container with with the result of the PCA
     */
    public static PCAResult pca(Matrix data) {
    	// Note: the class Jama.Matrix defines several handy methods. Using them,
        // you should only need a couple of lines to complete this function.
        //
        // You also might find utils.EigenDecomposition useful.

        Matrix centered = Common.center(data);
        
        Matrix cov = (centered.transpose().times(centered)).times((1 / (double) data.getRowDimension()));
        
        EigenDecomposition eig = new EigenDecomposition(cov);
        
        return new PCAResult(eig.eigenvectors, eig.eigenvalues);
    }


    /**
     * Projects the dataset on the new basis formed by the PCA.
     * @return a new matrix with the projected version of the data
     */
    public static Matrix project(Matrix data) {
        PCAResult result = pca(data);
        Matrix projected = null;
        
        Matrix centered = Common.center(data);

        projected = centered.times(result.rotation);

        return projected;
    }


    /** Use this function to run the various parts. */
    public static void main(String[] args) {
        Matrix data = FacesUtils.readFacesData();
        
        System.out.println(String.format(
                "Dataset has %d rows (items, faces) and %d columns (measurements per item).",
                data.getRowDimension(),
                data.getColumnDimension()
                ));

        // Prompt the user for an action.
        String action = Common.getString("action [variance/pca/project/extremes/face_values]: ");

        if ("variance".equals(action)) {
            // Plot the variance of each dimension of the dataset.
            double[] variances = variance(data);
            Common.linPlot(variances);

        } else if ("pca".equals(action)) {
            // Plot the magnitude of the components in log scale.
            PCAResult result = pca(data);
            Common.logPlot(result.values);

        } else if ("project".equals(action)) {
            // Print a row of the data projected on the new basis.
            Matrix projected = project(data);
            int id = Common.getInt("row / item: ");
            FacesUtils.printRow(projected, id);

        } else if ("extremes".equals(action)) {
            // Print items with extreme values (both min and max) along a
            // given principalcomponent.
            Matrix projected = project(data);
            int dim = Common.getInt("dimension: ");
            FacesUtils.printExtremes(projected, dim, 10);
        } else if ("face_values".equals(action)) {
        	int row = Common.getInt("row: ");
        	
        	NumberFormat formatter = new DecimalFormat("#0.00");     
        	
        	Matrix projected = project(data);
        	for (int i = 0; i < 6; i++) {
				System.out.println("Dimension " + (i + 1) + ": " + formatter.format(projected.get(row, i)));
			}
        }
    }
}
