package com.gregbender.memorymatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by u0049913 on 8/24/2017.
 */

public class GameBoard {
    List<Card> allCards = new ArrayList<Card>();
    boolean gameStarted = false;

    public GameBoard() {
        EventBus.getDefault().register(this);
    }
    public void addCard(Card card) {
        this.allCards.add(card);
    }
    public void shuffleUpAndStart() {
        Collections.shuffle(allCards);
        this.gameStarted = true;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
    public int getCardsFaceUpCount() {
        int count = 0;
        for (Card i : allCards) {
            if (i.isFaceUp()) {
                count++;
            }
        }
        return count;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkForMatch(MatchCheckEvent event) {

        if (event.message.equals("2")) {

            Card first = null;
            Card second = null;

            for (Card i : allCards) {
                if (i.isFaceUp()) {

                    if (first == null) {
                        first = i;
                    }
                    else if (second == null) {
                        second = i;
                    }

                    i.flip();
                }
            }

            if (first.isMatch(second)) {
                first.markAsMatch();
                second.markAsMatch();
                //soundCorrectSelection.start();
            }
            else {
                first.flipDown();
                second.flipDown();
            }
        }
    }
}
