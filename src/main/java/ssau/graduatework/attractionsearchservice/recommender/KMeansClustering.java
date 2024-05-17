package ssau.graduatework.attractionsearchservice.recommender;
import java.util.*;

public class KMeansClustering {

    private int k; // количество кластеров
    private int maxIterations; // максимальное количество итераций

    private Map<Integer, List<Long>> clusters; // карта кластеров

    public KMeansClustering(int k) {
        this.k = k;
        this.maxIterations = 100;
        this.clusters = new HashMap<>(); // Инициализируйте карту кластеров
    }

    public KMeansClustering(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    public Map<Integer, List<Long>> cluster(Map<Long, Map<Long, Double>> dataMatrix) {
        // Выберите случайным образом k начальных центроидов
        List<Integer> centroids = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int centroid = random.nextInt(dataMatrix.size());
            while (centroids.contains(centroid)) {
                centroid = random.nextInt(dataMatrix.size());
            }
            centroids.add(centroid);
        }

        // Повторяйте шаги 2 и 3, пока центроиды не перестанут изменяться или не будет достигнуто заданное количество итераций
        boolean converged = false;
        int iteration = 0;
        while (!converged && iteration < maxIterations) {
            // Присвойте каждый объект данных ближайшему центроиду
            clusters = new HashMap<>();
            for (long i = 0; i < dataMatrix.size(); i++) {
                double minDistance = Double.MAX_VALUE;
                int closestCentroid = -1;
                for (int j : centroids) {
                    double distance = calculateDistance(dataMatrix.get(i), dataMatrix.get(j));
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestCentroid = j;
                    }
                }
                clusters.computeIfAbsent(closestCentroid, key -> new ArrayList<>()).add(i);
            }

            // Пересчитайте центроиды как средние значения объектов данных в каждом кластере
            Map<Integer, Map<Long, Double>> newCentroids = new HashMap<>();
            for (int i : clusters.keySet()) {
                Map<Long, Double> centroid = new HashMap<>();
                List<Long> cluster = clusters.get(i);
                for (Long feature : dataMatrix.get(0).keySet()) {
                    double sum = 0.0;
                    for (Long j : cluster) {
                        sum += dataMatrix.get(j).get(feature);
                    }
                    centroid.put(feature, sum / cluster.size());
                }
                newCentroids.put(i, centroid);
            }

            // Проверьте, не сошлись ли центроиды
            converged = true;
            for (int i : centroids) {
                Map<Long, Double> oldCentroid = dataMatrix.get(i);
                Map<Long, Double> newCentroid = newCentroids.get(i);
                if (!oldCentroid.equals(newCentroid)) {
                    converged = false;
                    break;
                }
            }

            // Обновите центроиды
            centroids = new ArrayList<>(newCentroids.keySet());

            iteration++;
        }

        return clusters;
    }

    private double calculateDistance(Map<Long, Double> dataPoint1, Map<Long, Double> dataPoint2) {
        double sum = 0.0;
        for (Long feature : dataPoint1.keySet()) {
            double diff = dataPoint1.get(feature) - dataPoint2.get(feature);
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
