package sample;

import com.bayesserver.*;
import com.bayesserver.Table;
import com.bayesserver.inference.*;
import com.yworks.yfiles.geometry.RectD;
import com.yworks.yfiles.graph.*;
import com.yworks.yfiles.graph.styles.ImageNodeStyle;
import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.Pen;
import com.yworks.yfiles.view.input.GraphEditorInputMode;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HelloWorldController {
    private MyNetwork myNetwork;
    private Network bayesNetwork;
    private MyCalculator myCalculator;
    private MyNode mySelectedNode;
    private List<State> chooseStates;
    private InferenceFactory inferenceFactory;
//    private Inference inference;
//    private QueryOptions queryOptions;
//    private QueryOutput queryOutput;
    public GraphControl graphControl;
    public GraphEditorInputMode graphEditorInputMode;
    public IGraph graph;
    public Button btnCreateNode;
    public Button btnCreateLink;
    private Random generator = new Random();
    private int nodeNumber = 1;


    public void initialize() {

        // Bayes
        myNetwork = new MyNetwork();
        bayesNetwork = myNetwork.getNetwork();
        myCalculator = new MyCalculator();
        inferenceFactory = new RelevanceTreeInferenceFactory();
        myCalculator.setInferenceFactory(inferenceFactory);
        chooseStates = new ArrayList<>();

        // UI
        graph = graphControl.getGraph();
        myNetwork.setiGraph(graph);
        graphEditorInputMode = new GraphEditorInputMode();
        graphEditorInputMode.setContextMenuItems(GraphItemTypes.NODE);
        NodeDefaults nodeDefaults = new NodeDefaults();

        //Create node
        btnCreateNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNewNode();
            }
        });

        //Create Link
        btnCreateLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showLinkCreationScene(myNetwork);
            }
        });

        // Event with selected node
        graphEditorInputMode.addPopulateItemContextMenuListener((sender, args) -> {
            // Create the context menu items
            if (args.getItem() instanceof INode) {

                INode node = (INode) args.getItem();
                mySelectedNode = myNetwork.getSelectedNode(node);
                Inference inference = inferenceFactory.createInferenceEngine(bayesNetwork);
                QueryOptions queryOptions = inferenceFactory.createQueryOptions();
                QueryOutput queryOutput = inferenceFactory.createQueryOutput();
                // Get the menu to populate
                ContextMenu menu = (ContextMenu) args.getMenu();
                // Create a menu item
                // Create a menu item to change name and label the node
                MenuItem nodeGeneral = new MenuItem("Node general");
                //Set action node general
                nodeGeneral.setOnAction(actionEvent -> {
                    showNodeGeneral(mySelectedNode);
                    graph.setLabelText(mySelectedNode.getGraphicNode().getLabels().getItem(0), mySelectedNode.getName());
                });

                // Create a menu item to change node probability
                MenuItem nodeProbability = new MenuItem("Node Probability");
                // Set action node probability
                nodeProbability.setOnAction(actionEvent -> {
                    myNetwork.setPredecessorNodesOfNode(mySelectedNode);
                    showNodeProbabilityScene(mySelectedNode, myNetwork);
                });

                // Create a menu item to change node state
                MenuItem chooseState = new MenuItem("Choose State");
                // Set action change node state
                chooseState.setOnAction(actionEvent -> {
                    showNodeCheckStateScene(mySelectedNode, chooseStates, graph);
                });

                // Create a menu item to calculate P(selectedNode)
                // Create a menu item to change node probability
                MenuItem calcProbability = new MenuItem("Calculate Probability");
                // Set action node probability
                calcProbability.setOnAction(actionEvent -> {
                    for (State state : chooseStates){
                        inference.getEvidence().setState(state);
                    }
                    Table tblQuery = new Table(mySelectedNode.getDataNode());
                    inference.getQueryDistributions().add(tblQuery);
                    try {
                        inference.query(queryOptions, queryOutput);
                    } catch (InconsistentEvidenceException e) {
                        e.printStackTrace();
                    }

                    String message = "P(" + mySelectedNode.getName() + ") = { True: " + tblQuery.get(mySelectedNode.getTrueState()) + ", False: " + tblQuery.get(mySelectedNode.getFalseState()) + "}.";
                    showAlert(message);
                    for (MyNode myNode : myNetwork.getNodes()){
                        graph.setStyle(myNode.getGraphicNode(), nodeDefaults.getStyle());
                    }
                });

                menu.getItems().addAll(nodeGeneral, nodeProbability, chooseState, calcProbability);
                // Show the menu
                args.setShowingMenuRequested(true);
                // Mark the event as handled
                args.setHandled(true);
            }
        });

        //Set input mode
        graphControl.setInputMode(graphEditorInputMode);
    }

    public void onLoaded() {
        graphControl.fitGraphBounds();
    }

    // Create node
    public void createNewNode() {
        INode iNode = graph.createNode(
                new RectD(300.00 * generator.nextDouble(), 300.00 * generator.nextDouble(), 40, 40));
        ILabel iLabel = graph.addLabel(iNode, "Node" + nodeNumber);
        nodeNumber++;
        MyNode myNode = new MyNode();
        myNode.setGraphicNode(iNode);
        myNode.setName(myNode.getGraphicNode().getLabels().getItem(0).getText());
        Node node = new Node(myNode.getName(), myNode.getTrueState(), myNode.getFalseState());
        myNode.setDataNode(node);
        myNetwork.getNodes().add(myNode);
        myNetwork.getNetwork().getNodes().add(node);
    }

    //Show node general
    public void showNodeGeneral(MyNode myNode){
        NodeGeneralScene nodeGeneralScene = new NodeGeneralScene(myNode, myNode.getName());
        nodeGeneralScene.initModality(Modality.APPLICATION_MODAL);
        nodeGeneralScene.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                nodeGeneralScene.getMyNode();
            }
        });
        nodeGeneralScene.showAndWait();
    }

    //Show create link
    public void showLinkCreationScene(MyNetwork myNetwork){
        LinkCreationScene linkCreationScene = new LinkCreationScene(new MyLink(), myNetwork, "Create link");
        linkCreationScene.initModality(Modality.APPLICATION_MODAL);
        linkCreationScene.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                linkCreationScene.createMyLink();
            }
        });
        linkCreationScene.showAndWait();
    }

    //Show node probability scene
    public void showNodeProbabilityScene(MyNode myNode, MyNetwork myNetwork){
        NodeProbabilityScene nodeProbabilityScene = new NodeProbabilityScene(myNode, myNetwork, "Probability");
        nodeProbabilityScene.initModality(Modality.APPLICATION_MODAL);
        nodeProbabilityScene.showAndWait();
    }

    //Show check node state scene
    public void showNodeCheckStateScene(MyNode myNode, List<State> chooseStates, IGraph graph){
        NodeCheckStateScene nodeCheckStateScene =
                new NodeCheckStateScene(myNode, chooseStates, graph, "Check state");
        nodeCheckStateScene.initModality(Modality.APPLICATION_MODAL);
        nodeCheckStateScene.showAndWait();
    }

    // Show alert
    private Optional<ButtonType> showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Probability");
        alert.setHeaderText("Probability!");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }
}
