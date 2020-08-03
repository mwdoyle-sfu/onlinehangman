package ca.cmpt213.a4.onlinehangman.model;

public class GameInfo {

    public enum Status {
        Active,
        Won,
        Lost
    }

    private long gameNumber;
    private int guesses;
    private int incorrectGuesses;
    private String currentGuess;
    private Status status;
    private String word;
    private String hiddenWord;
    private String image = "/images/hangman0.jpg";

    public GameInfo(){
    }

    public GameInfo(long gameNumber, int guesses, int incorrectGuesses, String currentGuess, Status status, String word) {
        this.gameNumber = gameNumber;
        this.guesses = guesses;
        this.incorrectGuesses = incorrectGuesses;
        this.currentGuess = currentGuess;
        this.status = status;
        this.word = word;
    }

    public long getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(long gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getGuesses() {
        return guesses;
    }

    public void setGuesses(int guesses) {
        this.guesses = guesses;
    }

    public void incrementGuesses() { this.guesses++; }

    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public void setIncorrectGuesses(int incorrectGuesses) {
        this.incorrectGuesses = incorrectGuesses;
    }

    public void incrementIncorrectGuesses () { this.incorrectGuesses++; }

    public String getCurrentGuess() { return currentGuess; }

    public void setCurrentGuess(String currentGuess) {
        this.currentGuess = currentGuess;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getWord() { return word; }

    public void setWord(String word) { this.word = word; }

    public String getHiddenWord() {
        return hiddenWord.replace("", " ").trim();
    }

    public String getBlankHiddenWord() {
        return hiddenWord;
    }

    public void setHiddenWord(String hiddenWord) {
        this.hiddenWord = hiddenWord;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "gameNumber=" + gameNumber +
                ", guesses=" + guesses +
                ", incorrectGuesses=" + incorrectGuesses +
                ", currentGuess='" + currentGuess + '\'' +
                ", status=" + status +
                ", word='" + word + '\'' +
                ", hiddenWord='" + hiddenWord + '\'' +
                '}';
    }
}
