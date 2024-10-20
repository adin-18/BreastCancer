package com.example.breastcancer;

import org.tensorflow.lite.Interpreter;

public class CancerPrediction {
    private Interpreter tflite;
    public CancerPrediction(Interpreter interpreter) {
        this.tflite = interpreter;
    }
    public float predict(float[] inputFeatures) {
        if (inputFeatures == null || inputFeatures.length != 7) {
            throw new IllegalArgumentException("Invalid input features. Expected size: 7");
        }

        float[] output = new float[1];
        tflite.run(inputFeatures, output);
        return output[0];
    }
}
