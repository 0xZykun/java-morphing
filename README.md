# Java-morphing

## If you want to run the project from provided binaries :
### 1 - Get the project from releases
### 2 - Extract the archive
    unzip java-morphing-1.0.zip
### 3 - Go to the extracted folder
    cd java-morphing-1.0
### 4 - Run the project
    ./custom-jre/bin/java -jar java-morphing-1.0.jar

## If you want to run the project from sources :
## I - Install dependencies
### 1 - Install Maven
    sudo apt install maven
### 2 - Download my custom Java Developpement Kit (JDK) from here :
    unzip jdk-17.0.11_linux-x64_bin.zip
### 3 - Extract the archive
    TBA
### 4 - Create a folder in the project root directory named "jdk"
    mkdir jdk
### 5 - Copy the extracted folder in the "jdk" folder
    cp -r /path/to/jdk-17.0.11 jdk/
### 6 - Download JavaFX SDK from here :
    https://download2.gluonhq.com/openjfx/17.0.11/openjfx-17.0.11_linux-x64_bin-sdk.zip
### 7 - Extract the archive
    unzip openjfx-17.0.11_linux-x64_bin-sdk.zip
### 8 - Create a folder in the project root directory named "lib"
    mkdir lib
### 9 - Copy the extracted folder in the "lib" folder
    cp -r /path/to/javafx-sdk-17.0.11 lib/
### 10 - Verify the paths are correct
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
    mvn clean install
### 2 - Run the project
    mvn clean javafx:run --toolchains toolchains.xml
### 3 (Optional) - Create a package containing the project and its dependencies
    mvn clean package --toolchains toolchains.xml