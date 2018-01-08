import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class Controller {
	public final int BOARD_SIZE = 8;
	public static final int CELL_MAX_SIZE = 80;
    public static final int CELL_MIN_SIZE = 30;

	@FXML
	private BorderPane borderPane;
	private GridPane grid = new GridPane();
	Model model = new Model(BOARD_SIZE);
	Player currPlayerTurn = Player.PLAYER1;

	public void initialize() {
		ToolBar toolbar = new ToolBar();
		borderPane.setTop(toolbar);
		borderPane.setCenter(grid);
		grid.getStyleClass().add("game-grid");

		for (int i = 0; i < BOARD_SIZE; i++) {
			ColumnConstraints columnStraints = new ColumnConstraints();
			RowConstraints rowStraints = new RowConstraints();
			columnStraints.setMaxWidth(CELL_MAX_SIZE);
			columnStraints.setMinWidth(CELL_MIN_SIZE);
			rowStraints.setMaxHeight(CELL_MAX_SIZE);
			rowStraints.setMinHeight(CELL_MIN_SIZE);
			columnStraints.setPrefWidth(CELL_MAX_SIZE);
			rowStraints.setPrefHeight(CELL_MAX_SIZE);
			grid.getColumnConstraints().add(columnStraints);
			grid.getRowConstraints().add(rowStraints);
		}

		// align board to center of screen
		grid.setAlignment(Pos.CENTER);

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Pane pane = addPane(i,j);
				ImageView pieceImg = addPieceImage(i,j);
				grid.add(pane, i, j);
				grid.add(pieceImg,i,j);
				grid.setHalignment(pieceImg, HPos.CENTER);
				pieceImg.fitWidthProperty().bind(pane.widthProperty());
				pieceImg.fitHeightProperty().bind(pane.heightProperty());
			}
		}
	}

	private ImageView addPieceImage(int i, int j) {
		Cell currCell = model.getCellAt(new GamePos(i+1,j+1));
		ImageView pieceImg = new ImageView();
		switch (currCell) {
			case EMPTY:
				pieceImg.setImage(Assets.blank);
				break;
			case PLAYER1:
				pieceImg.setImage(Assets.blackPiece);
				break;
			case PLAYER2:
			default:
				pieceImg.setImage(Assets.whitePiece);
				break;
		}
		//pieceImg.setId((i+1) + ","+ (j+1));
		pieceImg.setOnMouseReleased(e -> {
			move(currPlayerTurn,j+1,i+1);
		});

		pieceImg.setPreserveRatio(true);
		return pieceImg;
	}

	private Pane addPane(int i, int j) {
		Pane pane = new Pane();
		//pane.setId((i+1) + ","+ (j+1));
		pane.setOnMouseReleased(e -> {
			move(currPlayerTurn,j+1,i+1);
		});
		// set each cell to stretch to its maximum available space
		grid.setHgrow(pane, Priority.ALWAYS);
		grid.setVgrow(pane, Priority.ALWAYS);
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
		return pane;
	}

	public void move(Player player, int x, int y) {
		System.out.println("in move " + x + "," + y);
		if (model.place(player, x,y)) {
			refreshBoardView();
			System.out.println(model);
			switchPlayerTurn();
		}
	}

	public void refreshBoardView() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Cell currCell = model.getCellAt(new GamePos(i + 1, j + 1));
				ImageView pieceImg = (ImageView)(getNodeFromGridPane(grid, j, i));
				switch (currCell) {
					case EMPTY:
						pieceImg.setImage(Assets.blank);
						break;
					case PLAYER1:
						pieceImg.setImage(Assets.blackPiece);
						break;
					case PLAYER2:
					default:
						pieceImg.setImage(Assets.whitePiece);
						break;
				}
			}
		}
	}

	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof ImageView) {
				return node;
			}
		}
		return null;
	}

	private void switchPlayerTurn() {
		currPlayerTurn = (currPlayerTurn==Player.PLAYER1)? Player.PLAYER2 : Player.PLAYER1;
	}
}
