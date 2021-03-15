## Online-shop [![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger) ![stability-wip](https://img.shields.io/badge/stability-work_in_progress-lightgrey.svg)
---
An online shop application project developed for my graduate thesis with some security implementations.

Here are some **screenshots** of the main screens and some major functionalities: 

Homepage: \
<img src="https://user-images.githubusercontent.com/17550473/111230521-07851080-85e8-11eb-9a93-0ba0641d7d49.PNG">
<br/>
Products, Products category, product details, product zoom hover, search products, contact page, cart, wishlist empty and wishlisted products (logged in users only),
login with google popup, login with facebook popup, forgot password dialog, forgot password email, example of responsive mobile look: 
<p float="left">
  <img src="https://user-images.githubusercontent.com/17550473/111231638-e7eee780-85e9-11eb-8dc7-b02f36e8947f.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231639-e8877e00-85e9-11eb-992b-44593c8bb56d.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231642-e8877e00-85e9-11eb-9916-b3d294ec66ce.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231643-e9201480-85e9-11eb-804a-ec77ade5141a.png" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231651-ea514180-85e9-11eb-849b-19139c7790e2.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231654-eae9d800-85e9-11eb-8c07-3cd11e1b8135.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231653-eae9d800-85e9-11eb-9f1e-382bc56f2e4d.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231623-e3c2ca00-85e9-11eb-9a8e-d0fd3e254b1b.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231625-e45b6080-85e9-11eb-840e-3649f6183df9.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231630-e58c8d80-85e9-11eb-857b-ecd9afbac534.png" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231632-e58c8d80-85e9-11eb-9f18-0c9a86d1b29d.png" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231628-e4f3f700-85e9-11eb-9866-adc9a592b633.PNG" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231629-e58c8d80-85e9-11eb-85ac-93e10721234d.png" width="300">
  <img src="https://user-images.githubusercontent.com/17550473/111231626-e4f3f700-85e9-11eb-8d08-8e550c3caaa1.PNG" height="150">
</p>

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
* Enforced HTTPS (with real certificate from Lets Encrypt) ✔️
* Register by sending confirmation token via email :heavy_check_mark:
   * Password protection with BCrypt hashing and salting :heavy_check_mark:
   * Scheduled job to clear expired confirmation tokens from db :heavy_check_mark:
