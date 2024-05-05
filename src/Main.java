import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String filePath = "test.java";
        MethodComplexityAnalyzer methodComplexityAnalyzer = new MethodComplexityAnalyzer();
        try {
            Map<String, Integer> complexityMap = methodComplexityAnalyzer.analyzeFile(filePath);
            System.out.println("3 Most Complex Functions:");
            complexityMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3)
                    .forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}