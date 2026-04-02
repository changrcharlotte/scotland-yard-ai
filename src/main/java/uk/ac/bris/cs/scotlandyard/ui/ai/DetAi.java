package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.Nonnull;
import io.atlassian.fugue.Pair;
import org.checkerframework.checker.nullness.qual.NonNull;
import uk.ac.bris.cs.scotlandyard.model.Ai;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;
public class DetAi implements Ai{

    @Override
    public @NonNull String name() {
        return "DetAi";
    }

    @Override
    public @NonNull Move pickMove(@NonNull Board board, Pair<Long, TimeUnit> timeoutPair) {
        var moves = board.getAvailableMoves().asList();
        return moves.get(new Random().nextInt(moves.size()));
    }
}
