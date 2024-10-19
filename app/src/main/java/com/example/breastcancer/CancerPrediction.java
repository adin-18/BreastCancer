package com.example.breastcancer;

import org.tensorflow.lite.Interpreter;

public class CancerPrediction {

    private Interpreter tflite;

    // Constructor to initialize the TensorFlow Lite interpreter
    public CancerPrediction(Interpreter interpreter) {
        this.tflite = interpreter;
    }

    /**
     * Predicts the cancer risk score based on input features.
     *
     * @param inputFeatures an array of float values representing the input features
     * @return the predicted risk score as a float
     */
    public float predict(float[] inputFeatures) {
        // Check if the inputFeatures length is correct (should match model input)
        if (inputFeatures == null || inputFeatures.length != 7) { // Change 7 to your model's input size
            throw new IllegalArgumentException("Invalid input features. Expected size: 7");
        }

        float[] output = new float[1];  // Output array to hold the prediction
        tflite.run(inputFeatures, output);  // Run the model inference
        return output[0];  // Return the predicted risk score
    }
}
