package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard currentBoard;
    public String winner;

    public ChessGame() {
        currentBoard = new ChessBoard();
        currentBoard.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Get the data we need
        ChessPiece currentPiece = currentBoard.getPiece(startPosition);
        TeamColor currentColor = currentPiece.getTeamColor();
        Collection<ChessMove> moves = currentPiece.pieceMoves(currentBoard, startPosition);
        Collection<ChessMove> valMoves = new ArrayList<>();

        // Deep Copy
        ChessBoard testBoard = new ChessBoard(currentBoard);

        // Test the move
        for (ChessMove move : moves) {
            testBoard.makeMove(move);
            if (!testBoard.isInCheck(currentColor)){
                valMoves.add(move);
            }
            testBoard = new ChessBoard(currentBoard);
        }

        return valMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece currentPiece = currentBoard.getPiece(move.getStartPosition());
        if (currentPiece == null) {
            throw new InvalidMoveException();
        }

        if (currentPiece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException();
        }

        Collection<ChessMove> allValidMoves = validMoves(move.getStartPosition());
        if (!allValidMoves.contains(move)) {
            throw new InvalidMoveException();
        }

        currentBoard.makeMove(move);


        if (teamTurn == TeamColor.BLACK) {
            if (isInCheckmate(TeamColor.WHITE)) {
                this.winner = "Black";
            }

            if (isInStalemate(TeamColor.WHITE)) {
                this.winner = "None";
            }

            teamTurn = TeamColor.WHITE;
        } else {
            if (isInCheckmate(TeamColor.BLACK)) {
                this.winner = "White";
            }

            if (isInStalemate(TeamColor.BLACK)) {
                this.winner = "None";
            }
            teamTurn = TeamColor.BLACK;
        }
    }


    public void resign(TeamColor color) {
        if (color == TeamColor.WHITE) {
            this.winner = "Black";
        } else {
            this.winner = "White";
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return currentBoard.isInCheck(teamColor);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!currentBoard.isInCheck(teamColor)) {
            return false;
        }

        return hasNoAvailableMoves(teamColor);
    }

    private boolean hasNoAvailableMoves(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <=8; j++) {
                ChessPosition positionToTest = new ChessPosition(i, j);
                ChessPiece pieceToTest = currentBoard.getPiece(positionToTest);
                if (pieceToTest == null) {
                    continue;
                }

                if (pieceToTest.getTeamColor() != teamColor) {
                    continue;
                }

                if (!validMoves(positionToTest).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (currentBoard.isInCheck(teamColor)) {
            return false;
        }

        return hasNoAvailableMoves(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, currentBoard);
    }
}
