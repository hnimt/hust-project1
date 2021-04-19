package sample;

import com.bayesserver.Link;
import com.yworks.yfiles.graph.IEdge;

public class MyLink {
    private String name;
    private IEdge edgeGraphic;
    private Link dataLink;
    private MyNode predecessorNode;
    private MyNode successorNode;

    public MyLink(String name, IEdge edgeGraphic, Link dataLink) {
        this.name = name;
        this.edgeGraphic = edgeGraphic;
        this.dataLink = dataLink;
    }

    public MyLink() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IEdge getEdgeGraphic() {
        return edgeGraphic;
    }

    public void setEdgeGraphic(IEdge edgeGraphic) {
        this.edgeGraphic = edgeGraphic;
    }

    public Link getDataLink() {
        return dataLink;
    }

    public void setDataLink(Link dataLink) {
        this.dataLink = dataLink;
    }

    public MyNode getPredecessorNode() {
        return predecessorNode;
    }

    public void setPredecessorNode(MyNode predecessorNode) {
        this.predecessorNode = predecessorNode;
    }

    public MyNode getSuccessorNode() {
        return successorNode;
    }

    public void setSuccessorNode(MyNode successorNode) {
        this.successorNode = successorNode;
    }
}
