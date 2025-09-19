package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovementUtils {
    Collection<ChessMove> getDiagonalMoves(ChessBoard board, ChessPosition myPosition, boolean adjacentOnly) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        // Get the board-accurate numbers
        int boardRow = myPosition.getRow();
        int boardCol = myPosition.getColumn();

        // Diagonal up and right
        for (int i = 1; boardRow + i <= 8; i++) {
            if (boardCol + i > 8) {
                break;
            }

            if (evaluateMoveAndCheckStop(board, myPosition, moves, boardRow + i, boardCol + i)
                    || adjacentOnly) {
                break;
            }
        }

        // Diagonal down and right
        for (int i = 1; boardRow - i > 0; i++) {
            if (boardCol + i > 8) {
                break;
            }

            if (evaluateMoveAndCheckStop(board, myPosition, moves, boardRow - i, boardCol + i)
                    || adjacentOnly) {
                break;
            }
        }

        // Diagonal down and left
        for (int i = 1; boardRow - i > 0; i++) {
            if (boardCol - i <= 0) {
                break;
            }

            if (evaluateMoveAndCheckStop(board, myPosition, moves, boardRow - i, boardCol - i)
                    || adjacentOnly) {
                break;
            }
        }

        // Diagonal up and left
        for (int i = 1; boardRow + i <= 8; i++) {
            if (boardCol - i <= 0) {
                break;
            }

            if (evaluateMoveAndCheckStop(board, myPosition, moves, boardRow + i, boardCol - i)
                    || adjacentOnly) {
                break;
            }
        }

        return moves;
    }

    Collection<ChessMove> getStraightLineMoves (ChessBoard board, ChessPosition myPosition, boolean adjacentOnly) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        // Get the board-accurate numbers
        int boardRow = myPosition.getRow();
        int boardCol = myPosition.getColumn();

        for (int i = boardRow + 1; i <= 8; i++) { // up
            if (evaluateMoveAndCheckStop(board, myPosition, moves, i, boardCol)
                    || adjacentOnly) {
                break;
            }
        }

        for (int i = boardCol + 1; i <= 8; i++) { // right
            if (evaluateMoveAndCheckStop(board, myPosition, moves, boardRow, i)
                    || adjacentOnly) {
                break;
            }
        }

        for (int i = boardRow - 1; i > 0; i--) { // down
            if (evaluateMoveAndCheckStop(board, myPosition, moves, i, boardCol)
                    || adjacentOnly) {
                break;
            }
        }

        for (int i = boardCol - 1; i > 0; i--) { // left
            if (evaluateMoveAndCheckStop(board, myPosition, moves, boardRow, i)
                    || adjacentOnly) {
                break;
            }
        }

        return moves;
    }

    Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();

        int rowAdvancer = myPiece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;

        boolean shouldPromote = shouldPromotePiece(myPiece.getTeamColor(), startRow);

        // Make sure we aren't moving off the board
        if (startRow + rowAdvancer >= 1 && startRow + rowAdvancer <= 8) {
            // Initialize possible moves (null promotion for now, We'll check those later
            ChessPosition forward = new ChessPosition(startRow + rowAdvancer, startCol);
            ChessPosition captureLeft = new ChessPosition(startRow + rowAdvancer, startCol - 1);
            ChessPosition captureRight = new ChessPosition(startRow + rowAdvancer, startCol + 1);

            if (!shouldPromote) {
                // Initialize our moves
                ChessMove forwardMove = new ChessMove(myPosition, forward, null);
                ChessMove captureLeftMove = new ChessMove(myPosition, captureLeft, null);
                ChessMove captureRightMove = new ChessMove(myPosition, captureRight, null);

                if(!isBlocked(board, forward, myPiece)) {
                    moves.add(forwardMove);
                }

                if (willCapturePiece(board, captureLeft, myPiece)) {
                    moves.add(captureLeftMove);
                }

                if (willCapturePiece(board, captureRight, myPiece)) {
                    moves.add(captureLeftMove);
                }
            } else {

            }
        }

        return moves;
    }

    private void addPromotionMoves(ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
    }

    private boolean shouldPromotePiece(ChessGame.TeamColor color, int startRow) {
        return false;
    }

    private boolean willCapturePiece(ChessBoard board, ChessPosition endPosition, ChessPiece myPiece) {
        ChessPiece piece = board.getPiece(endPosition);
        return piece != null && piece.getTeamColor() != myPiece.getTeamColor();
    }

    private boolean isBlocked(ChessBoard board, ChessPosition endPosition, ChessPiece myPiece) {
        ChessPiece piece = board.getPiece(endPosition);

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

        if(willCapturePiece(board, move.getEndPosition(), piece)) {
            moves.add(move);
            return true;
        } else if (isBlocked(board, move.getEndPosition(), piece)) {
            return true;
        } else {
            moves.add(move);
        }
        return false;
    }


}

