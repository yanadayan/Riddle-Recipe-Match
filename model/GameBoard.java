package application.model;

import java.util.*;
import javafx.scene.image.Image;

public class GameBoard {
    // Cards data structures
    private ArrayList<Card> arrayListCards;
    private LinkedList<Card> linkedListCards;
    private HashMap<String, String> pairsMap;
    private List<Card> cards;
    private LinkedHashMap<String, String> possiblePairs;

    private int errorCount;
    private int matchesFound;
    private int totalMatches;

   

    // All level definitions
    private static final List<LinkedHashMap<String, String>> LEVEL_PAIRS = List.of(
        new LinkedHashMap<>(Map.of(
            "stickyRice", "bananaLeaf",
            "riceflour", "cheese",
            "pot", "saltedegg"
        )),
        new LinkedHashMap<>(Map.of(
            "stickyRice", "bananaLeaf",
            "riceflour", "cheese",
            "pot", "saltedegg",
            "dough", "sesame"
        )),
        new LinkedHashMap<>(Map.of(
            "glutinousrice", "latik",
            "stickyRice", "bananaLeaf",
            "riceflour", "cheese",
            "pot", "saltedegg",
            "dough", "sesame",
            "coconut", "cornkernels"
        )),
        new LinkedHashMap<>(Map.of(
            "glutinousrice", "latik",
            "stickyRice", "bananaLeaf",
            "riceflour", "cheese",
            "pot", "saltedegg",
            "dough", "sesame",
            "coconut", "cornkernels",
            "sweetcorn", "milk",
            "purplerice", "muscuvado"
        )),
        new LinkedHashMap<>(Map.of(
            "glutinousrice", "latik",
            "stickyRice", "bananaLeaf",
            "riceflour", "cheese",
            "pot", "saltedegg",
            "dough", "sesame",
            "coconut", "cornkernels",
            "sweetcorn", "milk",
            "purplerice", "muscuvado",
            "chocolate", "swirledlog",
            "banana", "brownsugar"
        ))
    );

    public GameBoard(int level) {
        this.arrayListCards = new ArrayList<>();
        this.linkedListCards = new LinkedList<>();
        this.pairsMap = new HashMap<>();
        this.cards = new ArrayList<>();
        this.possiblePairs = new LinkedHashMap<>();
        this.errorCount = 0;
        this.matchesFound = 0;
        if (level < 1) level = 1;
        if (level > LEVEL_PAIRS.size()) level = LEVEL_PAIRS.size();
        this.possiblePairs.putAll(LEVEL_PAIRS.get(level - 1));
        this.pairsMap.putAll(this.possiblePairs); // Copy so we can show HashMap
        this.totalMatches = possiblePairs.size();
    }

    // Setup cards in all structures (for demo only)
    public void setupCards(Class<?> loaderClass) {
        List<String> keys = new ArrayList<>(possiblePairs.keySet());
        Collections.shuffle(keys);
        for (String key : keys) {
            String v1 = key;
            String v2 = possiblePairs.get(key);

            Image img1 = application.util.Utils.loadImage(v1 + ".png", loaderClass);
            Image img2 = application.util.Utils.loadImage(v2 + ".png", loaderClass);
            if (img1 != null && img2 != null) {
                Card c1 = new BasicCard(v1, img1);
                Card c2 = new BasicCard(v2, img2);

                cards.add(c1);           // Main game logic uses this
                cards.add(c2);
                arrayListCards.add(c1);  // Show ArrayList
                linkedListCards.add(c2); // Show LinkedList
            }
        }
        Collections.shuffle(cards);
    }

    public boolean checkMatch(int idx1, int idx2) {
        String name1 = cards.get(idx1).getName();
        String name2 = cards.get(idx2).getName();

        boolean isMatch = false;
        if (pairsMap.containsKey(name1) && pairsMap.get(name1).equals(name2))
            isMatch = true;
        else if (pairsMap.containsKey(name2) && pairsMap.get(name2).equals(name1))
            isMatch = true;

        if (isMatch) {
            cards.get(idx1).setMatched(true);
            cards.get(idx2).setMatched(true);
            matchesFound++;
        } else {
            errorCount++;
        }
        return isMatch;
    }

    public void incrementErrorCount() {
        this.errorCount++;
    }

    // Getters to demonstrate structure usage
    public List<Card> getCards() { return cards; }
    public ArrayList<Card> getArrayListCards() { return arrayListCards; }
    public LinkedList<Card> getLinkedListCards() { return linkedListCards; }
    public HashMap<String, String> getPairsMap() { return pairsMap; }



    public int getErrorCount() { return errorCount; }
    public int getMatchesFound() { return matchesFound; }
    public int getTotalMatches() { return totalMatches; }

    public void reset() {
        errorCount = 0;
        matchesFound = 0;
        for (Card c : cards) c.setMatched(false);
    }
}