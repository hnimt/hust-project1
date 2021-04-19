package sample;

import com.bayesserver.Node;
import com.bayesserver.State;
import com.bayesserver.Table;
import com.yworks.yfiles.graph.INode;
import com.yworks.yfiles.utils.IEnumerable;
import com.yworks.yfiles.utils.IObservableCollection;
import com.yworks.yfiles.utils.ObservableCollection;

import java.util.*;

public class MyNode {
    private String name;
    private INode graphicNode;
    private Node dataNode;
    private IObservableCollection<MyNode> predecessorNodes;
    private State trueState;
    private State falseState;
    private Node[] dataNodeArray;
    private double[] doubleDataArray;

    public MyNode() {
        this.predecessorNodes = new ObservableCollection<>();
        this.trueState = new State("True");
        this.falseState = new State("False");
    }

    public INode getGraphicNode() {
        return graphicNode;
    }

    public void setGraphicNode(INode graphicNode) {
        this.graphicNode = graphicNode;
    }

    public Node getDataNode() {
        return dataNode;
    }

    public void setDataNode(Node dataNode) {
        this.dataNode = dataNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getTrueState() {
        return trueState;
    }

    public State getFalseState() {
        return falseState;
    }

    public IObservableCollection<MyNode> getPredecessorNodes() {
        return predecessorNodes;
    }

    public void setPredecessorNodes(IObservableCollection<MyNode> predecessorNodes) {
        this.predecessorNodes = predecessorNodes;
    }

    public Node[] getDataNodeArray() {
        return dataNodeArray;
    }

    public void setDataNodeArray(Node[] dataNodeArray) {
        this.dataNodeArray = dataNodeArray;
    }

    public double[] getDoubleDataArray() {
        return doubleDataArray;
    }

    public void setDoubleDataArray(double[] doubleDataArray) {
        this.doubleDataArray = doubleDataArray;
    }
}
