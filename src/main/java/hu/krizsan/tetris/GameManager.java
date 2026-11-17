package hu.krizsan.tetris;

public class GameManager {
    private final PieceBag pieceBag = new PieceBag();
    private PieceType nextPiece = pieceBag.pop();
    private NextPiecePanel nextPiecePanel;
    private GamePanel gamePanel;
    private final Board board;

    public GameManager(NextPiecePanel nextPiecePanel, GamePanel gamePanel, Board board) {
        this.gamePanel = gamePanel;
        this.nextPiecePanel = nextPiecePanel;
        this.board = board;
    }

    public void newPiece() {
        board.spawnPiece(nextPiece);
        gamePanel.repaint();
        nextPiece = pieceBag.pop();
        nextPiecePanel.setPiece(nextPiece);
    }
}
