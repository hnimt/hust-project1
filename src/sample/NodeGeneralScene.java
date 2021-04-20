package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NodeGeneralScene extends Stage implements IEditScene {
    private MyNode myNode;
    private Scene scene = new Scene(new Group(), 300, 300);
    private GridPane grid = new GridPane();
    private Text sceneTitle = new Text("Node general");

    private Label lblNodeName = new Label("Node name: ");
    private TextField txtNodeName = new TextField();

    private Button btnOk = new Button("OK");
    private Button btnCancel = new Button("Cancel");

    @Override
    public void fillData() {
        txtNodeName.setText(myNode.getName());
    }

    @Override
    public void layoutScene() {
        grid.add(lblNodeName, 0, 1);
        grid.add(txtNodeName, 1, 1);
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(15);
        hBox.getChildren().setAll(btnOk, btnCancel);
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getMyNode();
                closeStage();
            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fillData();
                closeStage();
            }
        });
        grid.add(hBox, 0, 2, 2, 1);
    }

    public NodeGeneralScene(MyNode myNode, String sceneTitle){
        super();
        this.myNode = myNode;
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
        fillData();
    }

    @Override
    public void closeStage() {
        this.close();
    }

    public MyNode getMyNode() {
        myNode.setName(txtNodeName.getText());
        myNode.getDataNode().setName(myNode.getName());
        return myNode;
    }


}
