#!/bin/bash
javadoc -d docs -author -version -cp "./lib/opencv/opencv-440.jar:./lib/flatlaf.jar" ./src/image_processing/*.java ./src/utils/*.java ./src/ui/components/*.java ./src/ui/utils/*.java ./src/state/*.java
javac -d bin -cp "./lib/opencv/opencv-440.jar:./lib/flatlaf.jar" ./src/image_processing/*.java ./src/utils/*.java ./src/ui/components/*.java ./src/ui/utils/*.java ./src/state/*.java && java -classpath "./bin:./lib/opencv/opencv-440.jar:./lib/flatlaf.jar" -Djava.library.path="./lib/opencv/macOSx86" ui.components.MainUI
