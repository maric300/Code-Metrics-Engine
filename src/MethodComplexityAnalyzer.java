package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodComplexityAnalyzer {
    public Map<String, Integer> analyzeFile(String filePath) throws IOException {
        Map<String, Integer> complexityMap = new HashMap<>();
        Map<String, Integer> analyzedMethods = new HashMap<>();
        int totalMethods = 0;
        int nonCamelCaseMethods = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String methodName = null;
            int bracesCount = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (isMethodDeclaration(line)) {
                    if (methodName != null) {
                        totalMethods++;
                        if (!isValidMethodName(methodName)) {
                            nonCamelCaseMethods++;
                        }
                        int methodComplexity = analyzeMethod(filePath, methodName, analyzedMethods);
                        complexityMap.put(methodName, methodComplexity);
                    }
                    methodName = extractMethodNameFromDeclaration(line);
                    bracesCount = 0;
                }

                if (methodName != null) {
                    for (char c : line.toCharArray()) {
                        if (isOpenBrace(c)) {
                            bracesCount++;
                        } else if (isClosedBrace(c)) {
                            bracesCount--;
                            if (bracesCount == 0) {     //end of method
                                totalMethods++;
                                if (!isValidMethodName(methodName)) {
                                    nonCamelCaseMethods++;
                                }
                                int methodComplexity = analyzeMethod(filePath, methodName, analyzedMethods);
                                complexityMap.put(methodName, methodComplexity);
                                methodName = null;
                                break;
                            }
                        }
                    }
                }
            }
        }

        double nonCamelCasePercentage = (double) nonCamelCaseMethods / totalMethods * 100;
        System.out.println("Percentage of methods not following camelCase convention: " + nonCamelCasePercentage + "%");

        return complexityMap;
    }

    public int analyzeMethod(String filePath, String methodName, Map<String, Integer> analyzedMethods) throws IOException {
        if (analyzedMethods.containsKey(methodName)) {
            return analyzedMethods.get(methodName);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int methodComplexity = 0;
            boolean methodStarted = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (isMethodDeclaration(line)) {
                    if (methodStarted) {
                        break;
                    }
                    String currentMethodName = extractMethodNameFromDeclaration(line);
                    if (currentMethodName.equals(methodName)) {
                        methodStarted = true;
                    }
                }

                if (methodStarted) {
                    methodComplexity += countComplexity(line);

                    // Check for method calls
                    if (isMethodCall(line)) {
                        String calledMethodName = extractMethodNameFromCall(line);
                        if (!calledMethodName.contains(" ")) {
                            // Analyze the called method recursively and add its complexity
                            int calledMethodComplexity = analyzeMethod(filePath, calledMethodName, analyzedMethods);
                            methodComplexity += calledMethodComplexity;
                        }
                    }
                }
            }

            analyzedMethods.put(methodName, methodComplexity);
            return methodComplexity;
        }
    }

    public int countComplexity(String line) {
        int complexity = 0;

        Pattern pattern = Pattern.compile("\\b(if|for|while|switch)\\b");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            complexity++;
        }

        return complexity;
    }

    private boolean isMethodDeclaration(String line) {
        return line.startsWith("public") || line.startsWith("private") || line.startsWith("protected");
    }

    private boolean isMethodCall(String line) {
        return line.contains("(") && line.contains(")") && line.indexOf("(") < line.indexOf(")");
    }

    private String extractMethodNameFromDeclaration(String line) {
        return line.split("\\s+")[2].split("\\(")[0];
    }

    private String extractMethodNameFromCall(String line) {
        return line.substring(0, line.indexOf("(")).trim();
    }

    private boolean isOpenBrace(char c) {
        return c == '{';
    }

    private boolean isClosedBrace(char c) {
        return c == '}';
    }

    private boolean isValidMethodName(String methodName) {
//      //the second part is included so that it doesn't count interfaces which start with a capital I
        return methodName.matches("[a-z][a-zA-Z0-9]*") || methodName.matches("I[a-zA-Z0-9]*");
    }
}
