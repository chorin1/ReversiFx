import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Controller {
	public final int BOARD_SIZE = 8;

	@FXML
	private BorderPane borderPane;
	public void initialize() {
		Model model = new Model(BOARD_SIZE);

		ToolBar toolbar = new ToolBar();
		borderPane.setTop(toolbar);
		GridPane grid = new GridPane();
		borderPane.setCenter(grid);
		grid.getStyleClass().add("game-grid");
		for(int i = 0; i < BOARD_SIZE; i++) {
			ColumnConstraints column = new ColumnConstraints(80);
			RowConstraints row = new RowConstraints(80);
			grid.getColumnConstraints().add(column);
			grid.getRowConstraints().add(row);
		}

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Pane pane = new Pane();
				pane.setId((i+1) + ","+ (j+1));
				pane.setOnMouseReleased(e -> {
					System.out.println("You pressed cell " + pane.getId());
				});
				pane.getStyleClass().add("game-grid-cell");
				if (i==0 && j==0)
					pane.getStyleClass().add("top-left");
				else if (i == 0 && j!=BOARD_SIZE-1)
					pane.getStyleClass().add("first-column");
				else if (j == 0 && i!=BOARD_SIZE-1)
					pane.getStyleClass().add("first-row");
				else if (i==BOARD_SIZE-1 && j==BOARD_SIZE-1)
					pane.getStyleClass().add("bot-right");
				else if (i==0 && j==BOARD_SIZE-1)
					pane.getStyleClass().add("bot-left");
				else if (i!=BOARD_SIZE-1 && j==BOARD_SIZE-1)
					pane.getStyleClass().add("last-row");
				else if (i==BOARD_SIZE-1 && j!=0)
					pane.getStyleClass().add("last-column");
				else if (j==0 && i==BOARD_SIZE-1)
					pane.getStyleClass().add("top-right");
				grid.add(pane, i, j);
			}
		}
		grid.setAlignment(Pos.CENTER);
		for (int i=0; i< model.getBoardSize(); i++) {
			for (int j=0; j< model.getBoardSize(); j++) {
				Cell currCell = model.getCellAt(new GamePos(i+1,j+1));
				String currChar = "";
				switch (currCell) {
					case EMPTY:
						break;
					case PLAYER1:
						currChar = "X";
						break;
					case PLAYER2:
					default:
						currChar = "O";
						break;
				}
				Label lbl = new Label(currChar);
				grid.add(lbl, i, j);
				GridPane.setHalignment(lbl, HPos.CENTER);
			}
		}

	}
}
