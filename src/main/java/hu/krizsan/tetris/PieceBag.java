package hu.krizsan.tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PieceBag {
    private final List<PieceType> pieces = new ArrayList<>(7);

    public PieceBag() {
        refill();
    }

    private void refill() {
        pieces.addAll(Arrays.asList(PieceType.values()));
        Collections.shuffle(pieces);
    }

    public PieceType pop() {
        PieceType piece = pieces.remove(0);
        if (pieces.isEmpty()) {
            refill();
        }
        return piece;
    }
}
