package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        if (piece.getPieceType() == PieceType.BISHOP) {
            moves.addAll(getDiagonalMoves(board, myPosition));
        }
        return moves;
    }

    private Collection<ChessMove> getDiagonalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        // Get the code-accurate numbers
        int[] realPos = board.programmaticPosition(myPosition);
        int row = realPos[0];
        int col = realPos[1];

        // Get the board-accurate numbers
        int boardRow = myPosition.getRow();
        int boardCol = myPosition.getColumn();
        ChessPosition start = new ChessPosition(boardRow, boardCol);

        // Diagonal up and right
        for (int i = 1; row + i < 8; i++) {
            if (col + i >= 8) {
                break;
            }

            ChessPosition end = new ChessPosition(boardRow + i, boardCol + i);
            moves.add(new ChessMove(start, end, null));
        }

        // Diagonal down and right
        for (int i = 1; row - i >= 0; i++) {
            if (col + i >= 8) {
                break;
            }

            ChessPosition end = new ChessPosition(boardRow - i, boardCol + i);
            moves.add(new ChessMove(start, end, null));
        }

        // Diagonal down and left
        for (int i = 1; row - i >= 0; i++) {
            if (col - i < 0) {
                break;
            }

            ChessPosition end = new ChessPosition(boardRow - i, boardCol - i);
            moves.add(new ChessMove(start, end, null));
        }

        // Diagonal up and left
        for (int i = 1; row + i < 8; i++) {
            if (col - i < 0) {
                break;
            }

            ChessPosition end = new ChessPosition(boardRow + i, boardCol - i);
            moves.add(new ChessMove(start, end, null));
        }

        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
