package ix.lab04.smartvote;

import ix.lab04.faces.PCAResult;
import ix.lab04.faces.Faces;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import Jama.Matrix;
import utils.CandidatesData;
import utils.Common;
import utils.SmartVoteUtils;

public class SmartVote {

	/**
     * 1. Compute the PCA of the candidates' answers matrix.
     * 2. Plot the magnitude of the eigenvalues (on a linear scale.)
     * 3. Compute and print the amount of variance explained by the 1,2,3 first principal components.
     */
    public static void variance(CandidatesData data) {
    	
    	PCAResult pcaResult = Faces.pca(data.answersMatrix);
    	
    	double[] eigenValues = pcaResult.values;
    	
    	Common.linPlot(eigenValues);
    	
    	double sumValues = 0;
    	
    	for (double v : eigenValues) {
			sumValues += v;
		}
    	
    	System.out.println("Percentage of \"variance\" explained by first components:");
    	
    	for (int i = 0; i < 3; i++) {
    		double percentage = 0;
    		
    		for (int j = 0; j <= i; j++) {
				percentage += eigenValues[j] / sumValues;
			}

    		System.out.println("Percentage of variance of components 1 to " + (i+1) + ": " + percentage * 100);

		}
    }

    /**
	 * 1. Compute the PCA of the candidates' answers matrix.
	 * 2. Project the candidates on the two first principal components.
	 * 3. Plot the 2D projection using SmartVoteUtils.plotProjection(...).
	 */
    public static void project(CandidatesData data) {
    	Matrix projected = Faces.project(data.answersMatrix);

    	SmartVoteUtils.plotProjection(
    			extractRow(projected, 0),
    			extractRow(projected, 1),
    			data.partyAffiliations);
    }

    /**
     * 1. Compute the PCA of the candidates answers matrix.
     * 2. Extract the three first principal components
     * 3. For each of the three principal component, print the three questions
     *    that contribute most to the component.
     *
     * Hint: use SmartVoteUtils.topThree(..).
     */
    public static void questions(CandidatesData data, List<String> qs) {
        PCAResult pcaResult = Faces.pca(data.answersMatrix);
        
        for (int i = 0; i < 3; i++) {
			double[] rotationRow = extractRow(pcaResult.rotation, i);
			
			System.out.println("Dimension " + (i + 1) + "'s \"best\" questions:");
			
			for (int j : SmartVoteUtils.topThree(rotationRow)) {
				System.out.println(qs.get(j));
			}
			System.out.println();
		}
        
//        double[] v = Faces.variance(data.answersMatrix);
//        for (int i = 0; i < v.length; i++) {
//			System.out.println(v[i]);
//		}
        
//        for(double i : pcaResult.values) {
//        	System.out.println(i);
//        }

//        for(int i : SmartVoteUtils.topThree(pcaResult.values)) {
//        	System.out.println(i + ":" + qs.get(i));
//        }
    }


    /** Use this function to run the various parts. */
    public static void main(String[] args) {
        CandidatesData data = SmartVoteUtils.readCandidates();
        System.out.println(String.format("Dataset has N = %d candidates and M = %d questions",
                data.answersMatrix.getRowDimension(),
                data.answersMatrix.getColumnDimension()
                ));

        // Prompt the user for an action.
        String action = Common.getString("action [variance/project/questions]: ");

        if ("variance".equals(action)) {
            variance(data);

        } else if ("project".equals(action)) {
            project(data);

        } else if ("questions".equals(action)) {
            List<String> qs = SmartVoteUtils.readQuestions();
            questions(data, qs);
        }
    }
    
    private static double[] extractRow(Matrix m, int r) {
    	int rowDimension = m.getRowDimension();
    	int colDimension = m.getColumnDimension();
    	
    	double[] row = new double[rowDimension];
    	
    	for (int i = 0; i < colDimension; i++) {
			row[i] = m.get(i, r);
		}
    	
    	return row;
    }
}
