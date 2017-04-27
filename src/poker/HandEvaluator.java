package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static poker.Rank.*;
import static poker.HandRank.*;


public class HandEvaluator {
    private HandEvaluator() {

    }

    public static Integer getRankingToInt(PlayerInterface p) {
        return p.getHandRank().ordinal();
    }

    public static void checkRanking(PlayerInterface player, List<Card> tableCards) {

        //get the HIGH_CARD
        Card highCard = getHighCard(player, tableCards);
        player.setHighCard(highCard);

        //check for ROYAL_FLUSH
        List<Card> rankingList = getRoyalFlush(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, ROYAL_FLUSH, rankingList);
            return;
        }
        //check for STRAIGHT_FLUSH
        rankingList = getStraightFlush(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, STRAIGHT_FLUSH, rankingList);
            return;
        }
        //check for FOUR_OF_A_KIND
        rankingList = getFourOfAKind(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, FOUR_OF_A_KIND, rankingList);
            return;
        }
        //check for FULL_HOUSE
        rankingList = getFullHouse(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, FULL_HOUSE, rankingList);
            return;
        }
        //check for FLUSH
        rankingList = getFlush(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, FLUSH, rankingList);
            return;
        }
        //check for STRAIGHT
        rankingList = getStraight(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, STRAIGHT, rankingList);
            return;
        }
        //check for THREE_OF_A_KIND
        rankingList = getThreeOfAKind(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, THREE_OF_A_KIND, rankingList);
            return;
        }
        //check for TWO_PAIR
        rankingList = getTwoPair(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, TWO_PAIR, rankingList);
            return;
        }
        //check for ONE_PAIR
        rankingList = getOnePair(player, tableCards);
        if (rankingList != null) {
            setRankingEnumAndList(player, ONE_PAIR, rankingList);
            return;
        }
        //finish with HIGH_CARD
        player.setHandRank(HIGH_CARD);
        List<Card> highCardRankingList = new ArrayList<Card>();
        highCardRankingList.add(highCard);
        player.setRankingList(highCardRankingList);
        return;

    }

    public static List<Card> getRoyalFlush(PlayerInterface player, List<Card> tableCards) {
        if (!isSameSuit(player, tableCards)) {
            return null;
        }

        List<Rank> rankEnumList = toRankEnumList(player, tableCards);

        if (rankEnumList.contains(C_10) && rankEnumList.contains(JACK) && rankEnumList.contains(QUEEN)
                && rankEnumList.contains(KING) && rankEnumList.contains(ACE)) {
            return getMergedCardList(player, tableCards);
        }
        return null;
    }

    public static List<Card> getStraightFlush(PlayerInterface player, List<Card> tableCards) {
        return getSequence(player, tableCards, 5, true);
    }

    public static List<Card> getFourOfAKind(PlayerInterface player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        return checkPair(mergedList, 4);
    }

    public static List<Card> getFullHouse(PlayerInterface player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        List<Card> threeList = checkPair(mergedList, 3);
        if (threeList != null) {
            mergedList.removeAll(threeList);
            List<Card> twoList = checkPair(mergedList, 2);
            if (twoList != null) {
                threeList.addAll(twoList);
                return threeList;
            }
        }
        return null;
    }

    public static List<Card> getFlush(PlayerInterface player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        List<Card> flushList = new ArrayList<Card>();

        for (Card card1 : mergedList) {
            for (Card card2 : mergedList) {
                if (card1.getSuit().equals(card2.getSuit())) {
                    if (!flushList.contains(card1)) {
                        flushList.add(card1);
                    }
                    if (!flushList.contains(card2)) {
                        flushList.add(card2);
                    }
                }
            }
            if (flushList.size() == 5) {
                return flushList;
            }
            flushList.clear();
        }
        return null;
    }

    public static List<Card> getStraight(PlayerInterface player, List<Card> tableCards) {
        return getSequence(player, tableCards, 5, false);
    }

    public static List<Card> getThreeOfAKind(PlayerInterface player,
                                             List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        return checkPair(mergedList, 3);
    }

    public static List<Card> getTwoPair(PlayerInterface player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        List<Card> twoPair1 = checkPair(mergedList, 2);
        if (twoPair1 != null) {
            mergedList.removeAll(twoPair1);
            List<Card> twoPair2 = checkPair(mergedList, 2);
            if (twoPair2 != null) {
                twoPair1.addAll(twoPair2);
                return twoPair1;
            }
        }
        return null;
    }

    public static List<Card> getOnePair(PlayerInterface player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        return checkPair(mergedList, 2);
    }

    public static Card getHighCard(PlayerInterface player, List<Card> tableCards) {
        List<Card> allCards = new ArrayList<Card>();
        allCards.addAll(tableCards);
        allCards.add(player.getHand()[0]);
        allCards.add(player.getHand()[1]);

        Card highCard = allCards.get(0);
        for (Card card : allCards) {
            if (card.rankInt() > highCard.rankInt()) {
                highCard = card;
            }
        }
        return highCard;
    }

    private static List<Card> getSequence(PlayerInterface player, List<Card> tableCards,
                                          Integer sequenceSize, Boolean compareSuit) {
        List<Card> orderedList = getOrderedCardList(player, tableCards);
        List<Card> sequenceList = new ArrayList<Card>();

        Card cardPrevious = null;
        for (Card card : orderedList) {
            if (cardPrevious != null) {
                if ((card.rankInt() - cardPrevious.rankInt()) == 1) {
                    if (!compareSuit
                            || cardPrevious.getSuit().equals(card.getSuit())) {
                        if (sequenceList.size() == 0) {
                            sequenceList.add(cardPrevious);
                        }
                        sequenceList.add(card);
                    }
                } else {
                    if (sequenceList.size() == sequenceSize) {
                        return sequenceList;
                    }
                    sequenceList.clear();
                }
            }
            cardPrevious = card;
        }

        return (sequenceList.size() == sequenceSize) ? sequenceList : null;
    }

    private static List<Card> getMergedCardList(PlayerInterface player,
                                                List<Card> tableCards) {
        List<Card> merged = new ArrayList<Card>();
        merged.addAll(tableCards);
        merged.add(player.getHand()[0]);
        merged.add(player.getHand()[1]);
        return merged;
    }

    private static List<Card> getOrderedCardList(PlayerInterface player, List<Card> tableCards) {
        List<Card> ordered = getMergedCardList(player, tableCards);
        Collections.sort(ordered, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                return c1.rankInt() < c2.rankInt() ? -1 : 1;
            }
        });
        return ordered;
    }

    private static List<Card> checkPair(List<Card> mergedList, Integer pairSize) {
        List<Card> checkedPair = new ArrayList<Card>();
        for (Card card1 : mergedList) {
            checkedPair.add(card1);
            for (Card card2 : mergedList) {
                if (!card1.equals(card2)
                        && card1.getRank().equals(card2.getRank())) {
                    checkedPair.add(card2);
                }
            }
            if (checkedPair.size() == pairSize) {
                return checkedPair;
            }
            checkedPair.clear();
        }
        return null;
    }

    private static Boolean isSameSuit(PlayerInterface player, List<Card> tableCards) {
        Suit suit = player.getHand()[0].getSuit();

        if (!suit.equals(player.getHand()[1].getSuit())) {
            return false;
        }

        for (Card card : tableCards) {
            if (!card.getSuit().equals(suit)) {
                return false;
            }
        }

        return true;
    }

    private static List<Rank> toRankEnumList(PlayerInterface player, List<Card> tableCards) {
        List<Rank> rankEnumList = new ArrayList<Rank>();

        for (Card card : tableCards) {
            rankEnumList.add(card.getRank());
        }

        rankEnumList.add(player.getHand()[0].getRank());
        rankEnumList.add(player.getHand()[1].getRank());

        return rankEnumList;
    }

    private static void setRankingEnumAndList(PlayerInterface player, HandRank handRank, List<Card> rankingList) {
        player.setHandRank(handRank);
        player.setRankingList(rankingList);
    }
}
