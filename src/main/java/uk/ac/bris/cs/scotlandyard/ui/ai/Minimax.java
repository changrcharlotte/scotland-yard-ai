package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.graph.Traverser;
import jakarta.annotation.Nonnull;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Transport;
public class Minimax {

        private Integer minimax(Board position, int depth, int alpha, int beta, Boolean maximisingPlayer){

        if (depth==0 || !(position.getWinner().isEmpty())){
            return evaluateScore(position);
        }

        ImmutableList<Move> moves = position.getAvailableMoves().asList();
        if(maximisingPlayer){
            int maxEval = Integer.MIN_VALUE;
            for(Move child : moves){
                Board newBoard = position.
                int eval = minimax()
            }
        }
    }

}
