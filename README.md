# Build Project
cd java-pos

## Build Test War
mvn clean package -Ptest -DskipTests

## Build Dev War
mvn clean package -Pdev -DskipTests

## Build Uat War
mvn clean package -Puat -DskipTests

## Build Prod War
mvn clean package -Pprod -DskipTests

## Build Supply War
mvn clean package -Psupply -DskipTests
