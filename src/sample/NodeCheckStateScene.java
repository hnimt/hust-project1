package sample;

import com.bayesserver.State;
import com.bayesserver.inference.Inference;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.view.Pen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class NodeCheckStateScene extends Stage implements IEditScene {
    private MyNode myNode;
    private List<State> chooseStates;
    private IGraph graph;

    //Create scene and grid
    private Scene scene = new Scene(new Group(), 300, 300);
    private GridPane grid = new GridPane();
    private Text sceneTitle = new Text("Checking node's state: ");

    //Create label

    //Create toggle group and radio buttons
    private ToggleGroup toggleGroup = new ToggleGroup();
    private RadioButton rdTrueState = new RadioButton("True");
    private RadioButton rdFalseState = new RadioButton("False");

    //Create Button
    private Button btnOk = new Button("OK");
    private Button btnCancel = new Button("Cancel");

    public NodeCheckStateScene(MyNode myNode, List<State> chooseStates, IGraph graph, String sceneTitle){
        super();
        this.myNode = myNode;
        this.chooseStates = chooseStates;
        this.graph = graph;
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        layoutScene();
        this.sceneTitle.setText(sceneTitle);
        this.sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        this.sceneTitle.setText(sceneTitle);
        this.scene.setRoot(grid);
        this.setScene(this.scene);
    }

    @Override
    public void fillData() {

    }

    @Override
    public void layoutScene() {

        // Create style checked node
        ShapeNodeStyle redNodeStyle = new ShapeNodeStyle();
        redNodeStyle.setShape(ShapeNodeShape.RECTANGLE);
        redNodeStyle.setPaint(Color.LIGHTCORAL);
        redNodeStyle.setPen(Pen.getTransparent());

        ShapeNodeStyle blueNodeStyle = new ShapeNodeStyle();
        blueNodeStyle.setShape(ShapeNodeShape.RECTANGLE);
        blueNodeStyle.setPaint(Color.LIGHTBLUE);
        blueNodeStyle.setPen(Pen.getTransparent());

        // Set toggle group
        rdTrueState.setToggleGroup(toggleGroup);
        rdFalseState.setToggleGroup(toggleGroup);

        //Create hbox state
        HBox hBoxState = new HBox(10);
        hBoxState.setAlignment(Pos.CENTER);
        hBoxState.setSpacing(15);
        hBoxState.getChildren().setAll(rdTrueState, rdFalseState);

        //Create hbox button
        HBox hBoxButton = new HBox(10);
        hBoxButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxButton.setSpacing(15);
        hBoxButton.getChildren().setAll(btnOk, btnCancel);

        //handle submit
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (rdTrueState.isSelected()){
                    chooseStates.add(myNode.getTrueState());
                    graph.setStyle(myNode.getGraphicNode(), blueNodeStyle);
                } else if (rdFalseState.isSelected()){
                    chooseStates.add(myNode.getFalseState());
                    graph.setStyle(myNode.getGraphicNode(), redNodeStyle);
                }
                closeStage();
            }
        });

        //handle cancel
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeStage();
            }
        });

        grid.add(hBoxState, 0, 0, 2, 1);
        grid.add(hBoxButton, 0, 1, 2, 1);
    }

    @Override
    public void closeStage() {
        this.close();
    }
}
