package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class HandEvaluator {
    private HandEvaluator(){

    }
    public static Integer getRankingToInt(PlayerInterface p) {
        return p.getRankingEnum().ordinal();
    }
    private Card[] hand = new Card[2];

    public Comparator<Card> byRank = (Card left, Card right) -> {
        if (left.getRank().getValue() < right.getRank().getValue()) {
            return -1;
        } else {
            return 1;
        }
    };

    //move these to Player????
    public Hand() {
    }

    public Hand(Card[] hand) {
        this.hand = hand;
    }


    public Card[] getHand() {
        return hand;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    public void printCardsInHand() {
        for (Card i : hand) {
            System.out.println(i);
        }
    }
    //not my favorite method of determining rank, may rework soon
    public HandRank sortRanks(Card[] flop) {
        if (RoyalFlush(flop)) {
            return HandRank.ROYAL_FLUSH;
        }
        else if (StraightFlush(flop)) {
            return HandRank.STRAIGHT_FLUSH;
        }
        else if (FourOfAKind(flop)) {
            return HandRank.FOUR_OF_A_KIND;
        }
        else if (FullHouse(flop)) {
            return HandRank.FULL_HOUSE;
        }
        else if (Flush(flop)) {
            return HandRank.FLUSH;
        }
        else if (Straight(flop)) {
            return HandRank.STRAIGHT;
        }
        else if (ThreeOfAKind(flop)) {
            return HandRank.THREE_OF_A_KIND;
        }
        else if (TwoPair(flop)) {
            return HandRank.TWO_PAIR;
        }
        else if (Pair(flop)) {
            return HandRank.ONE_PAIR;
        }
        else {
            return HandRank.HIGH_CARD;
        }

    }

    public boolean RoyalFlush(Card[] flop) {
        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);

        if (Straight(allCards) && Flush(allCards)) { //must be a straight and flop to have royal flush
            //check for the proper cards
            boolean ace = false;
            boolean king = false;
            boolean queen = false;
            boolean jack = false;
            boolean ten = false;

            for (Card c : allCards) {
                switch (c.getRank()) {
                    case "Ace":
                        ace = true;
                        break;
                    case "King":
                        king = true;
                        break;
                    case "Queen":
                        queen = true;
                        break;
                    case "Jack":
                        jack = true;
                        break;
                    case "10":
                        ten = true;
                        break;

                }
            }
            return (ace && king && queen && jack && ten);
        }
        else {
            return false;
        }
    }

    public boolean Straight(Card[] flop) {
        int consecutiveCards = 0;
        int pos = 0;

        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);
        Arrays.sort(allCards, byRank);


        while (pos < allCards.length - 1 && !Straight) {
            if (allCards[pos + 1].getRank().getValue() - allCards[pos].getRank().getValue() == 1) {
                consecutiveCards++;
                if (consecutiveCards == 4) {
                    return true;
                } else {
                    pos++;
                }
            } else {
                consecutiveCards = 0;
                pos++;
            }
        }
        return false;
    }

    public boolean Flush(Card[] flop) {
        int clubs = 0;
        int spades = 0;
        int hearts = 0;
        int diamonds = 0;

        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);

        //preferably rework into if statement logic
        for (Card c : allCards) {
            switch (c.getSuit()) {
                case "HEART":
                    hearts++;
                    break;
                case "SPADES":
                    spades++;
                    break;
                case "CLUBS":
                    clubs++;
                    break;
                case "DIAMONDS":
                    diamonds++;
                    break;
            }
        }
        //return at least 5 match suits
        return (clubs >= 5 || spades >= 5 || hearts >= 5 || diamonds >= 5);
    }

    public boolean FourOfAKind(Card[] flop) {
        int repeats = 1; //single instance of card
        int i = 0;
        int k = 1;

        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);


        while (i < allCards.length) {
            repeats = 1;
            while (k < allCards.length) {
                if (allCards[i].getRank().getValue() == allCards[k].getRank().getValue()) {
                    repeats++;
                    if (repeats == 4) {
                        return true;
                    }
                }
                k++;
            }
            i++;
        }
        return false;
    }

    private boolean ThreeOfAKind(Card[] flop) {
        int repeats = 1; //single instance of card
        int i = 0;
        int k = 1;

        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);

        while (i < allCards.length) {
            repeats = 1;
            while (k < allCards.length) {
                if (allCards[i].getRank().getValue() == allCards[k].getRank().getValue()) {
                    repeats++;
                    if (repeats == 3) {
                        return true;
                    }
                }
                k++;
            }
            i++;
        }
        return false;
    }

    private boolean TwoPair(Card[] flop) {
        int repeats = 1;
        int pairs = 0;
        int i = 0;
        int k = i + 1;

        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);

        while (i < allCards.length) {
            repeats = 1;
            while (k < allCards.length) {
                if (allCards[i].getRank().getValue() == allCards[k].getRank().getValue()) {
                    repeats++;
                    if (repeats == 2) {
                        pairs = 1;
                        repeats = 1;
                        if (pairs == 2) {
                            return true;
                        }
                    }
                    k++;
                }
                i++;
            }
            return false;
        }
    }

    private boolean Pair(Card[] flop) {
        int repeats = 1; //single instance of card
        int i = 0;
        int k = 1;

        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);

        while (i < allCards.length) {
            repeats = 1;
            while (k < allCards.length) {
                if (allCards[i].getRank().getValue() == allCards[k].getRank().getValue()) {
                    repeats++;
                    if (repeats == 2) {
                        return true;
                    }
                }
                k++;
            }
            i++;
        }
        return false;
    }


    private boolean FullHouse(Card[] flop) {
        Card[] allCards = Stream.concat(Arrays.stream(flop), Arrays.stream(hand)).toArray(Card[]::new);
        Arrays.sort(allCards, byRank);

        if(ThreeOfAKind(allCards) && Pair(allCards)) {
            return true;
        }
        return false;

    }


    private boolean StraightFlush(Card[] flop) {
        if (Flush(flop) && Straight(flop)) {
            return true;
        }
        return false;
    }

    //need high card implementations still.
}

