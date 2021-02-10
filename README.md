## Online-shop [![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger) ![stability-wip](https://img.shields.io/badge/stability-work_in_progress-lightgrey.svg)
---
An online shop application project developed for my graduate thesis with some security implementations.

![Shoptastic-login-page](https://user-images.githubusercontent.com/17550473/102817389-fef9b100-43cf-11eb-9556-3f68b654b41e.PNG)
---
![Shoptastic-contactUs-page](https://user-images.githubusercontent.com/17550473/102817446-1769cb80-43d0-11eb-9796-ec49323c7d11.PNG)

#### Table of contents 📝
---
- [Functionalities](#-Functionalities-)
- [Technologies and tools](#-Technologies-and-tools-used-) 
- [Project setup](#-Project-setup)
  - [Backend](#Backend)
  - [Frontend](#Frontend)
- [License](#-license%EF%B8%8F) 

<br/>

#### 🎨 Functionalities ![stability-wip](https://img.shields.io/badge/stability-work_in_progress-lightgrey.svg)
---
* Database migration (Flyway) :heavy_check_mark:
* OAuth2.0 Authorization and Authentication with OpenID Connect identity layer :heavy_check_mark:
   * Login with Google :heavy_check_mark:
   * Login with Facebook :heavy_check_mark:
* JSON Web Tokens :heavy_check_mark: 
* Register by sending confirmation token via email :heavy_check_mark:
   * Password protection with BCrypt hashing and salting :heavy_check_mark:
   * Scheduled job to clear expired confirmation tokens from db
* Login, Forgot password flows :heavy_check_mark:
* Role based access control
* View, search, add, edit various products
* Dark mode :heavy_check_mark:
* Light mode
* Swagger API ❓ (Optional if time available)
* Spring Boot actuator health checks
* 💳 Buy products ❓  (Optional if time available - with Stripe payment gateway) 
* Cross Browser Support and Responsive design :heavy_check_mark:

    >*Note that this is not a production ready application, so it is not thoroughly tested and verified on each environment.*
    >
    * Tested through Windows OS on the following browsers:
       * Chrome 86 :heavy_check_mark:
       * Firefox 82 :heavy_check_mark:
       * Edge 44 :heavy_check_mark:
       * IE 11 :heavy_check_mark:
     * Besides emulators, tested on real iPhone 11 Pro device on:
       * Safari mobile browser :heavy_check_mark:
       * Chrome mobile browser :heavy_check_mark:
       
<br/>       

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

<br/>

#### 🚀 Project setup 
---

<br/>

#### &nbsp;&nbsp;&nbsp;&nbsp;Backend
After cloning, import all the dependencies and do a maven clean install.
If for some reason clean install does not work, make sure that your IDE uses jdk 11.
If you dont have JDK 11 installed, download it from **[Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).**

If you have MSSQL Server Management Studio already installed and you have experience using it, then skip to the following section [Creating the database](#Creating-the-database).

<br/>

##### Installing mssql server connection with Microsoft SQL executable tools
If your system has limited permissions then you can try the [docker setup](#Creating-the-mssql-server-connection-with-Docker) solution explained below and skip this section.

Depending on the OS you are using you need to install the correct version of SQL Server Management Studio. For Windows 7 the latest supported version is SQL Server 2014 Management Studio. Link from the official download page [here](https://www.microsoft.com/en-us/download/details.aspx?id=42299).
Note that during installation for Sql Express, for the instance name it would be best if you enter "localhost" without the quotes, and do a mixed mode with a strong password which will be later used to connect via management studio. Detailed Instructions are available on this [link](https://www.sqlshack.com/how-to-install-sql-server-2014-management-studio/), and this link [here](https://www.eukhost.com/kb/how-to-install-microsoft-sql-server-express-2014/). 

For Windows 10 you can install the latest SSMS from [here](https://docs.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-ver15).

<br/>

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
* If none of these options are working for you, one final approach is to run the mssql-docker via command line/terminal window.
   * First find and open the file **docker-compose.yml**. Its located in the online-shop-application module under resources directory.
   * Replace the **{SA_PASSWORD}** with the password for the SA user you setup previously.
   * Replace the **{mountLocation}** with your prefered mount location under the C:/ drive.
   * From terminal navigate to the location where the **docker-compose.yml** is located with the cd command and run the following command: 
   * > docker-compose up

    After a few moments you should see something like this in this console:
```sh
 Starting up database 'tempdb'.
2020-10-12 11:52:20.86 spid10s     The tempdb database has 2 data file(s).
2020-10-12 11:52:20.88 spid28s     The Service Broker endpoint is in disabled or stopped state.
2020-10-12 11:52:20.88 spid28s     The Database Mirroring endpoint is in disabled or stopped state.
2020-10-12 11:52:20.89 spid28s     Service Broker manager has started.
2020-10-12 11:52:20.90 spid8s      Recovery is complete. This is an informational message only. No user action is required.
```
That means that the connection is setup correctly with the password you specified in the docker-compose.yml file. 

<br/>

##### Creating the database
Assuming you have successfully created the mssql connection, next we need to create the database. For that we will open Microsoft SQL Server Management Studio and we will connect to our new connection with the following properties:

>Server name: localhost\
Authentication: SQL Server Authentication\
Login: sa\
Password: <yourPassword>
>
*The password is the one you entered during installation of the mssql tools or in the docker-compose.yml file*

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

Now in order to run the DbMigrationApplication, you should set the missing env properties values defined in the following file DbMigrationApplication.xml considering you followed everything by the instructions above, or you can add them via the edit configuration window that IntelliJ has. But don't worry we will get to the other missing values further below:
>env name="db.name" value="online_shop"\
env name="db.username" value="sa"\
env name="db.password" value=""\
env name="encryption.key" value=""
>
![DbMigration-idea-run-configuration](https://user-images.githubusercontent.com/17550473/102818063-32890b00-43d1-11eb-907d-6ca7d26afd2c.png)

So online_shop is the db name we created in previous steps, sa is the default username that we created during the sql server connection installing.

You might wonder now how to get the encrypted password. Well I have added a test in my code for this.

Navigate to EncryptDbPasswordTest class, set the ENCRYPTION_KEY to be same as the value you entered or will enter in the environment variables above, and in the field DB_PASSWORD enter your plain db connection password (that you set during installation of the mssql server).

Now you are set and you need to run the encryptPassword() test. With that you should receive your encrypted password in the console window in the IDE.
Copy and paste that value in the environment variables for the field db.password and you should be completely fine to run the db migration now.

![encryptDbPasswordTest](https://user-images.githubusercontent.com/17550473/102818379-c5c24080-43d1-11eb-86a1-4fc2527de653.PNG)


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

Now the same thing needs to be done i.e to copy all those environment variables from the DbMigrationApplication.xml to the OnlineShopApplication.xml .

<br/>

##### Configuring the OAuth Social logins

For the social logins we need to set the following properties:
>app.oauth.tokenSecret=\
\
spring.security.oauth2.client.registration.google.clientId=\
spring.security.oauth2.client.registration.google.clientSecret=\
spring.security.oauth2.client.registration.facebook.clientId=\
spring.security.oauth2.client.registration.facebook.clientSecret=
>

Now what values should we set for these?
- Well the tokenSecret can be whatever you want and is needed to sign the JWT
- The other properties however, you will first need to create your own developer accounts for google sign-in and facebook sign-in.

<br/>

   ##### Google
   
   For google, open this [link](https://console.developers.google.com/) and google provides support with instructions [here](https://support.google.com/cloud/answer/6158849?hl=en#zippy=)

   Anyways, once you open the first link, you should see something like this:
   ![console-developers-google](https://user-images.githubusercontent.com/17550473/107443282-d6bd4180-6b38-11eb-945a-c25500cc1786.PNG)
   Then click on the arrow to create a new project and name it by your choice.
   After the project is created, you need to click the OAuth consent screen on the left sidebar.
   You can name the app and user support email freely.
   The Application home page should be set to: http://localhost:8100/home
   Also you can add an Application privacy policy link, for example: https://www.privacypolicygenerator.info/live.php?token=gF3bqGg7XVtgMg0NM0fcSfvYxlaqjiJ5 , 
   and the domain from the privacy policy (privacypolicygenerator.info) must be added to Authorized domains.
   Developer contact information is up to you to choose.

   The next step is to add the scopes. Here you will add all the non-sensitive ones i.e. the email, profile, and openid.
   ![googleScopes](https://user-images.githubusercontent.com/17550473/107444679-4f250200-6b3b-11eb-956e-dc285c42958f.PNG)

   Save it and continue to next step where you can choose whether to add test user or not. Then save and continue and your OAuth consent screen is done.

   Now, you need to click on the Credentials menu on the left sidebar, and then click the create credentials blue button, and choose the option OAuth Client ID.
   You will be asked to choose application type, and select Web application. As for the other fields you can populate them like in the following screenshot:
   ![google-credentials-oauth-client](https://user-images.githubusercontent.com/17550473/107444913-c8245980-6b3b-11eb-80c5-f4fc0d725fc2.PNG)

   Save the changes, and you will receive your google ClientID, and Client Secret, which you will need to add in application.properties

<br/>

   ##### Facebook
   
   For facebook you will need to open this [link](https://developers.facebook.com/apps/).
   Once you are logged in you should see the green Create App button. Click on it and select the option Build Connected Experiences and click Continue.
   Populate all the fields if some are not already prefilled, and choose your app display name freely. Click on Create App. Then you will be redirected to a 
   new screen and you need to find the Facebook Login, click on set up, and choose Web. The site URL that you will be asked to enter is:
   http://localhost:4400/ . Save it and skip the next steps there but rather in the left sidebar under the Facebook Login, click on settings. There you can see 
   the Client OAuth Settings, but the defaults are fine, unless you want the user to always enters his Facebook password in order to log in on the website.
   
   Next thing you need to configure is on the left sidebar to click on Settings -> Basic
   There you will have your App ID/ ClientID and App Secret/Client Secret, which you can add in application.properties, however we still 
   need to set up the needed fields properly.
   
   The display name is up to you to choose, the App Domains should be localhost, and you can also add the same privacy policy link as for google,
   https://www.privacypolicygenerator.info/live.php?token=gF3bqGg7XVtgMg0NM0fcSfvYxlaqjiJ5. Contact email should be your personal/business email.
   And at the bottom, the site URL is: http://localhost:4400/
   ![fb settingsBasic](https://user-images.githubusercontent.com/17550473/107447621-cc9f4100-6b40-11eb-8224-437ade27b77a.PNG)

   And thats it. The Advanced settings are not needed.
   
   Additional resources on how to setup App for facebook login:\
   https://magefan.com/blog/create-facebook-application <br/>
   https://www.codexworld.com/create-facebook-app-id-app-secret/ <br/>
   https://www.knowband.com/blog/mobile-app/create-facebook-app-for-facebook-login-authentication/ <br/>
   
---

<br/>

##### Configuration for the spring mail sender


---

<br/>

#### &nbsp;&nbsp;&nbsp;&nbsp;Frontend

---

<br/>

#### 📜 License️

---
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

