package app;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import app.Algorithms.*;
import app.Algorithms.Backtracking.DivideAndConquerBacktracking;
import app.Algorithms.Backtracking.FastBacktracking;
import app.Algorithms.Backtracking.TracedBacktracking;
import app.Algorithms.DynamicProgramming.DynamicProgramming;
import app.Algorithms.Greedy.GreedyFirstFit;
import app.Algorithms.Greedy.GreedyNextFit;
import app.Entities.Parcel;
import app.Entities.ParcelComponent;
import app.Factories.ColorFactory;
import app.Factories.ParcelFactory;
import app.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main is the base class for the whole project which allows the program
 * to run and in the process determine the order in which the program
 * executes its other methods.
 * In this case the main class executes the GUI and from here on the other code can be accessed.
 *
 * @author 	Hendrick Baacke
 * @author 	Rafael Tsentides
 * @author 	Martijn Hilders
 * @author 	Hristo Minkov
 * @author 	Daan Stevens
 * @version 1.0
 * @since 1.0
 */
public class Main extends Application {
    private Shape3D[][] boxes;
    private Button[] plusButtons, infoButtons, minusButtons;
    private Label[] countLabels;
    private int[] countParcels, valuesParcels;
    private PerspectiveCamera[] cameras;
    private Group[] groups;
    private SubScene[] subScenes;
    private List<String> algorithmChoices;

    private Translate pivot;
    private Rotate rotate;
    private Timeline timeline;
    private Group introGroup, mainGroup, drawingGroup;
    private Button startButton, algorithmButton;
    private Label titleLabel, algorithmLabel, maxScoreLabel;
    private Stage primaryStage;
    private Scene introScene, mainScene;
    private String currentAlgorithm;

    /**
     * The method which initializes the GUI with its primary- and intro stage.
     * @param primaryStage              is the primary visual window template
     * @since 1.0
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.APPLICATION_TITLE);

        this.introScene = createIntroScene();

        this.primaryStage.setScene(introScene);
        this.primaryStage.show();
    }

    /**
     * Creates the main scene of the program, for the 3D purposes a camera is circling around the made containers
     * such that it looks like 3D. Also a button is added to go back to the main menu (introScene) as well as
     * a box to display the maximum score retrieved.
     * @since 1.0
     */
    private Scene createMainScene() {
        this.mainGroup = new Group();
        this.drawingGroup = new Group();

        Box boxContainer = new Box(Constants.CONTAINER_WIDTH, Constants.CONTAINER_HEIGHT, Constants.CONTAINER_LENGTH);
        boxContainer.setTranslateX(-Constants.CONTAINER_WIDTH/2.0);
        boxContainer.setTranslateY(-Constants.CONTAINER_HEIGHT/2.0);
        boxContainer.setTranslateZ(-Constants.CONTAINER_LENGTH/2.0);
        boxContainer.setMaterial(new PhongMaterial(Color.WHITE));
        boxContainer.setDrawMode(DrawMode.LINE);

        PerspectiveCamera containerCamera = new PerspectiveCamera(true);
        containerCamera.setTranslateX(-Constants.CONTAINER_WIDTH/2.0);
        containerCamera.setTranslateY(-Constants.CONTAINER_HEIGHT/2.0);
        containerCamera.setTranslateZ(-Constants.CONTAINER_LENGTH/2.0);
        containerCamera.getTransforms().addAll (
                this.pivot,
                this.rotate,
                new Rotate(Constants.ANIMATION_ROTATION_ANGLE, Rotate.X_AXIS),
                new Translate(0, 0, Constants.ANIMATION_Z_TRANSLATION)
        );

        this.drawingGroup.getChildren().add(boxContainer);
        this.drawingGroup.getChildren().add(containerCamera);

        SubScene mainSubScene = new SubScene(this.drawingGroup, Constants.APPLICATION_WIDTH,
                Constants.APPLICATION_HEIGHT, true, SceneAntialiasing.DISABLED);
        mainSubScene.setCamera(containerCamera);
        mainSubScene.setFill(Color.BLACK);

        Button goToMenu = new Button("Main Menu");
        goToMenu.setFont(Font.font ("Verdana", FontWeight.BOLD, 12));
        goToMenu.setPrefSize(120, 30);
        goToMenu.setOnAction(e -> {
            primaryStage.setScene(introScene);
        });

        this.maxScoreLabel = new Label();
        this.maxScoreLabel.setPrefWidth(200);
        this.maxScoreLabel.setLayoutX(680);
        this.maxScoreLabel.setLayoutY(50);
        this.maxScoreLabel.setFont(Font.font ("Verdana", FontWeight.BOLD, 16));
        this.maxScoreLabel.setTextFill(Color.WHITE);

        this.mainGroup.getChildren().add(mainSubScene);
        this.mainGroup.getChildren().add(goToMenu);
        this.mainGroup.getChildren().add(this.maxScoreLabel);

        return new Scene(
                this.mainGroup,
                Constants.APPLICATION_WIDTH,
                Constants.APPLICATION_HEIGHT,
                false
        );
    }

