package sample;

import com.bayesserver.*;
import com.bayesserver.inference.*;

public class TestMain {
    public static void main(String[] args) throws InconsistentEvidenceException {
//        int n = 3;
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < Math.pow(2, n); j++){
//                int temp = (int) (j / Math.pow(2, n - i - 1));
//                if (temp % 2 == 0) {
//                    System.out.print("True\t");
//                } else {
//                    System.out.print("False\t");
//                }
//            }
//            System.out.println();
//        }

//        double a = Double.valueOf("0.5");
//        System.out.println(a);

        // In this example we programatically create a simple Bayesian network.
        // Note that you can automatically define nodes from data using
        // classes in BayesServer.Data.Discovery,
        // and you can automatically learn the parameters using classes in
        // BayesServer.Learning.Parameters,
        // however here we build a Bayesian network from scratch.

        Network network = new Network("Demo");

        // add the nodes (variables)

        State aTrue = new State("True");
        State aFalse = new State("False");
        Node a = new Node("A", aTrue, aFalse);

        State bTrue = new State("True");
        State bFalse = new State("False");
        Node b = new Node("B", bTrue, bFalse);

        State cTrue = new State("True");
        State cFalse = new State("False");
        Node c = new Node("C", cTrue, cFalse);

        State dTrue = new State("True");
        State dFalse = new State("False");
        Node d = new Node("D", dTrue, dFalse);

        network.getNodes().add(a);
        network.getNodes().add(b);
        network.getNodes().add(c);
        network.getNodes().add(d);

        // add some directed links

        network.getLinks().add(new Link(a, b));
        network.getLinks().add(new Link(a, c));
        network.getLinks().add(new Link(b, d));
        network.getLinks().add(new Link(c, d));

        // at this point we have fully specified the structural (graphical) specification of the Bayesian Network.

        // We must define the necessary probability distributions for each node.

        // Each node in a Bayesian Network requires a probability distribution conditioned on it's parents.

        // newDistribution() can be called on a Node to create the appropriate probability distribution for a node
        // or it can be created manually.

        // The interface Distribution has been designed to represent both discrete and continuous variables,

        // As we are currently dealing with discrete distributions, we will use the
        // Table class.

        // To access the discrete part of a distribution, we use Distribution.Table.

        // The Table class is used to define distributions over a number of discrete variables.

        Table tableA = a.newDistribution().getTable();     // access the table property of the Distribution

        // IMPORTANT
        // Note that calling Node.newDistribution() does NOT assign the distribution to the node.
        // A distribution cannot be assigned to a node until it is correctly specified.
        // If a distribution becomes invalid  (e.g. a parent node is added), it is automatically set to null.

        // as node A has no parents there is no ambiguity about the order of variables in the distribution
        tableA.set(0.1, aTrue);
        tableA.set(0.9, aFalse);

        // now tableA is correctly specified we can assign it to Node A;
        a.setDistribution(tableA);


        // node B has node A as a parent, therefore its distribution will be P(B|A)

        Table tableB = b.newDistribution().getTable();
        tableB.set(0.2, aTrue, bTrue);
        tableB.set(0.8, aTrue, bFalse);
        tableB.set(0.15, aFalse, bTrue);
        tableB.set(0.85, aFalse, bFalse);
        b.setDistribution(tableB);


        // specify P(C|A)
        Table tableC = c.newDistribution().getTable();
        tableC.set(0.3, aTrue, cTrue);
        tableC.set(0.7, aTrue, cFalse);
        tableC.set(0.4, aFalse, cTrue);
        tableC.set(0.6, aFalse, cFalse);
        c.setDistribution(tableC);


        // specify P(D|B,C)
        Table tableD = d.newDistribution().getTable();

        // we could specify the values individually as above, or we can use a TableIterator as follows
        TableIterator iteratorD = new TableIterator(tableD, new Node[]{b, c, d});
        iteratorD.copyFrom(new double[]{0.4, 0.6, 0.55, 0.45, 0.32, 0.68, 0.01, 0.99});
        d.setDistribution(tableD);


        // The network is now fully specified

        // If required the network can be saved...

        // Now we will calculate P(A|D=True), i.e. the probability of A given the evidence that D is true

        // use the factory design pattern to create the necessary inference related objects
        InferenceFactory factory = new RelevanceTreeInferenceFactory();
        Inference inference = factory.createInferenceEngine(network);
        QueryOptions queryOptions = factory.createQueryOptions();
        QueryOutput queryOutput = factory.createQueryOutput();

        // we could have created these objects explicitly instead, but as the number of algorithms grows
        // this makes it easier to switch between them

        inference.getEvidence().setState(dTrue);  // set D = True

        System.out.println(inference.getEvidence().size());

        Table queryA = new Table(a);
        inference.getQueryDistributions().add(queryA);
        inference.query(queryOptions, queryOutput); // note that this can raise an exception (see help for details)

        System.out.println("P(A|D=True) = {" + queryA.get(aTrue) + "," + queryA.get(aFalse) + "}.");

        // Expected output ...
        // P(A|D=True) = {0.0980748663101604,0.90192513368984}

        // to perform another query we reuse all the objects

        // now lets calculate P(A|D=True, C=True)
        inference.getEvidence().setState(cTrue);
        System.out.println(inference.getEvidence().size());

        // we will also return the log-likelihood of the case
        queryOptions.setLogLikelihood(true); // only request the log-likelihood if you really need it, as extra computation is involved

        inference.query(queryOptions, queryOutput);
        System.out.println(String.format("P(A|D=True, C=True) = {%s,%s}, log-likelihood = %s.", queryA.get(aTrue), queryA.get(aFalse), queryOutput.getLogLikelihood()));

        // Expected output ...
        // P(A|D=True, C=True) = {0.0777777777777778,0.922222222222222}, log-likelihood = -2.04330249506396.


        // Note that we can also calculate joint queries such as P(A,B|D=True,C=True)

    }
}
