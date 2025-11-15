package service.models;

import chess.ChessGame;

public record JoinGameRequest(String playerColor, int gameID) { }
