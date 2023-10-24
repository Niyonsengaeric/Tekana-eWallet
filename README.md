# Tekana-eWallet Back-End API

## Introduction

The Tekana-eWallet Back-End API is the result of the Back-End Engineer Challenge, where the primary goal was to revamp the legacy application "Tekana-eWallet" used globally for money transfers. This README provides an overview of the project, emphasizing the core features implemented in the API.

## Solution

For this project, I have implemented the API using Spring Boot as the back-end framework and MySQL as the database system. The API is designed to provide a robust and efficient platform for managing customer accounts, wallets, and money transactions.

### Rate Limiter

One of the key features of this solution is the implementation of a rate limiter. This rate limiter serves a dual purpose by limiting the number of requests from individual users and controlling the overall traffic on the system. By doing so, it helps enhance the performance of the API and ensures system stability.

### Database Optimization

To optimize database performance, I have utilized indexing on MySQL tables. Indexing is a crucial technique that improves the speed of querying and enhances the overall responsiveness of the API.

This solution is geared towards achieving high performance and reliability, ensuring that the "Tekana-eWallet" API is well-prepared to handle the demands of 1 million customers globally.


## Core Features (Required)

### Customer Management

- **Customer Registration:**
  - Allows customers to create and manage their accounts.
  - Provides authentication and authorization for secure access.

### Wallet Management

- **Create Wallet:** Enables the creation of wallets for managing funds.
- **Money Transactions:**
  - Facilitates secure money transactions between customers.
  - Allows customers to view their transaction histories.

## Additional Features (Enhancements)

### Customer Management

- **Get Customer List:** Retrieves a list of all registered customers.
- **Get Specific Customer Info:** Fetches detailed information for a specific customer.

### Wallet Management

- **Get All Wallets:** Retrieves a list of all wallets in the system.
- **Wallet Deposit:** Allows customers to deposit funds into their wallets.
- **Get Customer Wallet:** Retrieves wallet information for a specific customer.
- **Get AccountNumber Wallet:** Fetches wallet information by account number.

### Transaction Handling

- **Create Transaction:** Facilitates the creation of money transactions between customers.
- **Get Transactions:** Retrieves a list of all transactions.
- **Get Specific Transaction:** Fetches details of a specific transaction.

### Wallet Activity Logs

- **Get Activity Logs:** Retrieves logs of wallet activity.
- **Get Activity Logs by Transaction:** Fetches activity logs associated with specific transactions.

## Running the Project

To run the Spring Boot project locally, please follow the steps below:

### Prerequisites

Before getting started, ensure that you have the following prerequisites in place:

- [Java Development Kit (JDK) 1.8 or higher](https://adoptopenjdk.net/) - Download and install the appropriate JDK version for your operating system.

- [Maven](https://maven.apache.org/download.cgi) - The project uses Maven as a build tool. You can download it from the official website.
  
- [MySQL Database](https://dev.mysql.com/downloads/mysql/): Set up a MySQL database and note the connection details (URL, username, password) for configuration.

### Setup

1. Clone the repository:

Follow these steps to set up and run the Tekana-eWallet Back-End API locally:


```bash
git clone https://github.com/Niyonsengaeric/Tekana-eWallet.git
```

2. create MySQL  data base (e.g:tekana_wallet)

3. Configure the connection details in the application.properties file. Here is an example of how your application.properties file should look like:

```
server.port=8080
security.basic.enabled=false

spring.datasource.url=jdbc:mysql://localhost:3306/tekana_wallet
spring.datasource.username=mysqlUser
spring.datasource.password=mysqlUserPassword
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update

## Hibernate Logging
##logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.SQL=OFF

# Initialize the datasource with available DDL and DML scripts
spring.datasource.initialization-mode=always

## Jackson Properties
spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false
spring.jackson.time-zone=GMT+2

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=false
system.rate.limit=3
user.rate.limit=2
```

4. Run the project

The application will be accessible at http://localhost:8080.

## API Documentation
The API documentation is available in the Postman API documentation format [here](https://documenter.getpostman.com/view/8164226/2s9YRDyq6M).

The following APIs are implemented:

## APIs

Here are the available endpoints and their descriptions for the Tekana-eWallet Back-End API:

| Endpoint | Method | Description | Authorization |
|---------|--------|-------------|---------------|
| `/api/v1/customer/register` | POST | Register a new customer account. | No |
| `/api/v1/customer/login` | POST | Authenticate and log in a customer. | No |
| `/api/v1/customer/list` | GET | Retrieve a list of all registered customers. | Yes |
| `/api/v1/customer/{customerId}` | GET | Fetch detailed information for a specific customer. | Yes |
| `/api/v1/wallet` | POST | Create a new wallet for managing funds. | Yes |
| `/api/v1/wallet` | GET | Retrieve a list of all wallets. | Yes |
| `/api/v1/wallet/deposit/{walletId}` | POST | Deposit an amount to a specific wallet. | Yes |
| `/api/v1/wallet/customer/{customerId}` | GET | Retrieve wallet information for a specific customer. | Yes |
| `/api/v1/wallet/customer` | GET | Retrieve a wallet by account number. | Yes |
| `/api/v1/transaction` | POST | Create a new transaction by specifying sender account, receiver account, and the transaction amount. | Yes |
| `/api/v1/transaction` | GET | Retrieve a list of all transactions. | Yes |
| `/api/v1/transaction/{id}` | GET | Retrieve a transaction by its ID. | Yes |
| `/api/v1/wallet-activity-logs` | GET | Retrieve a list of all wallet activity logs. | Yes |
| `/api/v1/wallet-activity-logs/transaction/{transactionId}` | GET | Retrieve wallet activity logs associated with a specific transaction by providing the transaction ID. | Yes |

In this updated table, there is a new "Authorization" column that indicates whether an endpoint is protected and requires authentication (Yes) or is open for public access (No). This addition provides clarity on the authentication requirements for each endpoint.



## Communication

If you have any questions or suggestions about the project, please feel free to contact me at [niyeric11@gmail.com].