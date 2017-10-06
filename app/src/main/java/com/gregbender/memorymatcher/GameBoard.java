package com.gregbender.memorymatcher;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard {
    List<Card> allCards = new ArrayList<Card>();
    boolean gameStarted = false;
    boolean playersTurn = false;
    private String moves = "1";
    String word;
    String currentSelection = null;

    public GameBoard(Context view, String word) {
        this.word = word;
        EventBus.getDefault().register(this);

        for (int i = 0; i < this.word.length(); i++){
            char c = this.word.charAt(i);
            this.addCard(new Card(view, Character.toString(c)));
        }
        this.shuffleUp();
        playersTurn = true;
    }

    public void addCard(Card card) {
        this.allCards.add(card);
    }

    public void shuffleUp() {
        this.allFaceDown();
        Collections.shuffle(allCards);

        this.moves = "2";
        setMoves("0");
    }

    public void allFaceDown() {
        for (Card i : allCards) {
            if (i.isFaceUp()) {
                i.flip();
            }
        }
        this.currentSelection = null;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gameBoardEventHandler(GameEvent event) {

        if (event.type == GameEventType.CARD_CLICK) {
            // ignore clicks on cards that are up
            Card clickedCard = (Card)event.eventDetails;

            if (playersTurn && clickedCard.isFaceDown()) {

                //setMoves(String.valueOf(Integer.valueOf(moves) + 1));
                setMoves("asdfasdf");
                this.moves = "asdf";
                // don't let the player click again until move is processed
                playersTurn = false;

                if (!this.isGameStarted()) {
                    this.gameStarted = true;
                    EventBus.getDefault().post(new GameEvent(GameEventType.GAME_START));
                }

                clickedCard.flip();

                if (currentSelection == null) {
                    currentSelection = clickedCard.letter;
                    EventBus.getDefault().post(new GameEvent(GameEventType.END_OF_TURN));
                }
                else if (currentSelection != null) {

                    currentSelection = currentSelection + clickedCard.letter;

                    if (word.equals(currentSelection)) {
                        EventBus.getDefault().post(new GameEvent(GameEventType.FINISHED));
                    }
                    else if (!word.startsWith(currentSelection)) {
                        EventBus.getDefault().post(new GameEvent(GameEventType.BAD_MOVE));
                    }
                    else {
                        EventBus.getDefault().post(new GameEvent(GameEventType.END_OF_TURN));
                    }
                }
            }
        }
        else if (event.type == GameEventType.ALL_FACE_DOWN) {
            this.allFaceDown();
            this.currentSelection = null;
        }
        else if (event.type == GameEventType.END_OF_TURN) {
            playersTurn = true;
        }
    }
    public String getMoves() {
        return this.moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }
}