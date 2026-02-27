package it.unibo.msrehab.rl.utils.regression;

import it.unibo.msrehab.rl.utils.Tuple;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SigmoidRegressor implements IRegressor
{
    private static final int MAX_ITERATIONS = 1000;
    private static final double CONVERGENCE_THRESHOLD = 1e-6;

    private double lr = 1;

    private double lrDampenFactor = 0.99;

    private double a = 1;
    private double b = 0;

    public SigmoidRegressor() {}

    public SigmoidRegressor(double a, double b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double predict(double x)
    {
        return 1 / (1 + Math.exp(a * (x - b)));
    }

    @Override
    public void reset()
    {
        a = 1;
        b = 0;
    }

    @Override
    public void fit(double[] x, double[] y)
    {
        if(x.length != y.length)
            throw new IllegalArgumentException("x and y must have the same length");

        if(x.length == 0)
            return;

        reset();

        a = getA();
        b = Arrays.stream(x).average().orElse(0.0);


        double prevCost = Double.MAX_VALUE;
        double deltaCost = Double.MAX_VALUE;
        int n = x.length;
        int iter = 0;
        double currLr = lr;
        do
        {
            iter ++;
            // Calcola il gradiente
            double gradA = 0;
            double gradB = 0;

            for (int i = 0; i < n; i++ )
            {
                double px = x[i];
                double py = y[i];

                double sigmoid = 1.0 / (1.0 + Math.exp(a * (px - b)));
                double error = py - sigmoid;

                gradA += error * (px - b) * sigmoid * (1 - sigmoid);
                gradB += error * -a * sigmoid * (1 - sigmoid);
            }

            // Aggiorna i parametri
            a -= currLr * gradA / n;
            b -= currLr * gradB / n;
            currLr *= lrDampenFactor;
            // Calcola il costo
            double cost = computeMeanSquaredError(x, y, a, b);
            deltaCost = Math.abs(cost - prevCost);
            prevCost = cost;
        }
        while(iter < MAX_ITERATIONS && deltaCost > CONVERGENCE_THRESHOLD);
    }


    /**
     * Calcola l'errore quadratico medio sui dati
     */
    private double computeMeanSquaredError(double[] x, double[] y, double a, double b)
    {
        return IntStream.range(0, x.length)
                .mapToDouble(i -> {
                    double prediction = 1.0 / (1.0 + Math.exp(a * (x[i] - b)));
                    return Math.pow(prediction - y[i], 2);
                })
                .average()
                .orElse(Double.MAX_VALUE);
    }

    public double getA()
    {
        return a;
    }


    public double getB()
    {
        return b;
    }


    public double getLr()
    {
        return lr;
    }

    public SigmoidRegressor setA(double a)
    {
        this.a = a;
        return this;
    }

    public SigmoidRegressor setB(double b)
    {
        this.b = b;
        return this;
    }

    public SigmoidRegressor setLr(double lr)
    {
        this.lr = lr;
        return this;
    }

    public double getLrDampenFactor()
    {
        return lrDampenFactor;
    }

    public SigmoidRegressor setLrDampenFactor(double lrDampenFactor)
    {
        this.lrDampenFactor = lrDampenFactor;
        return this;
    }
}
