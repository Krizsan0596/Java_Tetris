package hu.krizsan.tetris;

public class GameManager {
    private final PieceBag pieceBag = new PieceBag();
    private PieceType nextPiece = pieceBag.pop();
    private NextPiecePanel nextPiecePanel;
    private GamePanel gamePanel;
    private final Board board;
    private boolean running;

    public GameManager(NextPiecePanel nextPiecePanel, GamePanel gamePanel, Board board) {
        this.gamePanel = gamePanel;
        this.nextPiecePanel = nextPiecePanel;
        this.board = board;
    }

    public void start() {
        board.reset();
        pieceBag.reset();
        nextPiece = pieceBag.pop();
        running = true;
        newPiece();
    }

    public boolean isRunning() {
        return running;
    }

    public void newPiece() {
        if (board.spawnPiece(nextPiece)) {
            gamePanel.repaint();
            nextPiece = pieceBag.pop();
            nextPiecePanel.setPiece(nextPiece);
        } else {
            running = false;
        }
    }
}
