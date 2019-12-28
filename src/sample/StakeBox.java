package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StakeBox
{
    private Stake stake;
    private int numChips = 0;

    public Stake setStake()
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("In between Game of Cards");
        window.setMinWidth(250);
        window.setMinHeight(150);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label label = new Label();
        label.setText("How many chips do you want to start with?");
        GridPane.setConstraints(label, 0, 0);

        TextField inputStake = new TextField();
        GridPane.setConstraints(inputStake, 0,1);

        Button letsPlayButton = new Button("Let's play!");
        letsPlayButton.setOnAction(e -> {
            isInt(inputStake);
            if (numChips != 0)
            {
                stake = new Stake(numChips);
                window.close();
            }
        });
        GridPane.setConstraints(letsPlayButton, 0, 2);

        gridPane.getChildren().addAll(label, inputStake, letsPlayButton);

        Scene scene = new Scene(gridPane);
        window.setScene(scene);
        window.showAndWait();

        return stake;
    }
    public void isInt(TextField input)
    {
        try
        {
            numChips = Integer.parseInt(input.getText());
            input.setStyle("-fx-text-inner-color: black;");
        }
        catch (NumberFormatException e)
        {
            input.setStyle("-fx-text-inner-color: red;");
        }
    }
}