package hu.krizsan.tetris;

public class GameManager {
    private final PieceBag pieceBag = new PieceBag();
    private PieceType currentPiece;
    private PieceType nextPiece;
    private NextPiecePanel nextPiecePanel;
    private GamePanel gamePanel;

    public GameManager(NextPiecePanel nextPiecePanel, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.nextPiecePanel = nextPiecePanel;
    }

    public void newPiece() {
        currentPiece = nextPiece;
        nextPiece = pieceBag.pop();
        nextPiecePanel.setPiece(nextPiece);
    }
}
