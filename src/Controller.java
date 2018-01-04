import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

public class Controller {
	@FXML
	private BorderPane borderPane;
	public void initialize() {
		Model model = new Model(8);
		Label lbl = new Label("Hello World!");
		lbl.setTextFill(Color.web("#0076a3"));
		/*
		if (model.getCellAt(new GamePos(2,2))==Cell.EMPTY)
			System.out.println("CELL is empty!");
			*/
		ToolBar toolbar = new ToolBar();
		borderPane.setTop(toolbar);

		GridPane grid = new GridPane();
/*
		for (int i=1; i<= model.getBoardSize(); i++) {
			for (int j=1; j<= model.getBoardSize(); j++) {
				Cell currCell = model.getCellAt(new GamePos(i,j));
				String currChar;
				switch (currCell) {
					case EMPTY:
						currChar = " ";
						break;
					case PLAYER1:
						currChar = "X";
						break;
					case PLAYER2:
					default:
						currChar = "O";
						break;
				}
				//grid.add(new Label(currChar), i, j);
			}
		}
		*/

		borderPane.setCenter(grid);
	}
}
