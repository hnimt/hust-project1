package sample;

import com.bayesserver.Network;
import com.bayesserver.inference.*;

public class MyCalculator {
    private InferenceFactory inferenceFactory;

    public MyCalculator() {
    }

    public InferenceFactory getInferenceFactory() {
        return inferenceFactory;
    }

    public void setInferenceFactory(InferenceFactory inferenceFactory) {
        this.inferenceFactory = inferenceFactory;
    }

}
