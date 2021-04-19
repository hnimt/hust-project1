package sample;

import com.bayesserver.Node;
import com.bayesserver.Table;
import com.bayesserver.TableIterator;
import com.yworks.yfiles.utils.IObservableCollection;
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

import java.util.ArrayList;
import java.util.List;

public class NodeProbabilityScene extends Stage implements IEditScene{
    private MyNode myNode;
    private MyNetwork myNetwork;
    private int numberPredecessorNodes;
    private Scene scene = new Scene(new Group(), 800, 500);
    private GridPane grid = new GridPane();
    private Text sceneTitle = new Text("Node Probability");
    private List<TextField> textFieldList;

    private Button btnOk = new Button("OK");
    private Button btnCancel = new Button("Cancel");

    public NodeProbabilityScene(MyNode myNode, MyNetwork myNetwork, String sceneTitle) {
        super();
        this.myNode = myNode;
        this.myNetwork = myNetwork;
        this.textFieldList = new ArrayList<>();
        this.numberPredecessorNodes = myNode.getPredecessorNodes().size();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(10);
        grid.setVgap(10);
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

    //create textfield and fillData
    public void createTextFieldAndFillData(int rowHeader){
        List<TextField> tempTextFieldList = new ArrayList<>();
        if (myNode.getDoubleDataArray() == null){
            for (int i = 0; i < Math.pow(2, numberPredecessorNodes + 1); i++){
                TextField tempTextField = new TextField();
                tempTextField.setPrefWidth(50);
                tempTextFieldList.add(tempTextField);
                grid.add(tempTextField, i + 1, rowHeader);
            }

        } else {
            for (int i = 0; i < Math.pow(2, numberPredecessorNodes + 1); i++){
                TextField tempTextField = new TextField();
                tempTextField.setPrefWidth(40);
                tempTextField.setText(String.valueOf(myNode.getDoubleDataArray()[i]));
                tempTextFieldList.add(tempTextField);
                grid.add(tempTextField, i + 1, rowHeader);
            }
        }
        setTextFieldList(tempTextFieldList);
    }

    @Override
    public void layoutScene() {
        //layout scene
        int rowHeader = 0;
        for (MyNode predecessorNode : myNode.getPredecessorNodes()){
            grid.add(new Label(predecessorNode.getName()), 0, rowHeader);
            rowHeader++;
        }
        grid.add(new Label(myNode.getName()), 0, rowHeader);
        rowHeader++;
        grid.add(new Label(""), 0, rowHeader);
        for (int i = 0; i < numberPredecessorNodes + 1; i++) {
            for (int j = 0; j < Math.pow(2, numberPredecessorNodes + 1); j++){
                int temp = (int) (j / Math.pow(2, numberPredecessorNodes + 1 - i - 1));
                if (temp % 2 == 0) {
                    grid.add(new Label("True"), j + 1, i);
                } else {
                    grid.add(new Label("False"), j + 1, i);
                }
            }
        }

        //filldata
        createTextFieldAndFillData(rowHeader);

        int rowButton = rowHeader + 1;
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(15);
        hBox.getChildren().setAll(btnOk, btnCancel);
        grid.add(hBox,(int) (Math.pow(2, numberPredecessorNodes + 1) - 1), rowButton, 2, 1);

        //Action set data
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //set data
                List<MyNode> dataNodeList = new ArrayList<>();
                Node[] dataNodeArray = new Node[numberPredecessorNodes + 1];
                double[] doubleDataArray = new double[getTextFieldList().size()];

                for (MyNode myPredecessor : myNode.getPredecessorNodes()){
                    dataNodeList.add(myPredecessor);
                }
                for (int i = 0; i < dataNodeList.size(); i++){
                    dataNodeArray[i] = dataNodeList.get(i).getDataNode();
                }

                dataNodeArray[numberPredecessorNodes] = myNode.getDataNode();
                myNode.setDataNodeArray(dataNodeArray);

                for (int i = 0; i < getTextFieldList().size(); i++){
                    doubleDataArray[i] = Double.valueOf(getTextFieldList().get(i).getText());
                }
                myNode.setDoubleDataArray(doubleDataArray);

                Table bayesTable = myNode.getDataNode().newDistribution().getTable();
                TableIterator iterator = new TableIterator(bayesTable, dataNodeArray);
                iterator.copyFrom(doubleDataArray);
                myNode.getDataNode().setDistribution(bayesTable);
                closeStage();
            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeStage();
            }
        });
    }

    @Override
    public void closeStage() {
        this.close();
    }

    public List<TextField> getTextFieldList() {
        return textFieldList;
    }

    public void setTextFieldList(List<TextField> textFieldList) {
        this.textFieldList = textFieldList;
    }
}
