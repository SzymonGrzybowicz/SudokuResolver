package ui;

import exception.SudokuNotResolvedException;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.SudokuField;
import logic.SudokuResolver;

import java.util.List;

public class AppView extends Application {

    GridPane board = new GridPane();
    FlowPane flowPane = new FlowPane();

    @Override
    public void start(Stage primaryStage) {

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 0, 0, 0));

        Button clearBtn = new Button();
        clearBtn.setMinSize(245, 50);
        clearBtn.setText("Clear Sudoku!");
        clearBtn.setOnAction(event -> onClear());

        Button resolveBtn = new Button();
        resolveBtn.setMinSize(245, 50);
        resolveBtn.setText("Resolve Sudoku");
        resolveBtn.setOnAction(event -> onResolve());

        hBox.setMaxSize(450, 500);
        hBox.getChildren().addAll(clearBtn, resolveBtn);

        PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                StackPane cell = new StackPane();
                cell.getStyleClass().add("cell");
                cell.pseudoClassStateChanged(right, col == 2 || col == 5);
                cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);

                cell.getChildren().add(createTextField());

                board.add(cell, col, row);
            }
        }

        flowPane.getChildren().addAll(hBox, board);
        Scene scene = new Scene(flowPane, 490, 540, Color.BLACK);
        primaryStage.setTitle("Sudoku resolver");

        scene.getStylesheets().add("sudoku.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private TextField createTextField() {
        TextField textField = new TextField();

        textField.setTextFormatter(new TextFormatter<Integer>(c -> {
            if (c.getControlNewText().matches("^$|^([1-9])$")) {
                return c;
            } else {
                return null;
            }
        }));
        textField.setAlignment(Pos.CENTER);
        return textField;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void onClear() {
        board.getChildren().stream()
                .flatMap(i -> ((StackPane) i).getChildren().stream())
                .filter(i -> i instanceof TextField)
                .forEach(i -> ((TextField) i).clear());
    }

    public void onResolve() {
        SudokuField[][] sudokuFields = new SudokuField[9][9];
        board.getChildren().forEach(
                e -> {
                    if (e instanceof StackPane) {
                        int column = GridPane.getColumnIndex(e);
                        int row = GridPane.getRowIndex(e);
                        TextField text = (TextField) ((StackPane) e).getChildren().get(0);
                        int val = text.getText().isEmpty() ? 0 : Integer.parseInt(text.getText());
                        SudokuField field = new SudokuField(val, column, row);
                        if (val != 0) {
                            field.getPossibleValues().clear();
                        }
                        sudokuFields[row][column] = field;
                    }
                }
        );
        SudokuResolver resolver = new SudokuResolver();
        try {
            SudokuField[][] resolved = resolver.resolve(sudokuFields);
            for (int i = 0; i < resolved.length; i++) {
                for (int j = 0; j < resolved[0].length; j++) {
                    List<Node> nodes = board.getChildren();
                    for (Node node : nodes) {
                        if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j) {
                            ((TextField) ((StackPane) node).getChildren().get(0)).setText(Integer.toString(resolved[i][j].getValue()));
                        }
                    }

                }
            }
        } catch (SudokuNotResolvedException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sudoku Error!");
            alert.setHeaderText("Not resolved");
            alert.setContentText("This sudoku can't be resolved!");
            alert.showAndWait();
        }
    }
}
