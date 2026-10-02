To run the server, it is sufficient to have the following programs installed: jdk17+, maven, h2 database.

At the time of development, jdk 21, maven 3.9.9, h2 version 2.3.232 were used.

To run on jdk versions 17-23, it is necessary (in case of errors) to change the version in the pom.xml file:
<properties>
  <java.version>21</java.version>
 </properties>

It is enough to replace 21 with the appropriate jdk version on your computer.
To run the program, open the Command Prompt (Win + R -> cmd), navigate to the root folder of the project (Example):

And run the project by executing the command "mvn spring-boot:run":
After starting, open the browser and go to the address: localhost:9090


Additionally:

The database data is located in the folder “ ./Internship/DataBaseData”, and all configuration data is stored
in the file “ ./InternShip/src/main/resources/application.properties”:

In case you wish to view or modify data directly in the H2 database, you need to manually enter the login and password, as well as the absolute path to the file InternShip\DataBaseData\test.mv.db, but without the file extension (.mv.db):

To change the email address from which the email dispatch will occur, replace the value:
email.service.address
email.service.password

In the file InternShip\src\main\resources\application.properties, replace it with the one you desire (Example):


where email.service.address is your email, and email.service.password is the appropriate App Password of your email account.
