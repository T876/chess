package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovementUtils {
    public boolean willCapturePiece(ChessBoard board, ChessMove move, ChessPiece myPiece) {
        int row = move.getEndPosition().getRow() - 1;
        int col = move.getEndPosition().getColumn() - 1;

        ChessPiece piece = board.squares[row][col];

        return piece != null && piece.getTeamColor() != myPiece.getTeamColor();
    }

    public boolean isBlocked(ChessBoard board, ChessMove move, ChessPiece myPiece) {
        int row = move.getEndPosition().getRow() - 1;
        int col = move.getEndPosition().getColumn() - 1;

        ChessPiece piece = board.squares[row][col];

        return piece != null && piece.getTeamColor() == myPiece.getTeamColor();
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

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
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

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
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

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
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

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
                break;
            } else {
                moves.add(move);
            }
        }

        return moves;
    }

    Collection<ChessMove> getStraightLineMoves (ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        // Get the code-accurate numbers
        int[] realPos = board.programmaticPosition(myPosition);
        int row = realPos[0];
        int col = realPos[1];

        // Get the board-accurate numbers
        int boardRow = myPosition.getRow();
        int boardCol = myPosition.getColumn();
        ChessPosition start = new ChessPosition(boardRow, boardCol);

        for (int i = boardRow + 1; i <= 8; i++) { // up
            ChessPosition end = new ChessPosition(i, boardCol);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
                break;
            } else {
                moves.add(move);
            }
        }

        for (int i = boardRow - 1; i > 0; i--) { // down
            ChessPosition end = new ChessPosition(i, boardCol);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
                break;
            } else {
                moves.add(move);
            }
        }

        for (int i = boardCol + 1; i <= 8; i++) { // right
            ChessPosition end = new ChessPosition(boardRow, i);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
                break;
            } else {
                moves.add(move);
            }
        }

        for (int i = boardCol - 1; i > 0; i--) { // left
            ChessPosition end = new ChessPosition(boardRow, i);
            ChessMove move = new ChessMove(start, end, null);

            if(willCapturePiece(board, move, piece)) {
                moves.add(move);
                break;
            } else if (isBlocked(board, move, piece)) {
                break;
            } else {
                moves.add(move);
            }
        }

        return moves;
    }


}