    /**
     * creates the main menu of the program, in here the methods for the button initialization are called upon
     * to place the buttons onto the template.
     */
    private Scene createIntroScene() {
        this.introGroup = new Group();

        this.initMainComponents();
        this.initParcels();
        this.initCameras();
        this.initAnimation();
        this.initGroups();
        this.initSubScenes();
        this.initButtons();
        this.initCountComponents();
        this.initAlgorithmChoice();
        this.setInfoButtonsAction();

        return new Scene(
                this.introGroup,
                Constants.APPLICATION_WIDTH,
                Constants.APPLICATION_HEIGHT,
                true
        );
    }

    /**
     * Initializes the algorithm choice option of the application.
     */
    private void initAlgorithmChoice() {
        this.currentAlgorithm = "Greedy Next-Fit";

        this.algorithmChoices = new ArrayList<>();
        this.algorithmChoices.add("Greedy Next-Fit");
        this.algorithmChoices.add("Greedy First-Fit");
        this.algorithmChoices.add("Normal Backtracking");
        this.algorithmChoices.add("Divide and Conquer Backtracking");
        this.algorithmChoices.add("Traceable Backtracking");
        this.algorithmChoices.add("Dynamic Programming");
        this.algorithmChoices.add("Genetic Algorithm");
        this.algorithmChoices.add("Algorithm X - Dancing Links");

        this.algorithmLabel = new Label("Algorithm: Greedy Next-Fit");
        this.algorithmLabel.setLayoutX(400);
        this.algorithmLabel.setLayoutY(260);
        this.algorithmLabel.setPrefWidth(120);
        this.algorithmLabel.setFont(Font.font ("Verdana", FontWeight.BOLD, 12));// DokhalSoZaal
        this.algorithmLabel.setWrapText(true);
        this.algorithmLabel.setTextAlignment(TextAlignment.CENTER);

        this.algorithmButton = new Button("Choose Algorithm");
        this.algorithmButton.setLayoutY(330);
        this.algorithmButton.setLayoutX(380);
        this.algorithmButton.setPrefSize(160, 30);
        this.algorithmButton.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Greedy First-Fit", this.algorithmChoices);
            dialog.setTitle("Algorithm Choice");
            dialog.setHeaderText("You can choose among 3 algorithms.");
            dialog.setContentText("Choose an algorithm:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                this.algorithmLabel.setText("Algorithm: " + s);
                this.currentAlgorithm = s;
            });
        });

        this.introGroup.getChildren().add(this.algorithmLabel);
        this.introGroup.getChildren().add(this.algorithmButton);
    }

    /**
     * Adds action listeners to all 'i'-type buttons.
     */
    private void setInfoButtonsAction() {
        for (int i=0;i<6;i++){
            int finalI = i;
            this.infoButtons[i].setOnAction(e -> {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Information");

                String headerText = "";

                switch (finalI){
                    case 0: headerText = "Parcel type L -> 5 boxes (0.5 x 0.5 x 0.5)"; break;
                    case 1: headerText = "Parcel type P -> 5 boxes (0.5 x 0.5 x 0.5)"; break;
                    case 2: headerText = "Parcel type T -> 5 boxes (0.5 x 0.5 x 0.5)"; break;
                    case 3: headerText = "Parcel type A -> 1 box (1.0 x 1.0 x 2.0)"; break;
                    case 4: headerText = "Parcel type B -> 1 box (1.0 x 1.5 x 2.0)"; break;
                    case 5: headerText = "Parcel type C -> 1 box (1.5 x 1.5 x 1.5)"; break;
                }

                dialog.setHeaderText(headerText);

                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField parcelCount = new TextField(countParcels[finalI] + "");
                parcelCount.setPromptText("Count of parcels");
                TextField parcelValue = new TextField(valuesParcels[finalI] + "");
                parcelValue.setPromptText("Value for parcel");

                grid.add(new Label("Count of parcels:"), 0, 0);
                grid.add(parcelCount, 1, 0);
                grid.add(new Label("Value for parcel:"), 0, 1);
                grid.add(parcelValue, 1, 1);

                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        return new Pair<>(parcelCount.getText(), parcelValue.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                result.ifPresent(countValue -> {
                    countParcels[finalI] = Integer.valueOf(countValue.getKey());
                    valuesParcels[finalI] = Integer.valueOf(countValue.getValue());

                    countLabels[finalI].setText("Count: " + ++countParcels[finalI]);
                    countLabels[finalI].setText( "Count: "
                                    + (countParcels[finalI] == 0 ? "0" : --countParcels[finalI]));
                });
            });
        }
    }

    /**
     * Initializes the components for the main menu.
     */
    private void initMainComponents() {
        this.titleLabel = new Label("3D Knapsack");
        this.titleLabel.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        this.titleLabel.setPrefWidth(240);
        this.titleLabel.setLayoutX(350);
        this.titleLabel.setLayoutY(150);

        this.startButton = new Button("Start");
        this.startButton.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        this.startButton.setPrefSize(240, 60);
        this.startButton.setLayoutX(335);
        this.startButton.setLayoutY(430);
        this.startButton.setOnAction(e -> {
            mainScene = createMainScene();
            this.startAlgorithm(this.currentAlgorithm);
            primaryStage.setScene(mainScene);
        });

        this.valuesParcels = new int[]{3, 4, 5, 3, 4, 5};

        this.introGroup.getChildren().add(this.titleLabel);
        this.introGroup.getChildren().add(this.startButton);
    }

    /**
     * Initializes all components that deal with counting of parcels.
     */
    private void initCountComponents() {
        this.countParcels = new int[6];
        this.countLabels = new Label[6];

        for(int i = 0; i < 6; i++){
            this.countLabels[i] = new Label("Count: 0");
            this.countLabels[i].setFont(Font.font ("Verdana", FontWeight.BOLD, 18));
            this.countLabels[i].setPrefWidth(140);

            if(i < 3){
                this.countLabels[i].setLayoutX(110);
                this.countLabels[i].setLayoutY(170 + i*190);
            } else {
                this.countLabels[i].setLayoutX(720);
                this.countLabels[i].setLayoutY(170 + (i%3)*190);
            }
        }

        this.introGroup.getChildren().addAll(this.countLabels);
    }

    /**
     * Initializes all plus, minus and info buttons for the main menu.
     */
    private void initButtons() {
        this.plusButtons = new Button[6];
        this.minusButtons = new Button[6];
        this.infoButtons = new Button[6];

        for (int i = 0; i < 6; i++) {
            this.plusButtons[i] = new Button("+");
            this.minusButtons[i] = new Button("-");
            this.infoButtons[i] = new Button("i");

            this.plusButtons[i].setFont(Font.font ("Ariel Black", FontWeight.BOLD, 15));
            this.minusButtons[i].setFont(Font.font ("Ariel Black", FontWeight.BOLD, 15));
            this.infoButtons[i].setFont(Font.font ("Verdana", FontWeight.BOLD, 15));

            int finalI = i;
            this.plusButtons[i].setOnAction(e -> this.countLabels[finalI].setText("Count: " + ++this.countParcels[finalI]));
            this.minusButtons[i].setOnAction(e -> this.countLabels[finalI].setText( "Count: "
                    + (this.countParcels[finalI] == 0 ? "0" : --this.countParcels[finalI])));

            int sz = 30;
            this.plusButtons[i].setPrefSize(sz, sz);
            this.minusButtons[i].setPrefSize(sz, sz);
            this.infoButtons[i].setPrefSize(sz, sz);

            if(i < 3){
                this.plusButtons[i].setLayoutX(260);
                this.plusButtons[i].setLayoutY(30 + i*190);

                this.minusButtons[i].setLayoutX(260);
                this.minusButtons[i].setLayoutY(120 + i*190);

                this.infoButtons[i].setLayoutX(260);
                this.infoButtons[i].setLayoutY(75 + i*190);
            } else {
                this.plusButtons[i].setLayoutX(620);
                this.plusButtons[i].setLayoutY(30 + (i%3)*190);

                this.minusButtons[i].setLayoutX(620);
                this.minusButtons[i].setLayoutY(120 + (i%3)*190);

                this.infoButtons[i].setLayoutX(620);
                this.infoButtons[i].setLayoutY(75 + (i%3)*190);
            }
        }

        this.introGroup.getChildren().addAll(this.plusButtons);
        this.introGroup.getChildren().addAll(this.minusButtons);
        this.introGroup.getChildren().addAll(this.infoButtons);
    }

    /**
     * Initializing all the groups for the main menu and adding all boxes and cameras to them.
     */
    private void initGroups() {
        this.groups = new Group[6];
        for (int i=0;i<6;i++){
            this.groups[i] = new Group();
            this.groups[i].getChildren().addAll(this.boxes[i]);
            this.groups[i].getChildren().add(cameras[i]);
        }
    }

    /**
     * Initializes all subscenes and adds them to the introGroup.
     */
    private void initSubScenes() {
        this.subScenes = new SubScene[6];
        for (int i = 0; i < 6; i++) {
            this.subScenes[i] = new SubScene(this.groups[i], Constants.MAIN_SUBSCENES_WIDTH,
                    Constants.MAIN_SUBSCENES_HEIGHT, true, SceneAntialiasing.DISABLED);
            this.subScenes[i].setCamera(this.cameras[i]);
            this.subScenes[i].setFill(Color.BLACK);

            if(i < 3){
                this.subScenes[i].setLayoutX(50);
                this.subScenes[i].setLayoutY(20 + i*190);
            } else {
                this.subScenes[i].setLayoutX(660);
                this.subScenes[i].setLayoutY(20 + (i%3)*190);
            }
        }

        this.introGroup.getChildren().addAll(this.subScenes);
    }

    /**
     * Adds animation to the parcels in the main menu.
     */
    private void initAnimation() {
        this.timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(this.rotate.angleProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(Constants.ANIMATION_SPEED),
                        new KeyValue(this.rotate.angleProperty(), 360)
                )
        );
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    /**
     * Initializing the rotation and translation of the animation
     * and adding all the perspective cameras to the parcels in the main menu
     */

    private void initCameras() {
        this.pivot = new Translate();
        this.rotate = new Rotate(0, Rotate.Y_AXIS);

        this.cameras = new PerspectiveCamera[6];
        for(int i=0;i<Constants.SIZE_OF_PARCEL_TYPES;i++){
            this.cameras[i] = new PerspectiveCamera(true);
            this.cameras[i].getTransforms().addAll (
                    this.pivot,
                    this.rotate,
                    new Rotate(Constants.ANIMATION_ROTATION_ANGLE, Rotate.X_AXIS),
                    new Translate(0, 0, Constants.ANIMATION_Z_TRANSLATION)
            );
        }
    }

    /**
     * Initializing all the parcels displaied at the main menu.
     */
    private void initParcels() {
        this.boxes = new Shape3D[Constants.SIZE_OF_PARCEL_TYPES][];

        // L Shape

        this.boxes[0] = new Shape3D[5];

        this.boxes[0][0] = new Box(5, 5, 5);
        this.boxes[0][1] = new Box(5, 5, 5);
        this.boxes[0][1].setLayoutX(-5);
        this.boxes[0][2] = new Box(5, 5, 5);
        this.boxes[0][2].setLayoutX(-10);
        this.boxes[0][3] = new Box(5, 5, 5);
        this.boxes[0][3].setLayoutX(5);
        this.boxes[0][4] = new Box(5, 5, 5);
        this.boxes[0][4].setLayoutY(5);
        this.boxes[0][4].setLayoutX(5);

        for(int i=0;i<5;i++) this.boxes[0][i].setMaterial(new PhongMaterial(Color.valueOf("#55ffe1")));

        // P Shape
        this.boxes[1] = new Shape3D[5];

        this.boxes[1][0] = new Box(5, 5, 5);
        this.boxes[1][2] = new Box(5, 5, 5);
        this.boxes[1][2].setLayoutY(5);
        this.boxes[1][4] = new Box(5, 5, 5);
        this.boxes[1][4].setLayoutY(5);
        this.boxes[1][4].setLayoutX(5);
        this.boxes[1][3] = new Box(5, 5, 5);
        this.boxes[1][3].setLayoutX(5);
        this.boxes[1][1] = new Box(5, 5, 5);
        this.boxes[1][1].setLayoutY(-5);

        for(int i=0;i<5;i++) this.boxes[1][i].setMaterial(new PhongMaterial(Color.valueOf("#a6fd29")));

        // T Shape

        this.boxes[2] = new Shape3D[5];

        this.boxes[2][0] = new Box(5, 5, 5);
        this.boxes[2][1] = new Box(5, 5, 5);
        this.boxes[2][1].setLayoutX(-5);
        this.boxes[2][1].setLayoutY(-5);
        this.boxes[2][2] = new Box(5, 5, 5);
        this.boxes[2][2].setLayoutY(-5);
        this.boxes[2][3] = new Box(5, 5, 5);
        this.boxes[2][3].setLayoutY(-5);
        this.boxes[2][3].setLayoutX(5);
        this.boxes[2][4] = new Box(5, 5, 5);
        this.boxes[2][4].setLayoutY(5);

        for(int i=0;i<5;i++) this.boxes[2][i].setMaterial(new PhongMaterial(Color.valueOf("#ff3b94")));

        // Ordinary boxes

        for(int i=3;i<6;i++) this.boxes[i] = new Shape3D[1];

        this.boxes[3][0] = new Box(6, 6, 12);
        this.boxes[3][0].setMaterial(new PhongMaterial(Color.valueOf("#099FFF")));

        this.boxes[4][0] = new Box(6, 9, 12);
        this.boxes[4][0].setMaterial(new PhongMaterial(Color.valueOf("#FF6600")));

        this.boxes[5][0] = new Box(9, 9, 9);
        this.boxes[5][0].setMaterial(new PhongMaterial(Color.valueOf("#F2EA02")));
    }

    /**
     * Main method of the program
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * starting methods for all the different algorithms, where the start algorithm method contains the switch case to
     * initialize all of them.
     *
     * @param algorithmName                 used to chose which algorithm gets started.
     */
    private void startAlgorithm(String algorithmName) {
        switch (algorithmName){
            case "Greedy Next-Fit": this.startGreedyAlgorithm(false); break;
            case "Divide and Conquer Backtracking": this.startDivideAndConquerBacktrackingAlgorithm(); break;
            case "Normal Backtracking": this.startFastBacktrackingAlgorithm(); break;
            case "Traceable Backtracking": this.startBacktrackingAnimationAlgorithm(); break;
            case "Greedy First-Fit": this.startGreedyAlgorithm(true); break;
            case "Dynamic Programming": this.startDPAlgorithm(); break;
            case "Genetic Algorithm": break;
            case "Algorithm X - Dancing Links": break;
        }
    }

    /**
     * Starts Divide and Conquer algorithm.
     */
    private void startDivideAndConquerBacktrackingAlgorithm() {
        List<Parcel> inputParcels = new ArrayList<>();

        for(int i=0;i<Constants.SIZE_OF_PARCEL_TYPES;i++){
            if(this.countParcels[i] > 0){
                inputParcels.add(ParcelFactory.getParcelByIdAndValue(i, this.valuesParcels[i]));
            }
        }

        Algorithm<char[][][]> divideAndConquerBacktracking = new DivideAndConquerBacktracking(inputParcels, this.countParcels);

        char[][][] solution = divideAndConquerBacktracking.solve();

        for(int k = 0;k<Constants.CONTAINER_LENGTH;k++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
                    if(solution[i][j][k] == 'i') continue;

                    Box box1 = new Box(
                            Constants.BLOCK_SIZE,
                            Constants.BLOCK_SIZE,
                            Constants.BLOCK_SIZE);
                    box1.setTranslateX(-0.5 - i);
                    box1.setTranslateY(-0.5 - j);
                    box1.setTranslateZ(-0.5 - k);
                    box1.setMaterial(new PhongMaterial(ColorFactory
                            .getColorByParcelType(solution[i][j][k])));
                    this.drawingGroup.getChildren().add(box1);
                }
            }
        }

        this.maxScoreLabel.setText(Constants.PRINT_MAX_SCORE_STRING + divideAndConquerBacktracking.getMaxScore());
    }

    /**
     * Starts Fast Backtracking Algorithm
     */
    private void startFastBacktrackingAlgorithm(){
        List<Parcel> inputParcels = new ArrayList<>();

        for(int i=0;i<Constants.SIZE_OF_PARCEL_TYPES;i++){
            if(this.countParcels[i] > 0){
                inputParcels.add(ParcelFactory.getParcelByIdAndValue(i, this.valuesParcels[i]));
            }
        }

        FastBacktracking fastBacktracking = new FastBacktracking(inputParcels, this.countParcels);
        char[][][] solution = fastBacktracking.solve();

        for(int k = 0;k<Constants.CONTAINER_LENGTH;k++) {
            for (int j = 0; j < Constants.CONTAINER_HEIGHT; j++) {
                for (int i = 0; i < Constants.CONTAINER_WIDTH; i++) {
                    if(solution[i][j][k] == ' ') continue;

                    Box box1 = new Box(
                            Constants.BLOCK_SIZE,
                            Constants.BLOCK_SIZE,
                            Constants.BLOCK_SIZE);
                    box1.setTranslateX(-0.5 - i);
                    box1.setTranslateY(-0.5 - j);
                    box1.setTranslateZ(-0.5 - k);
                    box1.setMaterial(new PhongMaterial(ColorFactory
                            .getColorByParcelType(solution[i][j][k])));
                    this.drawingGroup.getChildren().add(box1);
                }
            }
        }

        this.maxScoreLabel.setText(Constants.PRINT_MAX_SCORE_STRING + fastBacktracking.getMaxScore());
    }

    /**
     * Starts a Greedy algorithm.
     * @param firstFit      checks if it should be FirstFit or NextFit
     * @see GreedyFirstFit
     * @see GreedyNextFit
     */
    private void startGreedyAlgorithm(boolean firstFit){
        List<Parcel> inputParcels = new ArrayList<>();

        for(int i=0;i<Constants.SIZE_OF_PARCEL_TYPES;i++){
            for(int j=0;j<this.countParcels[i];j++){
                inputParcels.add(ParcelFactory.getParcelByIdAndValue(i, this.valuesParcels[i]));
            }
        }

        List<Parcel> solutionParcels;
        if(firstFit){
            GreedyFirstFit greedyAlgorithm = new GreedyFirstFit(inputParcels, this.countParcels);
            solutionParcels = greedyAlgorithm.solve();
            this.maxScoreLabel.setText(Constants.PRINT_MAX_SCORE_STRING + greedyAlgorithm.getMaxScore());
        }else{
            GreedyNextFit greedyAlgorithm = new GreedyNextFit(inputParcels, this.countParcels);
            solutionParcels = greedyAlgorithm.solve();
            this.maxScoreLabel.setText(Constants.PRINT_MAX_SCORE_STRING + greedyAlgorithm.getMaxScore());
        }

        this.animatedAlgorithmStart(solutionParcels);
    }

    /**
     * Starts the Traceable Backtracking algorithm.
     */
    private void startBacktrackingAnimationAlgorithm(){
        List<Parcel> inputParcels = new ArrayList<>();

        for(int i=0;i<Constants.SIZE_OF_PARCEL_TYPES;i++){
            for(int j=0;j<this.countParcels[i];j++){
                inputParcels.add(ParcelFactory.getParcelByIdAndValue(i, valuesParcels[i]));
            }
        }

        TracedBacktracking tracedBacktracking = new TracedBacktracking(inputParcels, this.countParcels);
        List<Parcel> solutionParcels = tracedBacktracking.solve();

        this.animatedAlgorithmStart(solutionParcels);
        this.maxScoreLabel.setText(Constants.PRINT_MAX_SCORE_STRING + tracedBacktracking.getMaxScore());
    }

    /**
     * Starts the dynamic programming algorithm.
     */
    private void startDPAlgorithm(){
        List<Parcel> inputParcels = new ArrayList<>();

        for(int i=0;i<Constants.SIZE_OF_PARCEL_TYPES;i++){
            for(int j=0;j<this.countParcels[i];j++){
                inputParcels.add(ParcelFactory.getParcelByIdAndValue(i, valuesParcels[i]));
            }
        }

        DynamicProgramming dynamicProgramming = new DynamicProgramming(inputParcels, this.countParcels);
        List<Parcel> solutionParcels = dynamicProgramming.solve();

        this.animatedAlgorithmStart(solutionParcels);
        this.maxScoreLabel.setText(Constants.PRINT_MAX_SCORE_STRING + dynamicProgramming.getMaxScore());
    }

    /**
     * method used to animate the algorithms during their execution.
     * @param solutionParcels           array list of the solutions computed by the algorithms
     */
    private void animatedAlgorithmStart(List<Parcel> solutionParcels){
        final int[] parcelId = {0};
        Timeline animation = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> {
                            if(parcelId[0] < solutionParcels.size()){
                                for(ParcelComponent component : solutionParcels.get(parcelId[0]).getComponents()){
                                    Box box1 = new Box(
                                            Constants.BLOCK_SIZE,
                                            Constants.BLOCK_SIZE,
                                            Constants.BLOCK_SIZE);
                                    box1.setTranslateX(-0.5 - component.getX());
                                    box1.setTranslateY(-0.5 - component.getY());
                                    box1.setTranslateZ(-0.5 - component.getZ());
                                    box1.setMaterial(new PhongMaterial(ColorFactory
                                            .getColorByParcelType(solutionParcels.get(parcelId[0]).getType())));
                                    this.drawingGroup.getChildren().add(box1);
                                }
                                parcelId[0]++;
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(Constants.ANIMATION_DURATION_PER_PARCEL))
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
}
