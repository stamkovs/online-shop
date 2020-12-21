## Online-shop [![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger) ![stability-wip](https://img.shields.io/badge/stability-work_in_progress-lightgrey.svg)
---
A online shop application project developed for my graduate thesis with some security implementations.

#### Table of contents 📝
---
- [Functionalities](#-Functionalities-)
- [Technologies and tools](#-Technologies-and-tools-used-) 
- [Project setup](#-Project-setup)
  - [Backend](#Backend)
  - [Frontend](#Frontend)
- [License](#-license%EF%B8%8F) 

#### 🎨 Functionalities ![stability-wip](https://img.shields.io/badge/stability-work_in_progress-lightgrey.svg)
---
* Database migration
* OAuth2.0 Authentication
* JSON Web Tokens 
* Login via email or facebook ❓ (Optional if time available)
* Role based access control
* View, search, add, edit various products
* Dark mode
* Swagger API ❓ (Optional if time available)
* Spring Boot actuator health checks
* 💳 Buy products ❓  (Optional if time available - with Stripe payment gateway) 
* Cross Browser Support and Responsive design 

    >*Note that this is not a production ready application, so it is not thoroughly tested and verified on each environment.*
    >
    * Tested through Windows OS on the following browsers:
       * Chrome 86
       * Firefox 82
       * Edge 44
       * IE 11
     * Besides emulators, tested on real iPhone 11 Pro device on:
       * Safari mobile browser
       * Chrome mobile browser
#### 🧰 Technologies and tools used 🔨
---
* Spring Boot
* Docker
* MSSQL
* Flyway
* JUnit5
* Mockito
* Angular (#Todo)
* HTML5 and CSS3
#### 🚀 Project setup 
---

#### &nbsp;&nbsp;&nbsp;&nbsp;Backend
After cloning, import all the dependencies and do a maven clean install.
If for some reason clean install does not work, make sure that your IDE uses jdk 11.
If you dont have JDK 11 installed, download it from **[Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).**

If you have MSSQL Server Management Studio already installed and you have experience using it, then skip to the following section [Creating the database](#Creating-the-database).

##### Installing mssql server connection with Microsoft SQL executable tools
If your system has limited permissions then you can try the [docker setup](#Creating-the-mssql-server-connection-with-Docker) solution explained below and skip this section.

Depending on the OS you are using you need to install the correct version of SQL Server Management Studio. For Windows 7 the latest supported version is SQL Server 2014 Management Studio. Link from the official download page [here](https://www.microsoft.com/en-us/download/details.aspx?id=42299).
Note that during installation for Sql Express, for the instance name it would be best if you enter "localhost" without the quotes, and do a mixed mode with a strong password which will be later used to connect via management studio. Detailed Instructions are available on this [link](https://www.sqlshack.com/how-to-install-sql-server-2014-management-studio/), and this link [here](https://www.eukhost.com/kb/how-to-install-microsoft-sql-server-express-2014/). 

For Windows 10 you can install the latest SSMS from [here](https://docs.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-ver15).

##### Creating the mssql server connection with Docker
This section is in case you were restricted to create your own connection and database via mssql studio.
If you opted for creating the database via Microsoft SQL Server Management Studio and it went successfull then skip to the next section [Creating the database](#Creating-the-database).

---
If you have an older computer or dont have Windows 10 its better to skip this docker setup since various complicated issues may appear during installation and rely to [MSSQL instalation](#Installing-mssql-server-connection-with-Microsoft-SQL-executable-tools) without docker which is explained in the section above.

Depending on your OS you can follow the instructions to download and install Docker on this link **[here](https://hub.docker.com/editions/community/docker-ce-desktop-windows)**, and if any issues appear during installation troubleshoot them or skip to [MSSQL instalation](#Installing-mssql-server-connection-with-Microsoft-SQL-executable-tools) without docker.

Once you verify that docker is successfully installed, we will need to setup the mssql server connection. 

There are few ways to setup this configuration.

* If you have **IntelliJ version 2020.2** or **newer**:
   *  Open the project
   *  In the already created Run configurations window in Intellij, open the "docker-start-mssql-db"
   *  Add the missing values for "SA_PASSWORD" and the docker mount location that you have configured in the previous steps, click apply and OK.
   *  Run the docker-start-mssql-db
   ![docker-run-configuration](https://user-images.githubusercontent.com/17550473/102814498-066a8b80-43cb-11eb-9beb-d3b0646c0824.PNG)
***
* For **IntelliJ** versions **older** than **2020.2**
   *  Open Docker Desktop → Settings → General: Mark the "Expose daemon on tcp://localhost:2375 without TLS" to be allowed. Apply and restart Docker Desktop.
   *  Open the online-shop project -> open "Edit Configurations..." and on the + sign search for Docker → Docker-Compose. 
   *  By default Docker should be added in the Server dropdown, and now click on the three dots right after that dropdown.
   *  Under the "Connect to Docker Daemon with:", choose the TCP Socket option, and for the Engine API URL, enter "tcp://localhost:2375" without the quotes. Now wait a few seconds and below in the same window you should see the message "Connection successful". Then click OK.
   * Run the service.
   ![docker-run-configuration-older-IntelliJ](https://user-images.githubusercontent.com/17550473/102815019-f7380d80-43cb-11eb-9fb9-18281620c651.PNG)

***
* If none of these options are working for you, one final approach is to run the mssql-docker via command line/terminal window considering you have entered the SA_PASSWORD value and the mountLocation as mentioned previously.
   * Open the online-shop project, and from the root directory enter the following commands in terminal:
   * > cd \online-shop-application\src\main\resources && docker-compose up

After a few moments you should see something like this in this console:
```sh
 Starting up database 'tempdb'.
2020-10-12 11:52:20.86 spid10s     The tempdb database has 2 data file(s).
2020-10-12 11:52:20.88 spid28s     The Service Broker endpoint is in disabled or stopped state.
2020-10-12 11:52:20.88 spid28s     The Database Mirroring endpoint is in disabled or stopped state.
2020-10-12 11:52:20.89 spid28s     Service Broker manager has started.
2020-10-12 11:52:20.90 spid8s      Recovery is complete. This is an informational message only. No user action is required.
```
That means that the connection is setup correctly with the password you specified in the .env file. 

##### Creating the database
Assuming you have successfully created the mssql connection, next we need to create the database. For that we will open Microsoft SQL Server Management Studio and we will connect to our new connection with the following properties:

>Server name: localhost\
Authentication: SQL Server Authentication\
Login: sa\
Password: <yourPassword>
>
*The password is the one you entered during installation of the mssql tools or in the .env file*

After you are successfully connected, expand the localhost, do a right click on Databases -> New database, and in the window that will popup, in the Database name field, enter **online_shop**. Also you can change the default collation when writing queries explained [here](https://docs.microsoft.com/en-us/sql/relational-databases/collations/set-or-change-the-database-collation?view=sql-server-ver15#to-set-or-change-the-database-collation). After you successfully created the online_shop db, if it is not showing then do a right click again on the Databases in the left sidebar and click refresh in order to see that the online_shop database is really created.

Note that if you installed the MSSQL tools without Docker, you might need to configure the TCP/IP to enabled and the port to 1433 which is explained in details [here](https://docs.microsoft.com/en-us/sql/database-engine/configure-windows/configure-a-server-to-listen-on-a-specific-tcp-port?view=sql-server-ver15)

Now we need to populate our database with the test data.
For that I've already created a db migration tool. But if you try to run it you will see that it fails.
Thats because we havent set up the jdbc connection yet, and if you open application-dev.properties inside the online-shop-db-migration module, you can see everything is defined and we just need to provide the values for the fields in curly braces {}, but we wont change them here in application-dev.properties, as there is a better way explained few lines below.

>spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=${db.name}\
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver\
spring.datasource.username=${db.username}\
spring.datasource.password=${db.password}\
property.encryption.key=${encryption.key}
>

Now in order to run the DbMigrationApplication, you should set the missing env properties values defined in the following file DbMigrationApplication.xml considering you followed everything by the instructions above, and don't worry we will get to the other missing values further below:
>env name="db.name" value="online_shop"\
env name="db.username" value="sa"\
env name="db.password" value=""\
env name="encryption.key" value=""
>
So online_shop is the db name we created in previous steps, sa is the default username that we created during the sql server connection installing.
You might wonder now how to get the encrypted password. Well I have added a test in my code for this.
Navigate to EncryptDbPasswordTest class, set the ENCRYPTION_KEY to be same as the value you entered or will enter in the environment variables above, and in the field DB_PASSWORD enter your plain db connection password (that you set during installation of the mssql server).
Now you are set and you need to run the encryptPassword() test. With that you should receive your encrypted password in the console window in the IDE.
Copy and paste that value in the environment variables for the field db.password and you should be completely fine to run the db migration now.

Run the DbMigrationApplication and you should see something like this in the console which means the migration run successfully, and if you open the database via sql management studio you should see that new tables are added to the database.

```sh
2020-10-12 17:12:32.529  INFO 9232 --- [           main] c.s.o.s.f.service.DbMigrationService     : === Data migration - FINISH ===
2020-10-12 17:12:32.529  INFO 9232 --- [           main] c.s.o.s.f.app.DbMigrationApplication     : The DB migration finished with the DB updates.
2020-10-12 17:12:32.537  INFO 9232 --- [       Thread-2] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
2020-10-12 17:12:32.538  INFO 9232 --- [       Thread-2] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2020-10-12 17:12:32.543  INFO 9232 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2020-10-12 17:12:32.555  INFO 9232 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

Process finished with exit code 0

```

Now the last thing to do is to copy all those environment variables from the DbMigrationApplication to the OnlineShopApplication which is in another module and it starts the shop application.


#### &nbsp;&nbsp;&nbsp;&nbsp;Frontend
&nbsp;
#### 📜 License️
---
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)


