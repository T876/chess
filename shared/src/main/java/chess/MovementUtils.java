package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovementUtils {
    public boolean willCapturePiece(ChessBoard board, ChessMove move) {
        return false;
    }

    public boolean isBlocked(ChessBoard board, ChessMove move) {
        return false;
    }

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

            ChessPosition end = new ChessPosition(boardRow + i, boardCol + i);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move)) {
                break;
            } else {
                moves.add(move);
            }
        }

        // Diagonal down and right
        for (int i = 1; row - i >= 0; i++) {
            if (col + i >= 8) {
                break;
            }

            ChessPosition end = new ChessPosition(boardRow - i, boardCol + i);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move)) {
                break;
            } else {
                moves.add(move);
            }
        }

        // Diagonal down and left
        for (int i = 1; row - i >= 0; i++) {
            if (col - i < 0) {
                break;
            }

            ChessPosition end = new ChessPosition(boardRow - i, boardCol - i);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move)) {
                break;
            } else {
                moves.add(move);
            }
        }

        // Diagonal up and left
        for (int i = 1; row + i < 8; i++) {
            if (col - i < 0) {
                break;
            }

            ChessPosition end = new ChessPosition(boardRow + i, boardCol - i);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move)) {
                break;
            } else {
                moves.add(move);
            }
        }

        return moves;
    }


}

