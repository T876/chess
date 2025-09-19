package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovementUtils {
    Collection<ChessMove> getDiagonalMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
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

            if (evaluateMoveAndCheckStop(board, start, moves, boardRow + i, boardCol + i)) {
                break;
            }
        }

        // Diagonal down and right
        for (int i = 1; row - i >= 0; i++) {
            if (col + i >= 8) {
                break;
            }

            if (evaluateMoveAndCheckStop(board, start, moves, boardRow - i, boardCol + i)) {
                break;
            }
        }

        // Diagonal down and left
        for (int i = 1; row - i >= 0; i++) {
            if (col - i < 0) {
                break;
            }

            if (evaluateMoveAndCheckStop(board, start, moves, boardRow - i, boardCol - i)) {
                break;
            }
        }

        // Diagonal up and left
        for (int i = 1; row + i < 8; i++) {
            if (col - i < 0) {
                break;
            }

            if (evaluateMoveAndCheckStop(board, start, moves, boardRow + i, boardCol - i)) {
                break;
            }
        }

        return moves;
    }

    Collection<ChessMove> getStraightLineMoves (ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        // Get the board-accurate numbers
        int boardRow = myPosition.getRow();
        int boardCol = myPosition.getColumn();
        ChessPosition start = new ChessPosition(boardRow, boardCol);

        for (int i = boardRow + 1; i <= 8; i++) { // up
            if (evaluateMoveAndCheckStop(board, start, moves, i, boardCol)) {
                break;
            }
        }

        for (int i = boardCol + 1; i <= 8; i++) { // right
            if (evaluateMoveAndCheckStop(board, start, moves, boardRow, i)) {
                break;
            }
        }

        for (int i = boardRow - 1; i > 0; i--) { // down
            if (evaluateMoveAndCheckStop(board, start, moves, i, boardCol)) {
                break;
            }
        }

        for (int i = boardCol - 1; i > 0; i--) { // left
            if (evaluateMoveAndCheckStop(board, start, moves, boardRow, i)) {
                break;
            }
        }

        return moves;
    }

    private boolean willCapturePiece(ChessBoard board, ChessMove move, ChessPiece myPiece) {
        int row = move.getEndPosition().getRow() - 1;
        int col = move.getEndPosition().getColumn() - 1;

        ChessPiece piece = board.squares[row][col];

        return piece != null && piece.getTeamColor() != myPiece.getTeamColor();
    }

    private boolean isBlocked(ChessBoard board, ChessMove move, ChessPiece myPiece) {
        int row = move.getEndPosition().getRow() - 1;
        int col = move.getEndPosition().getColumn() - 1;

        ChessPiece piece = board.squares[row][col];

        return piece != null && piece.getTeamColor() == myPiece.getTeamColor();
    }

    private boolean evaluateMoveAndCheckStop(ChessBoard board,
                                             ChessPosition start,
                                             Collection<ChessMove> moves,
                                             int newRow,
                                             int newCol) {
        ChessPiece myPiece = board.getPiece(start);
        ChessPosition end = new ChessPosition(newRow, newCol);
        ChessMove move = new ChessMove(start, end, null);

        if (shouldStopMovement(board, move, myPiece, moves)) {
            return true;
        }

        return false;
    }

    private boolean shouldStopMovement(ChessBoard board,
                                       ChessMove move,
                                       ChessPiece piece,
                                       Collection<ChessMove> moves) {

        if(willCapturePiece(board, move, piece)) {
            moves.add(move);
            return true;
        } else if (isBlocked(board, move, piece)) {
            return true;
        } else {
            moves.add(move);
        }
        return false;
    }


}

