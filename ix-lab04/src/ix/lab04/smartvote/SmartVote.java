package ix.lab04.smartvote;

import java.util.List;

import utils.CandidatesData;
import utils.Common;
import utils.SmartVoteUtils;

public class SmartVote {


    public static void variance(CandidatesData data) {
        /*
         * TODO:
         *
         * 1. Compute the PCA of the candidates' answers matrix.
         * 2. Plot the magnitude of the eigenvalues (on a linear scale.)
         * 3. Compute and print the amount of variance explained by the 1,2,3 first principal components.
         */
    }


    public static void project(CandidatesData data) {
        /*
         * TODO:
         *
         * 1. Compute the PCA of the candidates' answers matrix.
         * 2. Project the candidates on the two first principal components.
         * 3. Plot the 2D projection using SmartVoteUtils.plotProjection(...).
         */
    }


    public static void questions(CandidatesData data, List<String> qs) {
        /*
         * TODO:
         *
         * 1. Compute the PCA of the candidates answers matrix.
         * 2. Extract the three first principal components
         * 3. For each of the three principal component, print the three questions
         *    that contribute most to the component.
         *
         * Hint: use SmartVoteUtils.topThree(..).
         */
    }


    /** Use this function to run the various parts. */
    public static void main(String[] args) {
        CandidatesData data = SmartVoteUtils.readCandidates();
        System.out.println(String.format("Dataset has N = %d candidates and M = %d questions",
                null,  // TODO Number of rows.
                null   // TODO Number of columns.
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
}
