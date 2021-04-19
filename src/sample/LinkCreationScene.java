package sample;

import com.bayesserver.Link;
import com.yworks.yfiles.graph.IEdge;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.INode;
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

import java.util.Iterator;

public class LinkCreationScene extends Stage implements IEditScene {
    private MyLink myLink;
    private MyNetwork myNetwork;
    private Scene scene = new Scene(new Group(), 300, 300);
    private GridPane grid = new GridPane();
    private Text sceneTitle = new Text("Link Creation");

    private Label lblPredecessorNode = new Label("Predecessor node: ");
    private TextField txtPredecessorNode = new TextField();
    private Label lblSuccessor = new Label("Successor node: ");
    private TextField txtSuccessor = new TextField();

    private Button btnOk = new Button("OK");
    private Button btnCancel = new Button("Cancel");

    public LinkCreationScene(MyLink myLink, MyNetwork myNetwork, String sceneTitle) {
        super();
        this.myLink = myLink;
        this.myNetwork = myNetwork;
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
    public void fillData() {
        txtPredecessorNode.setText("");
        txtSuccessor.setText("");
    }

    @Override
    public void layoutScene() {
        grid.add(lblPredecessorNode, 0, 1);
        grid.add(txtPredecessorNode, 1, 1);
        grid.add(lblSuccessor, 0, 2);
        grid.add(txtSuccessor, 1, 2);
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(15);
        hBox.getChildren().setAll(btnOk, btnCancel);
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
        grid.add(hBox, 0, 3, 2, 1);
    }

    @Override
    public void closeStage() {
        this.close();
    }

    public MyLink createMyLink (){
        for (MyNode node : myNetwork.getNodes()){
            if (node.getGraphicNode().getLabels().getItem(0).getText().equals(txtPredecessorNode.getText())){
                myLink.setPredecessorNode(node);
            }
            if (node.getGraphicNode().getLabels().getItem(0).getText().equals(txtSuccessor.getText())){
                myLink.setSuccessorNode(node);
            }
        }
        myLink.setEdgeGraphic(myNetwork.getiGraph().createEdge(myLink.getPredecessorNode().getGraphicNode(), myLink.getSuccessorNode().getGraphicNode()));
        myLink.setDataLink(new Link(myLink.getPredecessorNode().getDataNode(), myLink.getSuccessorNode().getDataNode()));
        myNetwork.getNetwork().getLinks().add(myLink.getDataLink());
        myNetwork.getLinks().add(myLink);
        return myLink;
    }

    public MyLink getMyLink() {
        return myLink;
    }

    public void setMyLink(MyLink myLink) {
        this.myLink = myLink;
    }

    public MyNetwork getMyNetwork() {
        return myNetwork;
    }

    public void setMyNetwork(MyNetwork myNetwork) {
        this.myNetwork = myNetwork;
    }





}
