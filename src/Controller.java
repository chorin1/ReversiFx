import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Controller {
	public final int BOARD_SIZE = 8;
	public static final int CELL_MAX_SIZE = 80;
    public static final int CELL_MIN_SIZE = 30;

	Image spirteImage = new Image("img/agk_spritesheet_gamebits3.png");
	PixelReader reader = spirteImage.getPixelReader();
	WritableImage whitePiece = new WritableImage(reader, 385, 0, 65, 65);
	WritableImage blackPiece = new WritableImage(reader, 385, 63, 65, 65);

	@FXML
	private BorderPane borderPane;
	public void initialize() {
		Model model = new Model(BOARD_SIZE);

		ToolBar toolbar = new ToolBar();
		borderPane.setTop(toolbar);
		GridPane grid = new GridPane();
		borderPane.setCenter(grid);
		grid.getStyleClass().add("game-grid");


		for (int i = 0; i < BOARD_SIZE; i++) {

			ColumnConstraints columnStraints = new ColumnConstraints();
			RowConstraints rowStraints = new RowConstraints();
			columnStraints.setMaxWidth(CELL_MAX_SIZE);
			columnStraints.setMinWidth(CELL_MIN_SIZE);
			rowStraints.setMaxHeight(CELL_MAX_SIZE);
			rowStraints.setMinHeight(CELL_MIN_SIZE);
			grid.getColumnConstraints().add(columnStraints);
			grid.getRowConstraints().add(rowStraints);
		}

		// align board to center of screen
		grid.setAlignment(Pos.CENTER);

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

				Cell currCell = model.getCellAt(new GamePos(i+1,j+1));
				ImageView pieceImg = new ImageView();
				switch (currCell) {
					case EMPTY:
						break;
					case PLAYER1:
						pieceImg.setImage(blackPiece);
						break;
					case PLAYER2:
					default:
						pieceImg.setImage(whitePiece);
						break;
				}

				grid.add(pieceImg,i,j);
				//pieceImg.setFitWidth(30);
				pieceImg.setPreserveRatio(true);
				pieceImg.minWidth(grid.getMinWidth());
				grid.setHalignment(pieceImg, HPos.CENTER);

			}
		}
	}
}