* Login, Forgot password flows :heavy_check_mark:
* Role based access control
* Dark mode :heavy_check_mark:
* Swagger API ❓ (Optional if time available)
* Spring Boot actuator health checks ? (Optional if time available, only user with ADMIN role should be able to access these)
* 💳 Buy products :x:  (Not in the scope for thesis defence, payment is major topic which also includes other complicated factors) 
* Cross Browser Support and Responsive design :heavy_check_mark:

    >*Note that this is not a production installed application, so it is not thoroughly tested and verified on each environment.*
    >
    * Tested through Windows OS on the following browsers:
       * Chrome 86 :heavy_check_mark:
       * Firefox 82 :heavy_check_mark:
       * Edge 44 :heavy_check_mark:
       * IE 11 :heavy_check_mark:
     * Besides emulators, tested on real iPhone 11 Pro device on (need to be re-checked with all the new changes, last manual testing done beginning of January):
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
   The Application home page should be set to your configured address (In my case initialy was http://localhost:8100/home as you can see in the screenshot,
   but after the addition of tls certificate I've changed it to https://shop.stamkov.com).
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
   new screen and you need to find the Facebook Login, click on set up, and choose Web. The site URL that you will be asked to enter will be the one configured
   for your frontend (in my case initialy to http://localhost:4400/, but after the addition of tls certificate I've changed it to https://shop.stamkov.com).
   Save it and skip the next steps there but rather in the left sidebar under the Facebook Login, click on settings. There you can see 
   the Client OAuth Settings, but the defaults are fine, unless you want the user to always enters his Facebook password in order to log in on the website.
   
   Next thing you need to configure is on the left sidebar to click on Settings -> Basic
   There you will have your App ID/ ClientID and App Secret/Client Secret, which you can add in application.properties, however we still 
   need to set up the needed fields properly.
   
   The display name is up to you to choose, the App Domains should be your hostname (localhost or other domain name you may use),
   and you can also add the same privacy policy link as for google,
   https://www.privacypolicygenerator.info/live.php?token=gF3bqGg7XVtgMg0NM0fcSfvYxlaqjiJ5. Contact email should be your personal/business email.
   ![fb settingsBasic](https://user-images.githubusercontent.com/17550473/107447621-cc9f4100-6b40-11eb-8224-437ade27b77a.PNG)

   And thats it. The Advanced settings are not needed.
   
   Additional resources on how to setup App for facebook login:\
   https://magefan.com/blog/create-facebook-application <br/>
   https://www.codexworld.com/create-facebook-app-id-app-secret/ <br/>
   https://www.knowband.com/blog/mobile-app/create-facebook-app-for-facebook-login-authentication/ <br/>
   
---


##### Configuration for the spring mail sender

In application.properties we need to set the following two properties (the others are already set):
>
spring.mail.username=\
spring.mail.password=
>
Basically you need to add an email, and its password. However you still need to change some settings on your email, depending on the email provider.
In my case I used google, so there are two ways to allow sending emails via an application. The first and the NOT recommended way is via allow less secure apps access.
Resources to read or configure this if you choose so:
https://support.google.com/accounts/answer/6010255?hl=en#zippy=%2Cuse-more-secure-apps <br/>
https://hotter.io/docs/email-accounts/secure-app-gmail/ <br/>
https://devanswers.co/allow-less-secure-apps-access-gmail-account/

However, the **prefered way** is to use 2-Step Verification when Signing in to Google, and create an App password by clicking on the Select App dropdown, and
add a name by your choice. Then you will receive a password which you will copy in the application.properties for spring.mail.password property.

![google-email-setup](https://user-images.githubusercontent.com/17550473/107587301-befdc000-6c01-11eb-972c-ad64e800bde7.PNG)

Resources on this: <br/>
https://support.google.com/mail/answer/185833?hl=en-GB <br/>
https://devanswers.co/create-application-specific-password-gmail/ <br/>
https://www.lifewire.com/get-a-password-to-access-gmail-by-pop-imap-2-1171882 <br/>


---

##### Changes needed for the HTTPS

In application.properties we already have the following properties:

>
server.ssl.enabled-protocols=TLSv1.3\
server.ssl.key-store-type=PKCS12\
server.ssl.key-store=classpath:shoptastic-cert.p12\
server.ssl.key-store-password=\
>

The **shoptastic-cert.p12** can be created from the certificate file and the private key file provided by an **CA**, and its password needs to be provided in the property above.
There are plenty of articles online how to achieve this, most commonly with openssl or mkcert.

As for the frontend if you look into **angular.json** file (search for "ssl" inside), we need to provide the certificate -> **cert.pem**, and the private key -> **privkey.pem** files located in the src-> ssl directory (needs to be created if it doesnt exist already). 
<br/>

---

#### Changelog

**2021-03-12**
- Adapted the route transitions to include the new cart component since that route is now enabled
- Implemented cart component with cart service for storing products in local storage and adapted all affected places where any cart functionality was available
- Refactored the css and extracted common buttons styles in new scss file

**2021-03-09**
- Added search products functionality 
- Bug fixes i.e. styling issues in IE, and breadcrumbs label visual improvement

**2021-03-08**
- Implemented home component with responsive design and implemented glidejs slider carousel library
- Introduced new rest api endpoints for products
- Added missing imports for the product-detail component where drift zoom was also implemented
- Implemented product detail component with responsive design
- Improvements in product list component
- Product card component implementation and design improvements
- Added breadcrumbs component i.e. refactored existing code and moved it into a component for reuse

**2021-03-07**
- Enforced HTTPS with real certificate, and adapted the oauth configurations and endpoints in the code as well as in the developer consoles

**2021-03-01**
- Improved product-item card css layout for IE11, and aligned the white border between product name and footer to be properly aligned in all items
not depending on the product name lines length

**2021-02-28**
- Added breadcrumbs for product level hierarchy and fixed css issues in IE11 related to the breadcrumbs
- Added DB migration script for inserting admin account
- Added DB migration script for inserting product categories
- Added DB migration script for inserting various products
- Adapted previous DB schemas
- Created product list component and implementation for showing products
- Created the product subcategories, enabled the navbar, and added the routing
- Implemented the product component and product-item card component responsive and cross-browser supported designs
- Implemented rest endpoints, services, converter and repositories for retrieving all products and products per category

**2021-02-14**
- Added scheduled cron jobs to clear expired or used tokens, and added tests for that, also some additional minor improvements introduced.
- Added missing unit tests for some of the services, and did a minor code refactoring.

**2021-02-11**
- Fixed bug when updating the tokens in db once they are used, and styling fixes for the navbar.
- Refactoring some common usage of code in some places, fixing checkstyle warnings and errors.

**2021-02-07**
- Added angular material spinner on each http request using interceptors.
- Fixed bug when registering with an already existing account email to throw exception and show proper message to the end user.
- Fixed bug when non-existing user has jwt token so that it is being revoked
  (scenario where the user have been deleted, but the cookies were still being present in the browser).
- Styling and other minor improvements.
- Updated the created_on column to datetime in the create confirmation token
- Added new migration script for reset password token
- Integrated angular material in Shoptastic
- Change scss primary pink color to magenta to follow the angular material theme i started using
- Added complete forgot password functionality on FE and BE, which includes dialog component from the entry component, sending a reset password token via email, forgot passwo
  rd component that updates the password, and angular material snackbar i.e. toast message displaying if it is successful or not
- Improved BCryptPasswordEncoder strength to 13
- Fixed bug when validating for the token i.e. if it is expired now throws proper exception and the bearer token cookie is revoked completely
- Added Auth Guard functionality in order to disable access to the entry component if user is already logged in
- Styling changes and additional navbar fixes
- Added additional validations and java doc
- Various other fixes

**2021-01-30**
- Implemented registration functionality flow through sending email with confirmation token for verification with resend option as well
- Added password protection with BCrypt hashing and salting
- Added confirm account component that checks the validity of the token that users receive in their emails and accordingly redirects if valid or not
- Validation improvements
- Added password strength checker and validation on the register form on the frontend
- Added new migration scripts needed for the registration flow
- Various other code improvements

**2021-01-13**
- Renamed login component to entry component, and minor fix for the oauth window url hash and fb window size

**2021-01-11**
- Added variable for the base url in the application.properties
- Pushing the AuthController as it was moved in another changelist and thereby not added in the previous commit

**2021-01-11**
- Added new rest module
- Refactored and moved existing code in better directory structure
- Added OAuth2 OpenID authentication for Google and Facebook
- Added and configured Spring security
- Saving the OAuth jwt in httponly cookie and validating it
- Added interceptors on backend and frontend needed to check if user is logged in or not on each request
- Added guards but with not much functionality for now
- Application properties changes for the rest and oauth, but committing without the sensitive data needed for the oauth providers
- Added new flyway db migrations needed for the OAuth authentication
- Added missing java doc
- Setup the frontend proxy connection to the backend
- Added new Html and css, also some improvements
- Added logout functionality with clearing of the cookies
- Added error interceptor that for now doesnt do much, but will be used for various conditional redirects
- Added angular auth.service.ts that triggers the OAuth authentication in new window with loader and proper message
- Extracted and added new constants in one central place
- Added logic to trigger rest request on each route change to check if user is logged in or not
- Added ngx-cookie for processing and managing the cookies on the frontend
- Created custom exceptions classes
- Added global controller advice exception handler
- Created repository for the user
- Added marker interface for component scanning
- Various other models, classes and code improvements

**2020-12-13**
- Removed different type of users table i.e. there will be only one user_account now with different roles

**2020-12-08**
- Added login component with styling, lacking functionallity with oauth openid and standard register/login with email verification and jwt or jwe
- Added docker run configuration where config/environment values need to be set only before running it

**2020-11-19**
- Created fully responsive contact us page, but its missing the email sending functionality for now, also made some improvements in the navbar, and for the home page, also minor improvement for the routing transitions to have opacity transition as well

**2020-11-18**
- Added routing and transitions for routing, also made improvements in the navbar, added icons, made it fully responsive etc

**2020-11-16**
- CSS refactoring, introduced variables, and added raleway font families typography
- Added angular compile support for IE11

**2020-11-02**
- Pushing run configurations without environment data set, and project code style configuration

**2020-11-01**
- Styling fixes for mobile and desktop sizes for the navbar and the home component
- Commit for forgotten needed unversioned files i.e. home component
- Small centering fix for the home page banner text
- Additional styling improvements for the navbar and added home component, needs refactoring and code cleanup tho

**2020-10-30**
- Updated the navbar after testing on real mobile so it doesnt glitch, and changed transition effects, also prevented clicking on the menu buttons since no functionality is added yet

**2020-10-29**
- Styling improvements for the navbar

**2020-10-27**
- Extended ng serve command to open the app automatically in the browser once it compiles successfully
- Added angular frontend initial commit, and setup the responsive navbar without bootstrap

**2020-10-12**
- Moved the encryption key value as a environment value
- Added tests for encrypting and decrypting the password in order to configure the jdbc connection easier
- Improvements for creating the mssql server connection via docker so that the sa password can be specified in the .env file

**2020-10-11**
- Pushing idea configuration file so that it always uses jdk 11
- Revert "Adding java version to the pom for the maven compiler"
- Adding java version to the pom for the maven compiler

**2020-10-03**
- Made the flyway data folder a configuration property

**2020-09-09**
- Create initial mssql database schema with versioning script, and changed migration test namings to be more descriptive

**2020-09-08**
- Added integration tests for flyway migrations using the h2 in memory database, added h2 versioning and repeatable migrating sql scripts, and added java entity for the db table used in the tests

**2020-09-04**
- Implemented new module with setup and run configuration for Flyway Database migration, refactored the code including packages and namings, and also improved the pom files.

**2020-09-01**
- Created mssql database with docker, added docker compose configuration file with persistent local storage, setup the jdbc connection via application-dev properties, and encrypted all sensitive data regarding the db connection

**2020-08-10**
- Initial project setup

**2020-08-10**
- Initial commit

<br/>

#### 📜 License️

---
Copyright © 2021 stamkovs

