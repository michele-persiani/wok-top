package it.unibo.msrehab.rl.utils.regression;


/**
 * Interface for regressors
 */
public interface IRegressor
{
    void fit(double[] x, double[] y);

    double predict(double x);

    /**
     * Resets the regressor's internal state and 'unfits' it. The regressor will have to be fitted again before predictions
     */
    void reset();
}
