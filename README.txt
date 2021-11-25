Environment
JDK: 11.0.6
IDE: IntelliJ 2020.3
Spring Boot: 2.3.3.RELEASE
All other lib versions can be found in pom.xml
Menu File->settings->Plugins: Install latest Lombok plugin

How to run it
save the folder to C:\DEV\StockBeta
in IntelliJ IDE, open folder C:\DEV\StockBeta
in IDE's right hand side, click Maven, right click StockBeta, click reload project to retrieve all libs

There is a server component and client component which is unit test.
The API is built using REST api and it exposes beta calculation method via http://localhost:8842/calcbeta
It uses Dependency Injection, and all the beans are configured in StockBetaDiConfig.
We can switch to different implementations by changing bean definitions in StockBetaDiConfig.

To run Server:
StockBetaApplication is the entry point of the server component
1 Open Run/Debug configuration in IDE's top-right corner, under StockBetaApplication's working direction: add $MODULE_WORKING_DIR$
so xml files(such as logback-spring) and csv files under working folder cab be picked up
2 open class StockBetaApplication, you can run it directly in IDE

To run client:
StockBetaControllerTest is the major client class we use it to invoke REST service to get stock betas
Under test->java->com.restapi: right click StockBetaControllerTest and click Run StockBetaControllerTest

Data structure used:
StockBetaProviderImpl has the following:
Map<String, StockReturnEntry> stockReturnMap [ConcurrentHashMap]: for every ticker, this hashMap holds a corresponding StockReturnEntry
StockReturnEntry: It holds  List<StockReturnRecord> records
LinkedList<StockReturnRecord> listReturnInDuration: we use LinkedList to hold a sliding window list so when calculating beta for next day, we shift the window forward for one day
Any questions, feel free to email me at sq6688@gmail.com
