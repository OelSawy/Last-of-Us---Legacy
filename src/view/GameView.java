package view;

import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.characters.Direction;
import model.characters.Hero;
import model.characters.Zombie;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;

public class GameView  extends Application  {
	String[] imgPaths = {
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/joel%20hero.jpg",
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/ellie%20hero.jpg",
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/tess%20hero.jpg",
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/riley%20hero.jpg",
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/tommy%20hero.jpg",
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/bill%20hero.jpg",
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/david%20hero.jpg",
		"file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/henry%20hero.png"
	};

	boolean gameWon;

	Hero selectedHero;

	int initialHero;

	Label[] selectedHeroDetails = {
		new Label(),
		new Label(),
		new Label(),
		new Label(),
		new Label(),
		new Label(),
		new Label()
	};

	enum SelectionMode {
		HERO, TARGET
	}

	SelectionMode mode = SelectionMode.HERO;

	@Override
	public void init() {
		try {
			Game.loadHeroes("F:/GUC/Semester 4/Computer Science - CSEN401/Game/Milestone2-Solution/Heroes.csv");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	@Override
	public void start(Stage primaryStage){
		Image icon = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/icon.png");
		primaryStage.getIcons().add(icon);
		primaryStage.setMaximized(true);
		primaryStage.setResizable(false);
		primaryStage.setTitle("The Last of Us Legacy");
		primaryStage.setScene(mainMenu(primaryStage));
		primaryStage.show();
	}
	
	private Scene mainMenu(Stage primaryStage) {
		StackPane root = new StackPane();
		VBox root2 = new VBox();
		Image background = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/background.jpg");
		ImageView backgroundView = new ImageView(background);
		Scene menu = new Scene(root);
		Font font = Font.font("Another Danger - Demo", FontWeight.BOLD, 70);
		Color greyColor = new Color(0.625,0.625,0.625,1);
		Label startGame = new Label("Start Game");
		startGame.setFont(font);
		startGame.setTextFill(greyColor);
		Label instructions = new Label("How to Play ?");
		instructions.setFont(font);
		instructions.setTextFill(greyColor);
		Label exit = new Label("Exit");
		exit.setFont(font);
		exit.setTextFill(greyColor);
		root2.setMaxHeight(600);
		root2.setAlignment(Pos.CENTER);
		root2.setSpacing(20);
		root2.setTranslateX(-500);
		root2.setTranslateY(-50);
		root2.getChildren().addAll(startGame,instructions,exit);
		root.getChildren().addAll(backgroundView,root2);
		startGame.setOnMouseEntered(new EventHandler<Event>() {
			
			@Override
			public void handle(Event arg0) {
				startGame.setTextFill(Color.WHITE);
			}
		});
		startGame.setOnMouseExited(new EventHandler<Event>() {
			
			@Override
			public void handle(Event arg0) {
				startGame.setTextFill(greyColor);
			}
		});
		startGame.setOnMouseClicked(new EventHandler<Event>() {
			
			@Override
			public void handle(Event event) {
				primaryStage.setScene(chooseHero(primaryStage));
			}
			
		});
		instructions.setOnMouseEntered(new EventHandler<Event>() {
			
			@Override
			public void handle(Event arg0) {
				instructions.setTextFill(Color.WHITE);
			}
		});
		instructions.setOnMouseExited(new EventHandler<Event>() {
			
			@Override
			public void handle(Event arg0) {
				instructions.setTextFill(greyColor);
			}
		});
		instructions.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				primaryStage.setScene(howToPlay(primaryStage));
			}
		});
		exit.setOnMouseEntered(new EventHandler<Event>() {
			
			@Override
			public void handle(Event arg0) {
				exit.setTextFill(Color.WHITE);
			}
		});
		exit.setOnMouseExited(new EventHandler<Event>() {
			
			@Override
			public void handle(Event arg0) {
				exit.setTextFill(greyColor);
			}
		});
		exit.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
		        System.exit(0);
			}
		});
		return menu;
	}
	
	private Scene chooseHero(Stage primaryStage) {
		Image blackBackground = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/black.jpg");
		ImageView backgroundView = new ImageView(blackBackground);
		StackPane root = new StackPane();
		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		VBox allHeroes = new VBox();
		HBox root2 = new HBox();
		Font font = Font.font("Game Continue 02", FontWeight.NORMAL, 23);
		ArrayList<Button> buttons1 = new ArrayList<Button>();
		ArrayList<Button> buttons2 = new ArrayList<Button>();
		Font headerFont = Font.font("Another Danger - Demo", FontWeight.BOLD, 70);
		Label header = new Label("Choose Your Hero");
		final Label name = new Label();
		final Label type = new Label();
		final Label hp = new Label();
		final Label actions = new Label();
		final Label damage = new Label();
		VBox heroDetails = new VBox();
		header.setFont(headerFont);
		header.setTextFill(Color.RED);
		for (int i = 0; i < Game.availableHeroes.size(); i++) {
			final int index = i;
			Button heroButton = new Button();
			heroButton.setPrefSize(200, 300);
			heroButton.setStyle("-fx-background-radius: 10px; ");
			heroButton.setOnMouseClicked(new EventHandler<Event>() {
				
				@Override
				public void handle(Event arg0) {
					selectedHero = Game.availableHeroes.get(index);
					initialHero = index;
					Game.startGame(Game.availableHeroes.get(index));
					primaryStage.setScene(map(primaryStage));
					
				}
			});
			Image heroImage = new Image(imgPaths[i], 210, 310, false, true);
			heroButton.setOnMouseEntered(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					name.setText("Hero Name : " + Game.availableHeroes.get(index).getName());
					name.setFont(font);
					name.setTextFill(Color.WHITE);
					type.setText("Hero Type : " + Game.availableHeroes.get(index).getClass().getSimpleName());
					type.setFont(font);
					type.setTextFill(Color.WHITE);
					hp.setText("Max HP : " + Game.availableHeroes.get(index).getMaxHp());
					hp.setFont(font);
					hp.setTextFill(Color.WHITE);
					actions.setText("Max Actions : " + Game.availableHeroes.get(index).getMaxActions());
					actions.setFont(font);
					actions.setTextFill(Color.WHITE);
					damage.setText("Hero Attack Damage : " + Game.availableHeroes.get(index).getAttackDmg());
					damage.setFont(font);
					damage.setTextFill(Color.WHITE);
				}
			});
			heroButton.setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					name.setText("");
					type.setText("");
					hp.setText("");
					actions.setText("");
					damage.setText("");
				}
			});
			Rectangle roundRect = new Rectangle(0, 0, 200, 300);
			roundRect.setArcWidth(10.0);
			roundRect.setArcHeight(10.0);
			ImagePattern heroPattern = new ImagePattern(heroImage);
			roundRect.setFill(heroPattern);
			roundRect.setEffect(new DropShadow(20, Color.BLACK));
			heroButton.setGraphic(roundRect);
			heroButton.setPadding(Insets.EMPTY);
			if (i < Game.availableHeroes.size()/2) {
				buttons1.add(heroButton);
			} else {
				buttons2.add(heroButton);
			}
		}
		for (int i = 0; i < buttons1.size(); i++) {
			HBox.setMargin(buttons1.get(i), new Insets(20));
		}
		for (int i = 0; i < buttons2.size(); i++) {
			HBox.setMargin(buttons2.get(i), new Insets(20));
		}
		heroDetails.getChildren().addAll(name,type,hp,actions,damage);
		heroDetails.setTranslateX(300);
		heroDetails.setTranslateY(450);
		BorderPane.setAlignment(header, Pos.TOP_CENTER);
		hbox1.getChildren().addAll(buttons1);
		hbox2.getChildren().addAll(buttons2);
		allHeroes.setAlignment(Pos.CENTER);
		allHeroes.getChildren().addAll(header,hbox1,hbox2);
		VBox.setMargin(header, new Insets(0,0,50,0));
		VBox.setMargin(hbox1, new Insets(0,0,50,0));
		allHeroes.setTranslateX(100);
		allHeroes.setTranslateY(-50);
		root2.getChildren().addAll(allHeroes,heroDetails);
		root.getChildren().addAll(backgroundView,root2);
		Scene choose = new Scene(root);
		return choose;
	}
	
	private Scene howToPlay(Stage primaryStage) {
		Image background = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/background.jpg");
		ImageView backgroundView = new ImageView(background);
		StackPane root = new StackPane();
		Pane root2 = new Pane();
		Font headerFont = Font.font("Another Danger - Demo", FontWeight.BOLD, 70);
		Label header = new Label("How to Play ?");
		header.setFont(headerFont);
		header.setTranslateX(200);
		header.setTranslateY(150);
		header.setTextFill(Color.WHITE);
		Font bodyFont = Font.font("Times New Roman", FontWeight.NORMAL, 50);
		Label body = new Label("The player only wins\nif he has successfully collected and used\nall vaccines and has 5 or more heroes alive");
		body.setTranslateX(200);
		body.setTranslateY(350);
		body.setFont(bodyFont);
		body.setTextFill(Color.RED);
		Button back = new Button("Back");
		back.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				primaryStage.setScene(mainMenu(primaryStage));
			}
		});
		root2.getChildren().addAll(header,body,back);
		root.getChildren().addAll(backgroundView,root2);
		Scene instructions = new Scene(root);
		return instructions;
	}

	private Scene map(Stage primaryStage) {
		StackPane root = new StackPane();
		GridPane mapPane = new GridPane();
		HBox root2 = new HBox();
		VBox details = new VBox();
		VBox controls = new VBox();
		Button[][] buttons = new Button[Game.map.length][Game.map.length];
		Font font = Font.font("Game Continue 02", FontWeight.NORMAL, 23);
		Font font2 = Font.font("Game Continue 02", FontWeight.NORMAL, 23);
		Image arrowUp = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/arrow%20up.png");
		ImageView upView = new ImageView(arrowUp);
		upView.setFitHeight(48);
		upView.setFitWidth(48);
		Image arrowDown = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/arrow%20down.png");
		ImageView downView = new ImageView(arrowDown);
		downView.setFitHeight(48);
		downView.setFitWidth(48);
		Image arrowLeft = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/arrow%20left.png");
		ImageView leftView = new ImageView(arrowLeft);
		leftView.setFitHeight(48);
		leftView.setFitWidth(48);
		Image arrowRight = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/arrow%20right.png");
		ImageView rightView = new ImageView(arrowRight);
		rightView.setFitHeight(48);
		rightView.setFitWidth(48);
		Label selectedHeader = new Label("Selected Hero :");
		selectedHeader.setFont(font);
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				final int i1 = i;
				final int j1 = j;
				Button b = new Button();
				b.setPrefSize(64, 64);
				b.setMaxHeight(64);
				b.setMaxWidth(64);
				b.setStyle("-fx-background-color: #003408; -fx-background-radius: 10px; ");
				b.setOnMouseClicked(new EventHandler<Event>() {
					@Override
					public void handle(Event arg0) {
						if (mode == SelectionMode.HERO && Game.map[Game.map.length-1-j1][i1] instanceof CharacterCell && ((CharacterCell)Game.map[Game.map.length-1-j1][i1]).getCharacter() instanceof Hero) {
							selectedHero = (Hero) ((CharacterCell) Game.map[Game.map.length-1-j1][i1]).getCharacter();
							updateHeroDetails();
							updateOtherHeroes(details);
						} else if (mode == SelectionMode.TARGET && Game.map[Game.map.length-1-j1][i1] instanceof CharacterCell && ((CharacterCell)Game.map[Game.map.length-1-j1][i1]).getCharacter() != null) {
							selectedHero.setTarget(((CharacterCell) Game.map[Game.map.length-1-j1][i1]).getCharacter());
						}
					}
				});
				buttons[Game.map.length-1-j][i] = b;
				mapPane.add(b, i, j);
			}
		}
		Label errorMessage = new Label();
		errorMessage.setFont(font);
		errorMessage.setTextFill(Color.RED);
		updateHeroDetails();
		details.getChildren().add(selectedHeader);
		VBox.setMargin(selectedHeader, new Insets(0, 0, 20, 0));
		details.getChildren().addAll(selectedHeroDetails);
		Button attack = new Button("Attack");
		attack.setFont(font);
		attack.setPrefSize(250, 50);
		attack.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				try {
					selectedHero.attack();
					updateHeroDetails();
					updateMap(mapPane, buttons);
					errorMessage.setText("");
				} catch (NotEnoughActionsException e) {
					errorMessage.setText(e.getMessage());
				} catch (InvalidTargetException e) {
					errorMessage.setText(e.getMessage());
				}
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		Button cure = new Button("Cure");
		cure.setFont(font);
		cure.setPrefSize(250, 50);
		cure.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				try {
					selectedHero.cure();
					updateMap(mapPane, buttons);
					updateHeroDetails();
					updateOtherHeroes(details);
					errorMessage.setText("");
				} catch (NoAvailableResourcesException e) {
					errorMessage.setText(e.getMessage());
				} catch (InvalidTargetException e) {
					errorMessage.setText(e.getMessage());
				} catch (NotEnoughActionsException e) {
					errorMessage.setText(e.getMessage());
				}
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		Button useSpecial = new Button("Use Special Action");
		useSpecial.setFont(font);
		useSpecial.setPrefSize(250, 50);
		useSpecial.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				try {
					selectedHero.useSpecial();
					updateHeroDetails();
					updateOtherHeroes(details);
					updateMap(mapPane, buttons);
					errorMessage.setText("");
				} catch (NoAvailableResourcesException e) {
					errorMessage.setText(e.getMessage());
				} catch (InvalidTargetException e) {
					errorMessage.setText(e.getMessage());
				}
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		Button setTarget = new Button("Choose Target");
		setTarget.setFont(font);
		setTarget.setPrefSize(250, 50);
		setTarget.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				if (mode == SelectionMode.HERO) {
					mode = SelectionMode.TARGET;
					setTarget.setText("Choose Hero");
				} else {
					mode = SelectionMode.HERO;
					setTarget.setText("Choose Target");
					if (Game.checkWin()) {
						gameWon = true;
						primaryStage.setScene(gameEnd(primaryStage));
					}
					if (Game.checkGameOver()) {
						gameWon = false;
						primaryStage.setScene(gameEnd(primaryStage));
					}
				}
			}
		});
		Button endTurn = new Button("End Turn");
		endTurn.setFont(font);
		endTurn.setPrefSize(250, 50);
		endTurn.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				Game.endTurn();
				updateHeroDetails();
				updateOtherHeroes(details);
				errorMessage.setText("");
				updateMap(mapPane, buttons);
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		controls.getChildren().addAll(attack,cure,useSpecial,setTarget,endTurn);
		Button moveUp = new Button();
		moveUp.setFont(font2);
		moveUp.setPrefSize(64,64);
		moveUp.setGraphic(upView);
		moveUp.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				int initial = selectedHero.getCurrentHp();
				try {
					selectedHero.move(Direction.UP);
					updateMap(mapPane, buttons);
					updateHeroDetails();
					if (selectedHero.getCurrentHp() < initial) {
						errorMessage.setText("Trap damage taken " + (initial - selectedHero.getCurrentHp()));
					} else {
						errorMessage.setText("");
					}
				} catch (MovementException e) {
					errorMessage.setText(e.getMessage());
				} catch (NotEnoughActionsException e) {
					errorMessage.setText(e.getMessage());
				}
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		Button moveDown = new Button();
		moveDown.setFont(font2);
		moveDown.setPrefSize(64,64);
		moveDown.setGraphic(downView);
		moveDown.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				int initial = selectedHero.getCurrentHp();
				try {
					selectedHero.move(Direction.DOWN);
					updateMap(mapPane, buttons);
					updateHeroDetails();
					if (selectedHero.getCurrentHp() < initial) {
						errorMessage.setText("Trap damage taken " + (initial - selectedHero.getCurrentHp()));
					} else {
						errorMessage.setText("");
					}
				} catch (MovementException e) {
					errorMessage.setText(e.getMessage());
				} catch (NotEnoughActionsException e) {
					errorMessage.setText(e.getMessage());
				}
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		Button moveLeft = new Button();
		moveLeft.setFont(font2);
		moveLeft.setMaxHeight(64);
		moveLeft.setMaxWidth(64);
		moveLeft.setGraphic(leftView);
		moveLeft.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				int initial = selectedHero.getCurrentHp();
				try {
					selectedHero.move(Direction.LEFT);
					updateMap(mapPane, buttons);
					updateHeroDetails();
					if (selectedHero.getCurrentHp() < initial) {
						errorMessage.setText("Trap damage taken " + (initial - selectedHero.getCurrentHp()));
					} else {
						errorMessage.setText("");
					}
				} catch (MovementException e) {
					errorMessage.setText(e.getMessage());
				} catch (NotEnoughActionsException e) {
					errorMessage.setText(e.getMessage());
				}
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		Button moveRight = new Button();
		moveRight.setFont(font2);
		moveRight.setPrefSize(64,64);
		moveRight.setGraphic(rightView);
		moveRight.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				int initial = selectedHero.getCurrentHp();
				try {
					selectedHero.move(Direction.RIGHT);
					updateMap(mapPane, buttons);
					updateHeroDetails();
					if (selectedHero.getCurrentHp() < initial) {
						errorMessage.setText("Trap damage taken " + (initial - selectedHero.getCurrentHp()));
					} else {
						errorMessage.setText("");
					}
				} catch (MovementException e) {
					errorMessage.setText(e.getMessage());
				} catch (NotEnoughActionsException e) {
					errorMessage.setText(e.getMessage());
				}
				if (Game.checkWin()) {
					gameWon = true;
					primaryStage.setScene(gameEnd(primaryStage));
				}
				if (Game.checkGameOver()) {
					gameWon = false;
					primaryStage.setScene(gameEnd(primaryStage));
				}
			}
		});
		HBox arrows = new HBox();
		arrows.getChildren().addAll(moveLeft,moveDown,moveRight);
		controls.getChildren().addAll(moveUp,arrows,errorMessage);
		Image background = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/whiteBackground.jpg");
		ImageView backgroundView = new ImageView(background);
		updateMap(mapPane,buttons);
		mapPane.setHgap(5);
		mapPane.setVgap(5);
		mapPane.setMaxHeight(1030);
		mapPane.setMaxWidth(1030);
		root2.getChildren().addAll(details,mapPane,controls);
		root.getChildren().addAll(backgroundView,root2);
		// Top Right Bottom Left
		HBox.setMargin(details, new Insets(50, 100, 0, 50));
		HBox.setMargin(mapPane, new Insets(20, 100, 0, 0));
		HBox.setMargin(controls, new Insets(0, 20, 0, 0));
		for (int i = 0; i < controls.getChildren().size() - 2; i++) {
			VBox.setMargin(controls.getChildren().get(i), new Insets(50, 0, 0, 0));
		}
		moveUp.setTranslateX(88);
		VBox.setMargin(errorMessage, new Insets(75, 0, 0, 0));
		Scene mapScene = new Scene(root);
		return mapScene;
	}

	private void updateMap(GridPane root, Button[][] buttons) {
		Image vaccine = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/Vaccine%20model.png");
		Image supply = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/Supply%20model.png");
		Image zombie = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/Zombie%20model.png");
		Image hero = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/joel.png");
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				if (Game.map[i][j].isVisible()) {
					buttons[i][j].setStyle("-fx-background-color: #82A259; -fx-background-radius: 10px;");
					if (Game.map[i][j] instanceof CollectibleCell) {
						if (((CollectibleCell) Game.map[i][j]).getCollectible() instanceof Vaccine) {
							ImageView vacc = new ImageView(vaccine);
							vacc.setPreserveRatio(true);
							vacc.setFitHeight(48);
							vacc.setFitWidth(48);
							buttons[i][j].setGraphic(vacc);
						} else {
							ImageView supp = new ImageView(supply);
							supp.setPreserveRatio(true);
							supp.setFitHeight(48);
							supp.setFitWidth(48);
							buttons[i][j].setGraphic(supp);
						}
					} else if (Game.map[i][j] instanceof CharacterCell) {
						if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Zombie) {
							ImageView zomb = new ImageView(zombie);
							zomb.setPreserveRatio(true);
							zomb.setFitHeight(48);
							zomb.setFitWidth(48);
							buttons[i][j].setGraphic(zomb);
						} else if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Hero) {
							ImageView heroView = new ImageView(hero);
							heroView.setPreserveRatio(true);
							heroView.setFitHeight(48);
							heroView.setFitWidth(48);
							buttons[i][j].setGraphic(heroView);
						} else {
							buttons[i][j].setGraphic(null);
						}
					}
				} else {
					buttons[i][j].setStyle("-fx-background-color: #003408; -fx-background-radius: 10px; ");
					buttons[i][j].setGraphic(null);
				}
			}
		}
	}

	private void updateHeroDetails() {
		Font font = Font.font("Game Continue 02", FontWeight.NORMAL, 23);
		selectedHeroDetails[0].setText("Name : " + selectedHero.getName());
		selectedHeroDetails[0].setFont(font);
		selectedHeroDetails[0].setTextFill(Color.BLACK);
		selectedHeroDetails[1].setText("Type : " + selectedHero.getClass().getSimpleName());
		selectedHeroDetails[1].setFont(font);
		selectedHeroDetails[1].setTextFill(Color.BLACK);
		selectedHeroDetails[2].setText("Current HP : " + selectedHero.getCurrentHp());
		selectedHeroDetails[2].setFont(font);
		selectedHeroDetails[2].setTextFill(Color.BLACK);
		selectedHeroDetails[3].setText("Attack Damage : " + selectedHero.getAttackDmg());
		selectedHeroDetails[3].setFont(font);
		selectedHeroDetails[3].setTextFill(Color.BLACK);
		selectedHeroDetails[4].setText("Available Actions : " + selectedHero.getActionsAvailable());
		selectedHeroDetails[4].setFont(font);
		selectedHeroDetails[4].setTextFill(Color.BLACK);
		selectedHeroDetails[5].setText("Vaccines Count : " + selectedHero.getVaccineInventory().size());
		selectedHeroDetails[5].setFont(font);
		selectedHeroDetails[5].setTextFill(Color.BLACK);
		selectedHeroDetails[6].setText("Supplies Count : " + selectedHero.getSupplyInventory().size());
		selectedHeroDetails[6].setFont(font);
		selectedHeroDetails[6].setTextFill(Color.BLACK);
	}

	private void updateOtherHeroes(VBox v) {
		Font font = Font.font("Game Continue 02", FontWeight.NORMAL, 23);
		v.getChildren().clear();
		Label selectedHeader = new Label("Selected Hero :");
		Label otherHeader = new Label("Other Heroes :");
		selectedHeader.setFont(font);
		otherHeader.setFont(font);
		v.getChildren().add(selectedHeader);
		VBox.setMargin(selectedHeader, new Insets(0, 0, 20, 0));
		VBox.setMargin(otherHeader, new Insets(20, 0, 20, 0));
		v.getChildren().addAll(selectedHeroDetails);
		v.getChildren().add(otherHeader);
		for (int i = 0; i < Game.heroes.size(); i++) {
			if (Game.heroes.get(i) != selectedHero) {
				Label name = new Label("Name : " + Game.heroes.get(i).getName());
				name.setFont(font);
				name.setTextFill(Color.BLACK);
				Label type = new Label("Type : " + Game.heroes.get(i).getClass().getSimpleName());
				type.setFont(font);
				type.setTextFill(Color.BLACK);
				Label hp = new Label("Current HP : " + Game.heroes.get(i).getCurrentHp());
				hp.setFont(font);
				hp.setTextFill(Color.BLACK);
				Label damage = new Label("Attack Damage : " + Game.heroes.get(i).getAttackDmg());
				damage.setFont(font);
				damage.setTextFill(Color.BLACK);
				Label actions = new Label("Available Actions : " + Game.heroes.get(i).getActionsAvailable());
				actions.setFont(font);
				actions.setTextFill(Color.BLACK);
				Label vaccines = new Label("Vaccines Count : " + Game.heroes.get(i).getVaccineInventory().size());
				vaccines.setFont(font);
				vaccines.setTextFill(Color.BLACK);
				Label supplies = new Label("Supplies Count : " + Game.heroes.get(i).getSupplyInventory().size());
				supplies.setFont(font);
				supplies.setTextFill(Color.BLACK);
				v.getChildren().addAll(name,type,hp,damage,actions,vaccines,supplies);
			}
		}
	}

	private Scene gameEnd(Stage primaryStage) {
		Image background = new Image("file:///F:/GUC/Semester%204/Computer%20Science%20-%20CSEN401/Game/Milestone2-Solution/src/assets/images/background.jpg");
		ImageView backgroundView = new ImageView(background);
		StackPane root = new StackPane();
		Font headerFont = Font.font("Another Danger - Demo", FontWeight.BOLD, 70);
		Label header = new Label();
		if (gameWon) {
			header.setText("You Won !");
		} else {
			header.setText("You Lost !");
		}
		header.setFont(headerFont);
		header.setTranslateX(200);
		header.setTranslateY(350);
		header.setTextFill(Color.WHITE);
		root.getChildren().addAll(backgroundView,header);
		Scene gameEndScene = new Scene(root);
		return gameEndScene;
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}