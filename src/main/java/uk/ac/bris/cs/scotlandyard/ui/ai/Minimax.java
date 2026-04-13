package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Minimax {

        Board board;
        DijkstraUndirectedSP dj;

        private Minimax(Board board){
            this.board = board;
            this.dj = new DijkstraUndirectedSP(board.getSetup().graph);

        }

        private Integer minimax(Move mv, int depth, int alpha, int beta, Boolean maximisingPlayer){

        if (depth==0 || !(board.getWinner().isEmpty())){
            return staticEvaluation(mv);
        }


        if(maximisingPlayer){ //this would be mrX
            int maxEval = Integer.MIN_VALUE;
            ImmutableList<Move> moves = board.getAvailableMoves().asList();
            for(Move child : moves){
                int eval = minimax(child, depth-1, alpha, beta, false );
                //simulate advance here idfk
                alpha = Integer.max(alpha, eval);
                if(beta <= alpha){
                    break;
                }
            }
            return maxEval;
        }
        else{
            int minEval = Integer.MIN_VALUE;
            ImmutableList<Move> moves = board.getAvailableMoves().asList();
            for(Move child: moves){
                int eval = minimax(child, depth-1, alpha, beta, true);
                minEval = Integer.min(minEval, eval);
                beta = Integer.min(beta, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return minEval;
        }

    }



        //the static evaluation of any given point of the chosen move should think about
        // 1) how far away mrX is from the detectives. if you choose mrX to be maximising player then that's maximising distance. when it's detectives turn... idfk that's the problem. are they meant to pick the best move? how would they know where the best move is if they don't know where mrX is?
        //we need static evaluation to be a number so literally just how good is this move.
        private int staticEvaluation(Move mv){
            int dest = mv.accept(new destinationVisitor());
            int eval = dj.findSP(dest, detLocations);
            eval = eval + (board.getSetup().graph.degree(dest));

        };


        private void simulateAdvance(Move move){
            Move.SingleMove mv = (Move.SingleMove) move;

            //initialising new gamestate variables
            Player newMrX = mrX;
            List<Player> newDetectives = new ArrayList<>(detectives);
            ImmutableList<LogEntry> newLog = log;
            Set<Piece> newRemaining = new HashSet<>(remaining);

            if (mv.commencedBy().isMrX()) { //if the piece making the move is MrX
                newMrX = mrX.use(mv.ticket).at(mv.destination); //use the ticket

                boolean reveal = setup.moves.get(log.size()); //get whether the mrX piece is being revealed on this turn
                LogEntry entry;
                if (reveal) {
                    entry = LogEntry.reveal(mv.ticket, mv.destination);
                } else {
                    entry = LogEntry.hidden(mv.ticket);
                }

                newLog = ImmutableList.<LogEntry>builder().addAll(log).add(entry).build();
                newRemaining.clear();
                for (Player d : newDetectives) { //basically if a move can be played, any move at all then add it into the new remaining
                    if (!makeSingleMoves(setup, newDetectives, d, d.location()).isEmpty()) {
                        newRemaining.add(d.piece());
                    }
                }

            } else { //if the piece making the move is not MrX
                for (int i = 0; i < newDetectives.size(); i++) {
                    Player d = newDetectives.get(i);
                    if (d.piece() == mv.commencedBy()) { //check which piece the move belongs to
                        Player updated = d.use(mv.ticket).at(mv.destination);
                        newDetectives.set(i, updated);
                        newMrX = newMrX.give(mv.ticket); //because mrX uses the discarded tickets i think lmao
                        break;
                    }
                }

                newRemaining.remove(mv.commencedBy());

            }
            if (newRemaining.isEmpty()) {
                newRemaining.add(Piece.MrX.MRX); //if it's empty then you know it's the next round basically and it's now mrX's turn
            }

            //return the gamestate
            return new MyGameStateFactory.MyGameState(
                    setup,
                    ImmutableSet.copyOf(newRemaining),
                    newLog,
                    newMrX,
                    newDetectives
            );
        }



}
