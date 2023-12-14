# Call Graph Generator

## Introduction

The project aims to generate a call graph for Java JAR files or .class files using ASM. The call graph includes every call path from source to target, providing researchers with a lightweight tool to obtain a project's call graph. The project's call graph achieves the precision of Class Hierarchy Analysis (CHA), offering researchers a comprehensive perspective.

## How to Use

### Install the Project

```shell
mvn clean package install
```

### Import the Project

Firstly, import the project into your Java project using Maven. Add the following dependency to your pom.xml file:

```xml
<dependency>
    <groupId>xmu.lab</groupId>
    <artifactId>callGraph</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

### Execute Call Graph Analysis

In your Java code, call the following method to perform call graph analysis:

```java
        CallGraph callGraph = CallGraph.getInstance();
        ArrayList<String> jarPaths = new ArrayList<>();
        jarPaths.add(directoryUrl.getPath());
        callGraph.addPaths(jarPaths);
        callGraph.create();
        Set<MethodCall> methodCalls = callGraph.getMethodCalls();
```

## Features

- **ASM Integration:** Utilizes ASM to parse Java bytecode for accurate call graph generation.
- **CHA Precision:** Achieves the precision of Class Hierarchy Analysis for a comprehensive view of the project's dependencies.
- **Class and JAR Analysis:** Supports the analysis of individual classes or entire JAR packages.

## TODO

- **Optimization:** Explore opportunities to enhance the performance and accuracy of call graph generation.
- **Documentation:** Expand and improve project documentation for better usability.
