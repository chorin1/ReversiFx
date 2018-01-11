import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Optional;

public class GameController {
	private static final int CELL_MAX_SIZE = 80;
    private static final int CELL_MIN_SIZE = 30;

	@FXML
	private Text player1scr;
	@FXML
	private Text player2scr;
	@FXML
	private BorderPane borderPane;
	@FXML
	private ImageView currentPlayerImg;

	private GridPane grid;
	private Model model;
	private Player currPlayerTurn;


	public void initialize() {
		/* check that graphics are available
		if (Assets.blank==null || Assets.blackPiece==null || Assets.whitePiece==null)
			throw new IllegalArgumentException("required image assets missing!");
		*/
		// set up model
		model = new Model(SettingsController.boardSize);
		currPlayerTurn = Player.PLAYER1;

		grid = new GridPane();
		//set up top menu
		MenuBar menuBar = new MenuBar();
		initMenu(menuBar);

		// set up borderpane (main layout)
		borderPane.setTop(menuBar);
		borderPane.setCenter(grid);

		// set up game board
		initBoard();

		// set up score labels
		player1scr.setText("Player1 - "+model.getScoreP1());
		player2scr.setText("Player2 - "+model.getScoreP2());

		//set up starting player piece image
		currentPlayerImg.setImage(Assets.getPiecesList().get(SettingsController.player1PieceIndex));
	}
	private void initBoard() {
		grid.getStyleClass().add("game-grid");

		// set up board constraints
		for (int i = 0; i < model.getBoardSize(); i++) {
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

		// add pane and imageview for each cell
		for (int i = 0; i < model.getBoardSize(); i++) {
			for (int j = 0; j < model.getBoardSize(); j++) {
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
	private void initMenu(MenuBar menuBar) {
		Menu gameMenu = new Menu("Game");
		Menu aboutMenu = new Menu("About");
		aboutMenu.setOnAction(event -> {
		//TODO: make about window
		});
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(event-> resetGame());
		MenuItem settings = new MenuItem("Settings");
		settings.setOnAction(event-> {
			openSettings();
		});
		MenuItem quit = new MenuItem("Quit");
		quit.setOnAction(event-> quitGame());
		gameMenu.getItems().addAll(newGame, settings, quit);
		menuBar.getMenus().addAll(gameMenu,aboutMenu);
	}
	private ImageView addPieceImage(int i, int j) {
		Cell currCell = model.getCellAt(new GamePos(i+1,j+1));
		ImageView pieceImg = new ImageView();
		setPieceImgByCell(pieceImg,currCell);
		pieceImg.setOnMouseReleased(e -> move(currPlayerTurn,j+1,i+1));
		pieceImg.setPreserveRatio(true);
		return pieceImg;
	}
	private Pane addPane(int i, int j) {
		Pane pane = new Pane();
		pane.setOnMouseReleased(e -> move(currPlayerTurn,j+1,i+1));
		// set each cell to stretch to its maximum available space
		GridPane.setHgrow(pane, Priority.ALWAYS);
		GridPane.setVgrow(pane, Priority.ALWAYS);
		pane.getStyleClass().add("game-grid-cell");
		if (i==0 && j==0)
			pane.getStyleClass().add("top-left");
		else if (i == 0 && j!=model.getBoardSize()-1)
			pane.getStyleClass().add("first-column");
		else if (j == 0 && i!=model.getBoardSize()-1)
			pane.getStyleClass().add("first-row");
		else if (i==model.getBoardSize()-1 && j==model.getBoardSize()-1)
			pane.getStyleClass().add("bot-right");
		else if (i==0 && j==model.getBoardSize()-1)
			pane.getStyleClass().add("bot-left");
		else if (i!=model.getBoardSize()-1 && j==model.getBoardSize()-1)
			pane.getStyleClass().add("last-row");
		else if (i==model.getBoardSize()-1 && j!=0)
			pane.getStyleClass().add("last-column");
		else if (j==0 && i==model.getBoardSize()-1)
			pane.getStyleClass().add("top-right");
		return pane;
	}

	// make a move on board
	private void move(Player player, int x, int y) {
		if (model.place(player, x,y)) {
			refreshBoardView();
			System.out.println(model);
			switchPlayerTurn();
		}
	}
	private void switchPlayerTurn() {
		//TODO: add another text for score
		player1scr.setText("Player1 - "+model.getScoreP1());
		player2scr.setText("Player2 - "+model.getScoreP2());
		currPlayerTurn = (currPlayerTurn==Player.PLAYER1)? Player.PLAYER2 : Player.PLAYER1;
		currentPlayerImg.setImage((currPlayerTurn==Player.PLAYER1)?
				Assets.getPiecesList().get(SettingsController.player1PieceIndex) :
				Assets.getPiecesList().get(SettingsController.player2PieceIndex));
		if (model.cantMove(Player.PLAYER1) && model.cantMove(Player.PLAYER2))
			endGame();
		else if (model.cantMove(currPlayerTurn)) {
			showAlertCantMove(currPlayerTurn);
			switchPlayerTurn();
		}
	}

	// refresh boardview
	private void refreshBoardView() {
		for (int i = 0; i < model.getBoardSize(); i++) {
			for (int j = 0; j < model.getBoardSize(); j++) {
				Cell currCell = model.getCellAt(new GamePos(i + 1, j + 1));
				// grid children are sorted by coulmn and then row
				ImageView pieceImg = (ImageView) (getNodeFromGridPane(grid, j, i));
				setPieceImgByCell(pieceImg,currCell);
			}
		}
	}

	// handle method to set imageview to selected player piece
	private void setPieceImgByCell(ImageView image, Cell cell) {
		switch (cell) {
			case EMPTY:
				image.setImage(Assets.blank);
				break;
			case PLAYER1:
				image.setImage(Assets.getPiecesList().get(SettingsController.player1PieceIndex));
				break;
			case PLAYER2:
			default:
				image.setImage(Assets.getPiecesList().get(SettingsController.player2PieceIndex));
				break;
		}
	}
	// handle method to get correct image from gridview board
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof ImageView) {
				return node;
			}
		}
		return null;
	}

	private void showAlertCantMove(Player player) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Too bad");
		String playerName = (player==Player.PLAYER1)? "player 1" : "player 2";
		alert.setHeaderText("Oopsy, " + playerName + " has no possible moves.");
		alert.setContentText("The turn passes to the other player");
		alert.showAndWait();
	}
	private void endGame() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("that's it!");
		if (model.getScoreP1() > model.getScoreP2())
			alert.setHeaderText("Game Over!\nThe winner is: Player1");
		if (model.getScoreP1() < model.getScoreP2())
			alert.setHeaderText("Game Over!\nThe winner is: Player2");
		else
			alert.setHeaderText("Game Over!\nIt's a tie!");
		alert.setContentText("Would you like to start another game?");
		ButtonType anotherGame = new ButtonType("Hell ya!");
		ButtonType quit = new ButtonType("Get me outta here!");
		ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(anotherGame, quit, cancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == anotherGame){
			resetGame();
		} else if (result.get() == quit) {
			quitGame();
		}
	}
	private void quitGame() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.close();
	}
	private void resetGame() {
		initialize();
	}
	private void openSettings() {
		try {
			Parent settings = FXMLLoader.load(getClass().getResource("SettingsLayout.fxml"));
			Scene settingsScene = new Scene(settings, 300, 400);
			Stage popup = new Stage();
			popup.setScene(settingsScene);
			popup.initModality(Modality.WINDOW_MODAL);
			popup.initOwner((Stage)borderPane.getScene().getWindow());
			popup.setTitle("Settings");
			popup.setResizable(false);
			popup.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
