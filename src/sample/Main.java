package sample;

import javafx.animation.ScaleTransition;            // used for the execution of cards flips.
import javafx.animation.TranslateTransition;        // used for the execution of cards movement.
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;                          // used to create Scene objects.
import javafx.scene.control.Button;                 // used to create button objects.
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * GUI implementation of the In Between Game of Cards.
 * <p>
 * @author Angel Peralta
 * @since 25APR2019 12:40 EST
 */
public class Main extends Application
{
    private Stage window;                       // holds the main game.
    private int risk;                           // the risk taken by the user.
    private Stake stake;                        // represents the stake state as the game unveils.
    private ImageView[] backOfCards = new ImageView[52];      // holds the 52 cards representing the back of each card.
    private TranslateTransition[] transition = new TranslateTransition[52];     // holds the 52 cards transitions.
    private int cardToMove = 0;                 // represents the array-index of the cards that is to be moved.
    private CardDeck cardDeck;                  // represents the deck of cards.
    private Pane pane;                          // represents the center layout inside the BorderPane.
    private Label stakeLabel;                   // shows the state of the stake.
    private Label betLabel;                     // shows the state of the risk.
    private Label handLabel;                    // shows the hand state.
    private Label wonHandsLabel;                // shows the state of won hands.
    private Label lostHandsLabel;               // shows the state of lost hands.
    private GridPane bottomGrid;                // represents the bottom layout inside the BorderPane.
    private int handNum = 1;                    // number of current hand.
    private int wonHandsNum = 0;                // number of won hands.
    private int lostHandsNum = 0;               // number of lost hands.
    private int yVa2 = -70;                     // y-coordinate of moving card.
    private int yVal3 = 175;                    // y-coordinate of moving card

