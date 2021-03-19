# Stock Brokerage Application
## Description
This application is a RESTful web app used to manage stock trading. It allows for the users to open accounts, add and withdrawl funding for the these accounts, and trade stocks. This includes buying and selling off the base market, as well as between 2 accounts that exist within this application. Administrators can update the stocks available in the system and their prices. The API allows for updates on users and accounts as well.
## Usage
The application is a Spring Boot app. It can by run by using maven. The data needs to be stored in a PostgreSQL database. The connection to the database needs to be configured locally. This is done by updating the local environment variables: DB_URL, DB_USERNAME, and DB_PASSWORD.
