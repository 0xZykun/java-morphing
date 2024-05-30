# Java-morphing

## If you want to run the project from provided binaries :
### 1 - Get the project from releases
### 2 - Extract the archive
    unzip java-morphing-1.0-distribution.zip
### 3 - Run the project
    ./custom-jre/bin/java -jar java-morphing-1.0.jar

## If you want to run the project from sources :
## I - Install dependencies
### 1 - Install Maven
    sudo apt install maven
### 2 - Install git-lfs
    sudo apt install git-lfs
### 3 - Initialize git-lfs
    git-lfs install
### 4 - Pull lfs files
    git-lfs pull
### 5 - Go to jdk folder
    cd jdk
### 6 - Extract the JDK archive
    unzip jdk-17.0.11.zip
### 7 - Go to the lib directory
    cd ../lib/
### 8 - Extract the JavaFX JDK
    unzip javafx-sdk-17.0.11.zip
### 9 - Verify the paths are correct
    .
    ├── jdk
    │   └── jdk-17.0.11
    │       ├── bin
    │       ├── ...
    └── lib
        └── javafx-sdk-17.0.11
            ├── bin
            ├── ...
## II - Run the project
### 1 - Configure dependencies in Maven
    mvn clean install --toolchains toolchains.xml
### 2 - Run the project
    mvn clean javafx:run --toolchains toolchains.xml
### 3 (Optional) - Create a package containing the project and its dependencies
    mvn clean package --toolchains toolchains.xml