    @Override
    public void start(Stage primaryStage)
    {
        window = primaryStage;
        window.setTitle("In Between Game of Cards");

        bottomGrid = new GridPane();
        bottomGrid.setPadding(new Insets(10, 10, 10, 10));
        bottomGrid.setVgap(10);
        bottomGrid.setHgap(10);

        Label riskLabel = new Label("Risk: ");
        GridPane.setConstraints(riskLabel, 0, 0);

        TextField riskInput = new TextField();
        GridPane.setConstraints(riskInput, 1, 0);

        Button playButton = new Button("Play");
        playButton.setOnAction(e ->
        {
            stake.setBettedChips(isInt(riskInput));         // setting risk equal to the value the user entered
                                                            // in the text field.
            // removing children to re-set their values and add them back into the layout.
            bottomGrid.getChildren().removeAll(betLabel, stakeLabel);

            stakeLabel = new Label("Chips on Stake: " + stake.getTotalChips());
            GridPane.setConstraints(stakeLabel, 15, 0);

            betLabel = new Label("Chips at risk: " + risk);
            GridPane.setConstraints(betLabel, 15, 1);

            bottomGrid.getChildren().addAll(betLabel, stakeLabel);
            //////////////////////////////////////////////////////////////////////////////
            playCards();        // playing card animation and comparison between given cards.
        });
        GridPane.setConstraints(playButton, 1, 1);

        bottomGrid.getChildren().addAll(riskLabel, riskInput, playButton);

        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(
                            Color.rgb(40, 40, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        Image img;
        int yVal1 = 150;
        // setting the deck of cards in a stack kind of 3d way.
        for (int index = 0; index < 52; index ++)
        {

            img = new Image("BackOfCard.png");
            backOfCards[index] = new ImageView(img);
            backOfCards[index].setX(325);
            backOfCards[index].setY(yVal1);
            pane.getChildren().add(backOfCards[index]);
            yVal1 -= 1;
        }
        // setting the layouts inside the BorderPane layout.
        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(bottomGrid);
        borderPane.setCenter(pane);

        Scene scene = new Scene(borderPane, 800, 650);
        window.setScene(scene);
        window.show();

        boolean wannaPlay = ConfirmBox.display("In Between Game of Cards", "Ready to start playing?");

        if (!wannaPlay)
        {
            closeProgram();
        }

        stake = new StakeBox().setStake();
        stakeLabel = new Label("Chips on Stake: " + stake.getTotalChips());
        GridPane.setConstraints(stakeLabel, 15, 0);

        betLabel = new Label("Chips at risk: " + risk);
        GridPane.setConstraints(betLabel, 15, 1);

        handLabel = new Label("Hand #" + handNum);
        GridPane.setConstraints(handLabel, 20, 0);

        wonHandsLabel = new Label("Won hands: " + wonHandsNum);
        GridPane.setConstraints(wonHandsLabel, 30, 0);

        lostHandsLabel = new Label("Lost hands: " + lostHandsNum);
        GridPane.setConstraints(lostHandsLabel, 30, 1);

        bottomGrid.getChildren().addAll(stakeLabel, betLabel, handLabel, wonHandsLabel, lostHandsLabel);

        cardDeck = new CardDeck();
        cardDeck.shuffle();

        double time = 1;

        // moving the entire deck of cards to the top left corner of the window.
        for (int index = 0; index < 52; index ++)
        {
            transition[index] = new TranslateTransition();
            transition[index].setDuration(Duration.seconds(time));
            transition[index].setToX(-300);
            transition[index].setToY(-75);
            transition[index].setNode(backOfCards[index]);
            transition[index].play();
            time += 0.1;
        }
    }

    /**
     * Prompts user to decide whether he or she wants to play or exit the game.
     */
    private void closeProgram()
    {
        boolean answer = ConfirmBox.display("In Between Game of Cards", "Sure you want to exit?");

        if (answer)
        {
            window.close();
            System.exit(0);
        }
    }

    /**
     * If the given parameter is an integer, returns it, otherwise returns 0.
     * @param input the value to check.
     * @return the value if it is an integer, otherwise returns 0.
     */
    private int isInt(TextField input)
    {
        try
        {
            risk = Integer.parseInt(input.getText());
            input.setStyle("-fx-text-inner-color: black;");
            return risk;
        }
        catch (NumberFormatException e)
        {
            input.setStyle("-fx-text-inner-color: red;");
        }
        return 0;
    }

    /**
     * Executes every time the user plays a risk, moves the cards and compares them to
     * determine whether the hand is lost or not.
     */
    private void playCards() {

        Card card1 = cardDeck.nextCard();   cardDeck.removeCard();
        Card card2 = cardDeck.nextCard();   cardDeck.removeCard();
        Card card3 = cardDeck.nextCard();   cardDeck.removeCard();

        ImageView firstCard = new ImageView(new Image(card1.getImage()));
        firstCard.setScaleX(0);
        firstCard.setX(125);
        firstCard.setY(322);

        ImageView secondCard = new ImageView(new Image(card2.getImage()));
        secondCard.setScaleX(0);
        secondCard.setX(525);
        secondCard.setY(322);

        ImageView thirdCard = new ImageView(new Image(card3.getImage()));
        thirdCard.setScaleX(0);
        thirdCard.setX(325);
        thirdCard.setY(322);

        pane.getChildren().addAll(firstCard, secondCard, thirdCard);

        transition[cardToMove].setDuration(Duration.seconds(1));
        transition[cardToMove].setToX(-200);
        transition[cardToMove].setToY(yVal3);
        transition[cardToMove].setNode(backOfCards[cardToMove]);
        transition[cardToMove].play();

        ScaleTransition hideFirst = new ScaleTransition(Duration.millis(1500), backOfCards[cardToMove]);
        hideFirst.setFromX(1);
        hideFirst.setToX(0);

        ScaleTransition showFirst = new ScaleTransition(Duration.millis(1500), firstCard);
        showFirst.setFromX(0);
        showFirst.setToX(1);

        hideFirst.setOnFinished(e -> showFirst.play());

        hideFirst.play();

        cardToMove ++;

        transition[cardToMove].setDuration(Duration.seconds(1));
        transition[cardToMove].setToX(200);
        transition[cardToMove].setToY(yVal3);
        transition[cardToMove].setNode(backOfCards[cardToMove]);
        transition[cardToMove].play();

        ScaleTransition hideSecond = new ScaleTransition(Duration.millis(1500), backOfCards[cardToMove]);
        hideSecond.setFromX(1);
        hideSecond.setToX(0);

        ScaleTransition showSecond = new ScaleTransition(Duration.millis(1500), secondCard);
        showSecond.setFromX(0);
        showSecond.setToX(1);

        hideSecond.setOnFinished(e -> showSecond.play());

        hideSecond.play();

        cardToMove ++;

        transition[cardToMove].setDuration(Duration.seconds(1));
        transition[cardToMove].setToX(0);
        transition[cardToMove].setToY(yVal3);
        transition[cardToMove].setNode(backOfCards[cardToMove]);
        transition[cardToMove].play();

        ScaleTransition hideThird = new ScaleTransition(Duration.millis(1500), backOfCards[cardToMove]);
        hideThird.setFromX(1);
        hideThird.setToX(0);

        ScaleTransition showThird = new ScaleTransition(Duration.millis(1500), thirdCard);
        showThird.setFromX(0);
        showThird.setToX(1);

        hideThird.setOnFinished(e -> showThird.play());

        boolean doubleRisk = ConfirmBox.display("Question",
                                                "Do you feel confident enough\nto double the risk?");
        if (doubleRisk)
        {
            risk *= 2;
            stake.doubleBettedChips();

            bottomGrid.getChildren().removeAll(betLabel, stakeLabel);

            stakeLabel = new Label("Chips on Stake: " + stake.getTotalChips());
            GridPane.setConstraints(stakeLabel, 15, 0);

            betLabel = new Label("Chips at risk: " + risk);
            GridPane.setConstraints(betLabel, 15, 1);

            bottomGrid.getChildren().addAll(betLabel, stakeLabel);
        }
        hideThird.play();

        cardToMove ++;

        if ((card1.compareTo(card3) < 0 && card2.compareTo(card3) > 0) || (card1.compareTo(card3) > 0 && card2.compareTo(card3) < 0))
        {
            stake.increaseTotalChips();

            wonHandsNum ++;

            bottomGrid.getChildren().remove(wonHandsLabel);

            wonHandsLabel = new Label("Won hands: " + wonHandsNum);
            GridPane.setConstraints(wonHandsLabel, 30, 0);

            bottomGrid.getChildren().add(wonHandsLabel);
        }
        else
        {
            if (card1.compareTo(card2) == 0)
            {
                stake.tieHand();
            }
            stake.decreaseTotalChips();

            lostHandsNum ++;

            bottomGrid.getChildren().remove(lostHandsLabel);

            lostHandsLabel = new Label("Lost hands: " + lostHandsNum);
            GridPane.setConstraints(lostHandsLabel, 30, 1);

            bottomGrid.getChildren().add(lostHandsLabel);
        }

        showThird.setOnFinished(actionEvent ->
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException ignored) {}

            ScaleTransition showFirst2 = new ScaleTransition(Duration.millis(1500), firstCard);
            showFirst2.setFromX(1);
            showFirst2.setToX(0);

            ScaleTransition hideFirst2 = new ScaleTransition(Duration.millis(1500), backOfCards[cardToMove - 3]);
            hideFirst2.setFromX(0);
            hideFirst2.setToX(1);

            showFirst2.setOnFinished(e -> hideFirst2.play());

            showFirst2.play();


            ScaleTransition showSecond2 = new ScaleTransition(Duration.millis(1500), secondCard);
            showSecond2.setFromX(1);
            showSecond2.setToX(0);

            ScaleTransition hideSecond2 = new ScaleTransition(Duration.millis(1500), backOfCards[cardToMove - 2]);
            hideSecond2.setFromX(0);
            hideSecond2.setToX(1);

            showSecond2.setOnFinished(e -> hideSecond2.play());

            showSecond2.play();


            ScaleTransition showThird2 = new ScaleTransition(Duration.millis(1500), thirdCard);
            showThird2.setFromX(1);
            showThird2.setToX(0);

            ScaleTransition hideThird2 = new ScaleTransition(Duration.millis(1500), backOfCards[cardToMove - 1]);
            hideThird2.setFromX(0);
            hideThird2.setToX(1);

            showThird2.setOnFinished(e -> hideThird2.play());

            showThird2.play();

            hideFirst2.setOnFinished(e ->
            {
                transition[cardToMove - 3].setDuration(Duration.seconds(1));
                transition[cardToMove - 3].setToX(292);
                transition[cardToMove - 3].setToY(yVa2);
                transition[cardToMove - 3].setNode(backOfCards[cardToMove - 3]);
                transition[cardToMove - 3].play();

                yVa2 --;
            });

            hideSecond2.setOnFinished(e ->
            {
                transition[cardToMove - 2].setDuration(Duration.seconds(1));
                transition[cardToMove - 2].setToX(292);
                transition[cardToMove - 2].setToY(yVa2);
                transition[cardToMove - 2].setNode(backOfCards[cardToMove - 2]);
                transition[cardToMove - 2].play();

                yVa2 --;
            });

            hideThird2.setOnFinished(e ->
            {
                transition[cardToMove - 1].setDuration(Duration.seconds(1));
                transition[cardToMove - 1].setToX(292);
                transition[cardToMove - 1].setToY(yVa2);
                transition[cardToMove - 1].setNode(backOfCards[cardToMove - 1]);
                transition[cardToMove - 1].play();

                yVa2 --;
            });
        });

        handNum ++;

        bottomGrid.getChildren().removeAll(handLabel, stakeLabel);

        stakeLabel = new Label("Chips on Stake: " + stake.getTotalChips());
        GridPane.setConstraints(stakeLabel, 15, 0);

        handLabel = new Label("Hand #" + handNum);
        GridPane.setConstraints(handLabel, 20, 0);

        bottomGrid.getChildren().addAll(handLabel, stakeLabel);

        if (cardDeck.deckSize() < 3 || !stake.hasChips())
        {
            endGame();
        }
        yVal3 += 2;
    }

    /**
     * Lets the user know the game has ended and closes it.
     */
    private void endGame()
    {
        AlertBox.display("In Between Game of Cards", "The game has ended.");
        window.close();
        System.exit(0);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
