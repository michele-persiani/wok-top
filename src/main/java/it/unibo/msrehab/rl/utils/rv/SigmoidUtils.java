package it.unibo.msrehab.rl.utils.rv;

import it.unibo.msrehab.rl.utils.Tuple;

import java.util.List;

public class SigmoidUtils
{
    private SigmoidUtils()
    {
    }

    /**
     * Trova i coefficienti a e b per la funzione sigmoidBernoulli usando gradient descent con early stopping
     * @param dataset Lista di coppie (x, boolean) dove x è il valore di input e boolean è il risultato osservato
     * @param learningRate Tasso di apprendimento per il gradient descent
     * @param maxIterations Numero massimo di iterazioni per l'ottimizzazione
     * @param errorThreshold Soglia di miglioramento dell'errore per early stopping
     * @return Array double[] contenente [a, b, numeroIterazioniEffettuate]
     */

    public static double[] findSigmoidCoefficients(
            List<Tuple<Double, Boolean>> dataset,
            double learningRate,
            int maxIterations,
            double errorThreshold
    )
    {

        // Inizializzazione parametri
        double a = 1.0;
        double b = 0.0;

        // Per il tracking dell'errore
        double previousError = computeMeanSquaredError(dataset, a, b);
        double currentError;

        // Contatore iterazioni effettive
        int actualIterations = 0;

        // Gradient descent con early stopping
        for (int i = 0; i < maxIterations; i++) {
            double gradientA = 0.0;
            double gradientB = 0.0;

            // Calcolo gradienti
            for (Tuple<Double, Boolean> entry : dataset) {
                double x = entry.first;
                double y = entry.second ? 1.0 : 0.0;

                // Calcolo della predizione
                double sigmoid = 1.0 / (1.0 + Math.exp(a * (x - b)));

                // Aggiornamento gradienti
                double error = sigmoid - y;
                gradientA += error * (x - b) * sigmoid * (1 - sigmoid);
                gradientB += -error * a * sigmoid * (1 - sigmoid);
            }

            // Aggiornamento parametri
            a -= learningRate * gradientA / dataset.size();
            b -= learningRate * gradientB / dataset.size();

            // Calcola errore corrente
            currentError = computeMeanSquaredError(dataset, a, b);

            // Incrementa il contatore
            actualIterations++;

            // Verifica se il miglioramento è sotto la soglia
            double improvement = Math.abs(previousError - currentError);
            if (improvement < errorThreshold) {
                break;
            }

            previousError = currentError;
        }

        return new double[]{a, b, actualIterations};

    }

    /**
     * Calcola l'errore quadratico medio sui dati
     */
    private static double computeMeanSquaredError(
            List<Tuple<Double, Boolean>> dataset,
            double a,
            double b) {

        return dataset.stream()
                .mapToDouble(entry -> {
                    double x = entry.first;
                    double y = entry.second ? 1.0 : 0.0;
                    double prediction = 1.0 / (1.0 + Math.exp(a * (x - b)));
                    return Math.pow(prediction - y, 2);
                })
                .average()
                .orElse(Double.MAX_VALUE);
    }

}
