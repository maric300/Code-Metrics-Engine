# Code-Metrics-Engine

The **Code Metrics Engine** is a method complexity analyzer implementation developed for a JetBrains internship. It
utilizes a recursive approach to analyze methods, effectively handling scenarios where methods call other methods.

## Implementation and idea
The program reads a Java file line by line and identifies method declarations. As it encounters each declaration,
it stores the method name along with its complexity value in a map (methodName : complexityValue). The
program also checks if each method name follows the camelCase convention. Once the entire file has been processed, the 
program calculates the percentage of methods that do not adhere to the camelCase convention and prints this information
to the console.

This implementation utilizes a recursive approach to tackle the task of methods calling other complex methods.

In the `analyzeFile` method, the program iterates through each line of the provided Java source code file. Upon encountering
a method declaration, it calls the analyzeMethod method to calculate the complexity of the method. This process continues 
recursively, allowing for the analysis of methods that call other methods within the file.

The `analyzeMethod` method recursively analyzes each method encountered. It tracks the complexity of each method by summing
up the occurrences of conditional statements (if, for, while, switch) within the method body. Additionally, it handles method
calls by recursively analyzing the called methods and aggregating their complexities.

## Complexity calculation

For simplicity, calculating the complexity of methods is currently limited to counting the number of conditional statements(if, switch, for, while).
This approach can easily be expanded by updating the `countComplexity` method to encompass alternative methods for complexity assessment.

## Usage

- In the `Main` class change the `filepath` variable to the name of the class that needs analyzing.
- Run the program and check for the results in the console.
- The `test.java` class is provided as a default file for analyzing.