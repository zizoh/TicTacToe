package com.example.android.tictactoe;

public interface BoardContract {

    interface View extends BaseView<Presenter> {

        void initBoard();

        void showPlayerXScore(int score);

        void showPlayerOScore(int score);

        void enableAllBoxes(boolean enableBoxes);

        void showPlayerWithTurn(Boolean playerXTurn);

        void showMoveByPlayer(int row, int column, int player);

        void showPlayerXWins();

        void showPlayerOWins();

        void showGameOver();

        void showGameDraw();

    }

    interface Presenter extends BasePresenter {

        void setBoardSize(int boardSize);

        void setGameMode(int gameMode);

        void setPlayerXScore();

        void setPlayerOScore();

        void setPlayerWithTurn(Boolean playerXTurn);

        void setMoveByPlayerAt(int row, int column);

        void restartGame();

    }
}
