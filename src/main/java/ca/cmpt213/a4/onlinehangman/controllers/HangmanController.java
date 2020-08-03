package ca.cmpt213.a4.onlinehangman.controllers;

import ca.cmpt213.a4.onlinehangman.model.GameInfo;
import ca.cmpt213.a4.onlinehangman.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class HangmanController {
    private Message promptMessage; //a resusable String object to display a prompt message at the screen
    private AtomicLong nextGameNumber = new AtomicLong();
    private GameInfo gameInfo = null;
    private List<GameInfo> gameInfoList = new ArrayList<>();
    private List<String> stringList  = new ArrayList<>();;

    // https://stackoverflow.com/a/16100219
    public void getWords() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("commonWords.txt"));
        String str;

        while((str = in.readLine()) != null){
            stringList.add(str);
        }
    }

    //works like a constructor, but wait until dependency injection is done, so it's more like a setup
    @PostConstruct
    public void hangmanControllerInit() throws IOException {
        promptMessage = new Message("Initializing...");
        getWords();
    }

    @GetMapping("/helloworld")
    public String showHelloworldPage(Model model) {
        promptMessage.setMessage("You are at the helloworld page!");
        model.addAttribute("promptMessage", promptMessage);
        // take the user to helloworld.html
        return "helloworld";
    }

    @GetMapping("/welcome")
    public String showWelcomePage(Model model) {
        gameInfo = null;

        System.out.println(stringList);
        // take the user to welcome.html
        return "welcome";
    }

    @PostMapping("/game")
    public String submitForm(@ModelAttribute("gameInfo") GameInfo info, Model model) {

        if (gameInfo == null) {
            gameInfo = new GameInfo(0, 0, 0, "", GameInfo.Status.Active, "");
            gameInfo.setGameNumber(nextGameNumber.incrementAndGet());
            // add word
            Random rand = new Random();
            String randomElement = stringList.get(rand.nextInt(stringList.size()));
            gameInfo.setWord(randomElement);
            // make dashed string
            String s = "";
            for (int i = 0; i < randomElement.length(); i++) {
                s += "_";
            }
            gameInfo.setHiddenWord(s);

            model.addAttribute("gameInfo", gameInfo);
            gameInfoList.add(gameInfo);

        } else {

            if (info.getCurrentGuess() == "") {
                model.addAttribute("gameInfo", gameInfo);
                gameInfoList.set((int)gameInfo.getGameNumber() - 1, gameInfo);
                return "game";
            }

            gameInfo.incrementGuesses();
            gameInfo.setCurrentGuess(info.getCurrentGuess());

            // find locations of guessed letter
            List<Integer> characterLocations = new ArrayList<>();
            String s = gameInfo.getWord();
            for (int i = 0; i < s.length(); i++){
                if(gameInfo.getCurrentGuess().charAt(0) == s.charAt(i)) {
                    characterLocations.add(i);
                }
            }

            // increment incorrect guess if true
            if (characterLocations.size() == 0){
                gameInfo.incrementIncorrectGuesses();
            } else {
                // reveal letters
                for (int i = 0; i < characterLocations.size(); i++) {
                    StringBuilder sb = new StringBuilder(gameInfo.getBlankHiddenWord());
                    sb.setCharAt(characterLocations.get(i), gameInfo.getCurrentGuess().charAt(0));
                    gameInfo.setHiddenWord(sb.toString());
                }
            }

            // check for win
            if (!gameInfo.getBlankHiddenWord().contains("_")) {
                gameInfo.setStatus(GameInfo.Status.Won);
                model.addAttribute("gameInfo", gameInfo);
                gameInfoList.set((int)gameInfo.getGameNumber() - 1, gameInfo);
                return "gameover";
            }
            // check for loss
            if (gameInfo.getIncorrectGuesses() == 7)  {
                gameInfo.setStatus(GameInfo.Status.Lost);
                model.addAttribute("gameInfo", gameInfo);
                gameInfoList.set((int)gameInfo.getGameNumber() - 1, gameInfo);
                return "gameover";
            }

            model.addAttribute("gameInfo", gameInfo);
            gameInfoList.set((int)gameInfo.getGameNumber() - 1, gameInfo);
        }

        System.out.println(gameInfo);

        // take the user to game.html
        return "game";
    }

    // list selected game {id}
    @GetMapping("/game/{id}")
    public String showGamePage(@PathVariable("id") long id, Model model) {

        // if id < 1 send error
        gameInfo = null;

        for (GameInfo game : gameInfoList) {
            if (game.getGameNumber() == id) {
                gameInfo = game;
            }
        }

        if (gameInfo == null) {
            // game not found error
            return badValueExceptionHandler();

        } else {
            // check for win
            if (!gameInfo.getBlankHiddenWord().contains("_")) {
                gameInfo.setStatus(GameInfo.Status.Won);
                model.addAttribute(gameInfo);
                return "gameover";
            }
            // check for loss
            if (gameInfo.getIncorrectGuesses() == 7) {
                gameInfo.setStatus(GameInfo.Status.Lost);
                model.addAttribute(gameInfo);
                return "gameover";
            }

            model.addAttribute("gameInfo", gameInfo);

            // take the user to game.html
            return "game";
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "Invalid values.")
    @ExceptionHandler(GameNotFoundException.class)
    public String badValueExceptionHandler() {
        // do nothing
        return "gamenotfound";
    }

}