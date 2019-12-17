# NoTest

NoTest is a test automation framework that requires NO testing code.

Its basic idea is to action method-level input/output as test vectors by running your application in real environment, and then playback the test vectors in the future (in either development environment or production environment) to do regression testing.

## Build

run gradle task "shadowJar", then the output jar will be placed at "\<project folder\>/build/libs/no-test.jar"

## Usage

### Only support linux & macOS

#### Recording

1. prepare an json file for which method you would like to record, using `com.github.zszlly.util.MethodUtils.toNoTestMethodDescription(Method)` to generate your method description and fill them to a json list.

    Here is an example:
    
        [
          "Lcom/github/zszlly/DummyMain;.add(II)I",
          "Lcom/github/zszlly/DummyMain;.addB(ILcom/github/zszlly/DummyMain$GetB;)I",
          "Lcom/github/zszlly/DummyMain;.addArray([Ljava/lang/Integer;)I",
          "Lcom/github/zszlly/DummyMain;.addAAndB(Lcom/github/zszlly/DummyMain$GetB;)I"
        ]

2. build the project as build section described.

3. copy the json properties file and built jar file "\<project folder\>/build/libs/no-test.jar" into the host where your program running at.

4. run `jps` to get your java program pid

5. run command:
        
        java -DrecordMethod=<your configuration file path> -DcaseSavingPath=<the path where recorded files will be saved to> -jar no-test.jar <your process PID>

6. then you can find the latest 10 method calls for which you filled in your configuration.

#### Playback

you can now just add your project libraries as classpath and run the following command to mock and run the test cases:
    
    java -cp "no-test.jar:<your project classpath>" com.github.zszlly.player.NoTestPlayer <recorded file path>
