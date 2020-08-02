package ca.cmpt213.a4.onlinehangman.controllers;

import ca.cmpt213.a4.onlinehangman.model.GameInfo;
import ca.cmpt213.a4.onlinehangman.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class HangmanController {
    private Message promptMessage; //a resusable String object to display a prompt message at the screen
    private AtomicLong nextGameNumber = new AtomicLong();
    private GameInfo gameInfo = new GameInfo();


    //works like a constructor, but wait until dependency injection is done, so it's more like a setup
    @PostConstruct
    public void hangmanControllerInit() {
        promptMessage = new Message("Initializing...");
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
        gameInfo = new GameInfo();
        gameInfo.setGameNumber(nextGameNumber.incrementAndGet());

        // take the user to welcome.html
        return "welcome";
    }

    @GetMapping("/game")
    public String showGamePage(Model model) {
        model.addAttribute("gameInfo", gameInfo);



        // take the user to game.html
        return "game";
    }

    @PostMapping("/game")
    public String submitForm(@ModelAttribute("gameInfo") GameInfo info) {

        gameInfo.setCurrentGuess(info.getCurrentGuess());
        gameInfo.incrementGuesses();
        System.out.println(gameInfo);

        // take the user to game.html
        return "game";
    }
}