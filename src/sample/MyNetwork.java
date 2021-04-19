package sample;

import com.bayesserver.Network;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.INode;
import com.yworks.yfiles.utils.IEnumerable;
import com.yworks.yfiles.utils.IObservableCollection;
import com.yworks.yfiles.utils.ObservableCollection;

import java.util.Iterator;
import java.util.Set;

public class MyNetwork {
    private Network network;
    private IGraph iGraph;
    private IObservableCollection<MyNode> nodes;
    private IObservableCollection<MyLink> links;

    public MyNetwork(Network network, IObservableCollection<MyNode> nodes) {
        this.network = network;
        this.nodes = nodes;
    }

    public MyNetwork() {
        this.nodes = new ObservableCollection<>();
        this.links = new ObservableCollection<>();
        this.network = new Network("Bayes Network");
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public IObservableCollection<MyNode> getNodes() {
        return nodes;
    }

    public void setNodes(IObservableCollection<MyNode> nodes) {
        this.nodes = nodes;
    }

    public IGraph getiGraph() {
        return iGraph;
    }

    public void setiGraph(IGraph iGraph) {
        this.iGraph = iGraph;
    }

    public IObservableCollection<MyLink> getLinks() {
        return links;
    }

    public void setLinks(IObservableCollection<MyLink> links) {
        this.links = links;
    }

    public MyNode getSelectedNode(INode graphicNode){
        MyNode myNode = new MyNode();
        for (MyNode myNode1 : nodes) {
            if (myNode1.getGraphicNode().equals(graphicNode)) {
                return myNode1;
            }
        }
        return myNode;
    }

    public void setPredecessorNodesOfNode(MyNode myNode){
        IObservableCollection<MyNode> tempPredecessorNodes = new ObservableCollection<>();
        for (MyLink myLink : this.getLinks()){
            if (myLink.getSuccessorNode().equals(myNode)){
                tempPredecessorNodes.add(myLink.getPredecessorNode());
            }
        }
        myNode.setPredecessorNodes(tempPredecessorNodes);
    }


}
