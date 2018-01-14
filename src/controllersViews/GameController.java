package controllersViews;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Optional;

import model.*;
import assets.*;

/**
 * The main controller of the game.
 * Binds between the game model and the view.
 * shows current model state, score of players and current player's turn.
 * when a new game is created, the controller will use the last saved user choice
 */
public class GameController {
	// max/min size of a cell (in pixels)
	private final static int CELL_MAX_SIZE = 80;
    private final static int CELL_MIN_SIZE = 30;

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
	private Stage settingsPopupStage;
	private SettingsController settingsController;

	/**
	 * Initialize the game view - sets up a gridpane for the board and menu bar
	 * right side of borderpane is constructed by fxml
	 */
	public void initialize() {

		grid = new GridPane();
		//set up top menu
		MenuBar menuBar = new MenuBar();
		initMenu(menuBar);

		// place menubar and gamegrid in the borderpane
		borderPane.setTop(menuBar);
		borderPane.setCenter(grid);

	}

	/**
	 * Init the game view elements according to current settings and model
 	 */
	public void initBoard() {
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
				GridPane.setHalignment(pieceImg, HPos.CENTER);
				GridPane.setValignment(pieceImg, VPos.CENTER);
				// allow windows resize
				pieceImg.fitWidthProperty().bind(pane.widthProperty().subtract(10));
				pieceImg.fitHeightProperty().bind(pane.heightProperty().subtract(10));
			}
		}

		// set up score labels
		updateScore();

		//set up starting player piece image
		currentPlayerImg.setImage(Assets.getInstance().getPieceImg(settingsController.getPlayer1PieceIndex()));
		currPlayerTurn = Player.PLAYER1;
	}

	/**
	 * Init the top menu bar view elements according to current settings and model
	 */
	private void initMenu(MenuBar menuBar) {
		Menu gameMenu = new Menu("Game");
		MenuItem newGame = new MenuItem("New Game");
		MenuItem settings = new MenuItem("Settings");
		MenuItem quit = new MenuItem("Quit");
		Menu aboutMenu = new Menu("About");
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction(event -> showAbout());
		newGame.setOnAction(event-> resetGame());
		settings.setOnAction(event-> openSettings());
		quit.setOnAction(event-> quitGame());

		gameMenu.getItems().addAll(newGame, settings, quit);
		aboutMenu.getItems().add(aboutItem);
		menuBar.getMenus().addAll(gameMenu,aboutMenu);
	}
	/**
	 * get the correct piece (image) by x, y coordinates
	 * @param i coordinate x on the board
	 * @param j coordinate y on the board
	 * @return the imageview according to user selected piece of the player in that boarcell
 	 */
	private ImageView addPieceImage(int i, int j) {
		BoardCell currCell = model.getCellAt(new GamePos(i+1,j+1));
		ImageView pieceImg = new ImageView();
		// call handle method to set the correct image for the imageview
		setPieceImgByCell(pieceImg,currCell);
		// set action for image to make a move
		pieceImg.setOnMouseReleased(e -> move(currPlayerTurn,j+1,i+1));
		pieceImg.setPreserveRatio(true);
		return pieceImg;
	}
	/**
	 * get the correct pane by x, y coordinates
	 * @param i coordinate x on the board
	 * @param j coordinate y on the board
	 * @return the pane with the correct style according to its board position (and with the correct move action)
	 */
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

	/**
	 * updates the score labels
	 */
	private void updateScore() {
		player1scr.setText(String.valueOf(model.getScoreP1()));
		player2scr.setText(String.valueOf(model.getScoreP2()));
	}

	/**
	 * checks if a move is valid according to the model, if so the move will be placed and the turn will be switched.
	 * @param player player who made the move
	 * @param x coordinate on the board (1..n)
	 * @param y coordinate on the board (1..n)
	 */
	private void move(Player player, int x, int y) {
		if (model.place(player, x,y)) {
			//System.out.println(model);
			switchPlayerTurn();
		}
	}

	/**
	 * updates the view elements for a turn switch
	 * check if the game has ended or the current player has no moves
	 */
	private void switchPlayerTurn() {
		currPlayerTurn = (currPlayerTurn==Player.PLAYER1)? Player.PLAYER2 : Player.PLAYER1;
		refreshBoardView();
		updateScore();
		currentPlayerImg.setImage((currPlayerTurn==Player.PLAYER1)?
				Assets.getInstance().getPieceImg(settingsController.getPlayer1PieceIndex()) :
				Assets.getInstance().getPieceImg(settingsController.getPlayer2PieceIndex()));

		// both players can't move
		if (model.cantMove(Player.PLAYER1) && model.cantMove(Player.PLAYER2))
			endGame();
		// just one player cant move
		else if (model.cantMove(currPlayerTurn)) {
			showAlertCantMove(currPlayerTurn);
			switchPlayerTurn();
		}
	}

	/**
	 * refresh the board, use after each turn is done
	 */
	private void refreshBoardView() {
		for (int i = 0; i < model.getBoardSize(); i++) {
			for (int j = 0; j < model.getBoardSize(); j++) {
				BoardCell currCell = model.getCellAt(new GamePos(i + 1, j + 1));
				// grid children are sorted by coulmn and then row
				ImageView pieceImg = (getImgFromGridpane(grid, j, i));
				setPieceImgByCell(pieceImg,currCell);
			}
		}
	}

	/**
	 * handle method to set imageview to the selected player piece]
	 * @see Assets
 	 */
	private void setPieceImgByCell(ImageView image, BoardCell cell) {
		switch (cell) {
			case EMPTY:
				image.setImage(Assets.getInstance().blank);
				break;
			case PLAYER1:
				image.setImage(Assets.getInstance().getPieceImg(settingsController.getPlayer1PieceIndex()));
				break;
			case PLAYER2:
			default:
				image.setImage(Assets.getInstance().getPieceImg(settingsController.getPlayer2PieceIndex()));
				break;
		}
	}

	/**
	 * handle method to get the imageView using coordinates from gridView board
	 * @param gridPane the board
	 * @param col   col coordinate
	 * @param row   row coordinate
	 * @return  the imageView in that cell (the piece)
	 */
	private ImageView getImgFromGridpane(GridPane gridPane, int col, int row) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof ImageView) {
				return (ImageView)node;
			}
		}
		return null;
	}

	/**
	 * show an alert that the player can't move
	 * @param player the player that has no moves
	 */
	private void showAlertCantMove(Player player) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Too bad");
		String playerName = (player==Player.PLAYER1)? "player 1" : "player 2";
		alert.setHeaderText("Oopsy, " + playerName + " has no possible moves.");
		alert.setContentText("The turn passes to the other player");
		alert.showAndWait();
	}

	/**
	 * show alert that the game has ended. give the user choices to start a new game or quit.
	 */
	private void endGame() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("that's it!");
		if (model.getScoreP1() > model.getScoreP2())
			alert.setHeaderText("Game Over!\nThe winner is: Player1");
		else if (model.getScoreP1() < model.getScoreP2())
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

	/**
	 * quit game
	 */
	private void quitGame() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.close();
	}

	/**
	 * create a new game with the saved user settings
	 */
	private void resetGame() {
		model = new Model(settingsController.getBoardSize());
		initialize();
		initBoard();
	}

	/**
	 * open settings stage as a pop-up
	 */
	private void openSettings() {
		try {
			// cancel all changes if user closes window brutally (through 'X' bttn)
			settingsPopupStage.setOnCloseRequest(event -> settingsController.cancelChanges());
			settingsPopupStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * show the about alert
	 */
	private void showAbout() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("ReversiFX");
		alert.setContentText("Ver. 0.6\nA game by Ben C.");
		alert.setGraphic(new ImageView(Assets.getInstance().getAppImage("aboutImg")));
		alert.showAndWait();
	}

	/**
	 * give this controller reference to the settings controller and stage
	 * used to open settings view as popup, and get current settings from the controller
	 * @param controller instance of SettingsController
	 * @param stage stage of the SettingsController
	 */
	public void initSettings(SettingsController controller, Stage stage) {
		settingsPopupStage = stage;
		settingsController = controller;
	}

	/**
	 * set game model used by this controller
	 * @param model the model of the game
	 * @see Model
	 */
	public void setModel (Model model) {
		this.model = model;
	}
}