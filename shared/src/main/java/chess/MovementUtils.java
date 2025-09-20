package chess;

import java.util.ArrayList;
import java.util.Arrays;
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

    Collection<ChessMove> getKnightMoves (ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPiece myPiece = board.getPiece(myPosition);
        Collection<ChessMove> potentialMoves = new ArrayList<ChessMove>(Arrays.asList(
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1),null),
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1),null),
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1),null),
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1),null),
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2),null),
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2),null),
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2),null),
                new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2),null)
        ));

        for (ChessMove move : potentialMoves) {
            if(positionIsInBounds(move.getEndPosition()) && !isBlocked(board, move.getEndPosition(), myPiece)) {
                moves.add(move);
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

        // Initialize possible moves (null promotion for now, We'll check those later
        ChessPosition forward = new ChessPosition(startRow + rowAdvancer, startCol);
        ChessPosition captureLeft = new ChessPosition(startRow + rowAdvancer, startCol - 1);
        ChessPosition captureRight = new ChessPosition(startRow + rowAdvancer, startCol + 1);

        handlePawnMove(myPosition, forward, myPiece, board, moves);
        handlePawnMove(myPosition, captureLeft, myPiece, board, moves);
        handlePawnMove(myPosition, captureRight, myPiece, board, moves);

        if (isPawnFirstMove(myPosition, myPiece) && !isPawnBlocked(board, forward)) {
            ChessPosition doubleForward = new ChessPosition(startRow + (rowAdvancer * 2), startCol);
            handlePawnMove(myPosition, doubleForward, myPiece, board, moves);
        }

        return moves;
    }

    private boolean isPawnFirstMove(ChessPosition myPosition, ChessPiece myPiece) {
        return myPiece.getTeamColor() == ChessGame.TeamColor.WHITE ? myPosition.getRow() == 2 : myPosition.getRow() == 7;
    }

    private void handlePawnMove (ChessPosition startPosition,
                                 ChessPosition endPosition,
                                 ChessPiece myPiece,
                                 ChessBoard board,
                                 Collection<ChessMove> moves) {

        boolean shouldPromote = shouldPromotePiece(myPiece.getTeamColor(), startPosition.getRow());
        if (!positionIsInBounds(endPosition)) {
            return;
        }

        int startCol = startPosition.getColumn();
        int endCol = endPosition.getColumn();

        if (startCol == endCol && !isPawnBlocked(board, endPosition)) {
            if (shouldPromote) {
                addPromotionMoves(startPosition, endPosition, moves);
            } else {
                moves.add(new ChessMove(startPosition, endPosition, null));
            }
        }

        if (startCol != endCol && willCapturePiece(board, endPosition, myPiece)) {
            if (shouldPromote) {
                addPromotionMoves(startPosition, endPosition, moves);
            } else {
                moves.add(new ChessMove(startPosition, endPosition, null));
            }
        }
    }

    private void addPromotionMoves(ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
    }

    private boolean shouldPromotePiece(ChessGame.TeamColor color, int startRow) {
        return color == ChessGame.TeamColor.WHITE ? startRow == 7 : startRow == 2;
    }

    private boolean isPawnBlocked(ChessBoard board, ChessPosition endPosition) {
        return board.getPiece(endPosition) != null;
    }

    private boolean positionIsInBounds(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row <= 8 && row >= 1 && col <= 8 && col >= 1;
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

