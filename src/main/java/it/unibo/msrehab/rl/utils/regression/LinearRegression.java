package it.unibo.msrehab.rl.utils.regression;


public class LinearRegression implements IRegressor
{
    private double slope = 0;        // coefficiente angolare (m nella formula y = mx + b)
    private double intercept = 0;    // intercetta (b nella formula y = mx + b)
    private double rSquared;     // coefficiente di determinazione (R²)

    public LinearRegression()
    {
    }

    public LinearRegression(double slope, double intercept)
    {
        this.slope = slope;
        this.intercept = intercept;
    }

    /**
     * Calcola la regressione lineare dai dati di input
     * @param x valori x (variabile indipendente)
     * @param y valori y (variabile dipendente)
     * @throws IllegalArgumentException se gli array hanno lunghezze diverse o sono vuoti
     */
    public void fit(double[] x, double[] y)
    {
        if (x == null || y == null || x.length != y.length || x.length == 0)
            throw new IllegalArgumentException("Array x e y devono avere la stessa lunghezza e non essere vuoti");

        int n = x.length;

        double meanX = 0.0;
        double meanY = 0.0;
        for (int i = 0; i < n; i++) {
            meanX += x[i];
            meanY += y[i];
        }
        meanX /= n;
        meanY /= n;

        // Calcolo di slope e intercept
        double numerator = 0.0;
        double denominator = 0.0;
        for (int i = 0; i < n; i++) {
            double xDiff = x[i] - meanX;
            numerator += xDiff * (y[i] - meanY);
            denominator += xDiff * xDiff;
        }

        this.slope = numerator / Math.max(denominator, 1e-6);
        this.intercept = meanY - slope * meanX;

        // Calcolo R²
        double totalSS = 0.0;    // Total Sum of Squares
        double residualSS = 0.0; // Residual Sum of Squares
        for (int i = 0; i < n; i++) {
            double predicted = predict(x[i]);
            residualSS += Math.pow(y[i] - predicted, 2);
            totalSS += Math.pow(y[i] - meanY, 2);
        }
        this.rSquared = 1.0 - (residualSS / (totalSS + 1e-6));
    }

    /**
     * Predice il valore y per un dato x usando il modello addestrato
     * @param x valore x per cui predire y
     * @return valore y predetto
     */
    public double predict(double x) {
        return slope * x + intercept;
    }

    @Override
    public void reset()
    {
        slope = 0;
        intercept = 0;
        rSquared = 0;
    }

    /**
     * @return il coefficiente angolare della retta di regressione
     */
    public double getSlope()
    {
        return slope;
    }

    /**
     * @return l'intercetta della retta di regressione
     */
    public double getIntercept()
    {
        return intercept;
    }

    /**
     * @return il coefficiente di determinazione R²
     */
    public double getRSquared()
    {
        return rSquared;
    }

    /**
     * @return l'equazione della retta di regressione in formato stringa
     */
    @Override
    public String toString()
    {
        return String.format("y = %.4fx + %.4f (R² = %.4f)", slope, intercept, rSquared);
    }
}
