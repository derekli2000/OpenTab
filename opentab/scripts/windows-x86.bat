javac -d bin -cp ".\lib\opencv\opencv-440.jar;.\lib\flatlaf.jar" ./src/image_processing/*.java ./src/utils/*.java ./src/ui/components/*.java ./src/ui/utils/*.java ./src/state/*.java && java -cp ".\bin;.\lib\opencv\opencv-440.jar;.\lib\flatlaf.jar" -Djava.library.path=".\lib\opencv\x86" ui.components.MainUI