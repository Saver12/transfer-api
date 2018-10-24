# Money Transfer API

RESTful API for money transfers between accounts

## Technologies
* Jersey 2.7
* Jetty Container 9.3.24.v20180605
* JUnit 4.8.2
* Lombok 1.18.2 (for IntelliJ IDEA 'Enable annotation processing' should be turned on)
* Genson 0.98 (for JSON conversion)
* ConcurrentHashMap is used as in-memory datastore for demonstration purpose

## Requirements
* Java 8
* Maven 3+

## Build application
Checkout the project, then run
```
    mvn clean package
```
## Run tests
Integration tests implemented with jersey-test-framework, also used concurrent-junit by vmlens for concurrent requests
```
    mvn clean test
```
## Run application
Build application, then run
```
    java -jar transfer-1.0-SNAPSHOT.jar
```

## API methods description
### Users
#### Create user
```
    POST localhost:8080/api/users
    {
        "name": "John",
        "email": "jk@yahoo.com"
    }
```
Response:
```
    HTTP 201 Created
    {
        "id": 1,
        "name": "John",
        "email": "jk@yahoo.com"
    }
```
#### Update user
```
    PUT localhost:8080/api/users/1
    {
        "name": "Max",
        "email": "maxx@gmail.com"
    }
```
Response:
```
    HTTP 200 OK
    {
        "id": 1,
        "name": "Max",
        "email": "maxx@gmail.com"
    }
```
#### Delete user
```
    DELETE localhost:8080/api/users/1
```
Response:
```
    HTTP 200 OK
    {
        "id": 1,
        "name": "John",
        "email": "jk@yahoo.com"
    }
```
#### Get all users
```
    GET localhost:8080/api/users
```
Response:
```
    HTTP 200 OK
    [
        {
            "id": 1,
            "name": "John",
            "email": "jk@yahoo.com"
        },
        {
            "id": 2,
            "name": "Mary",
            "email": "maryann@mail.com"
        }
    ]
```
#### Get one user
```
    GET localhost:8080/api/users/1
```
Response:
```
    HTTP 200 OK
    {
        "id": 1,
        "name": "John",
        "email": "jk@yahoo.com"
    }
```
### Accounts
#### Create account
```
    POST localhost:8080/api/accounts
    {
        "userId": 1,
        "balance": "400",
        "currency": "USD"
    }
```
Response:
```
    HTTP 201 Created
    {
        "balance": 400,
        "currency": {
            "currencyCode": "USD",
            "defaultFractionDigits": 2,
            "displayName": "US Dollar",
            "numericCode": 840,
            "symbol": "$"
        },
        "id": 1,
        "userId": 1
    }
```
#### Update account
```
    PUT localhost:8080/api/accounts/1
    {
	    "currency" : "EUR",
	    "balance" : 5000
    }
```
Response:
```
    HTTP 200 OK
    {
        "balance": 5000,
        "currency": {
            "currencyCode": "EUR",
            "defaultFractionDigits": 2,
            "displayName": "Euro",
            "numericCode": 978,
            "symbol": "EUR"
        },
        "id": 1,
        "userId": 1
    }
```
#### Delete account
```
    DELETE localhost:8080/api/accounts/1
```
Response:
```
    HTTP 200 OK
    {
        "balance": 5000,
        "currency": {
            "currencyCode": "EUR",
            "defaultFractionDigits": 2,
            "displayName": "Euro",
            "numericCode": 978,
            "symbol": "EUR"
        },
        "id": 1,
        "userId": 1
    }
```
#### Get all accounts
```
    GET localhost:8080/api/accounts
```
Response:
```
    HTTP 200 OK
    [
        {
            "balance": 5000,
            "currency": {
                "currencyCode": "EUR",
                "defaultFractionDigits": 2,
                "displayName": "Euro",
                "numericCode": 978,
                "symbol": "EUR"
            },
            "id": 1,
            "userId": 1
        },
        {
            "balance": 150,
            "currency": {
                "currencyCode": "GBP",
                "defaultFractionDigits": 2,
                "displayName": "British Pound Sterling",
                "numericCode": 826,
                "symbol": "GBP"
            },
            "id": 2,
            "userId": 3
        }
    ]
```
#### Get one account
```
    GET localhost:8080/api/accounts/1
```
Response:
```
    HTTP 200 OK
    {
        "balance": 5000,
        "currency": {
            "currencyCode": "EUR",
            "defaultFractionDigits": 2,
            "displayName": "Euro",
            "numericCode": 978,
            "symbol": "EUR"
        },
        "id": 1,
        "userId": 1
    }
```
### Transfers
#### Create transfer
```
    POST localhost:8080/api/trans
    {
        "sourceAccountId" : 1,
        "destinationAccountId" : 3,
        "currency" : "USD",
        "amount" : 150
    }
```
Response:
```
    HTTP 201 Created
    {
        "id": 1,
        "sourceAccountId": 1,
        "destinationAccountId": 3,
        "amount": 150,
        "currency": {
            "currencyCode": "USD",
            "defaultFractionDigits": 2,
            "displayName": "US Dollar",
            "numericCode": 840,
            "symbol": "$"
        },
        "message": "success",
        "status": "EXECUTED"
    }
```
Successful transfer requirements:
* Source and destination accounts/users exist
* Currency of transfer, source and destination accounts are identical
* Transfer amount is greater than zero
* Sufficient funds on the source account

If one of these requirements is not met, transfer is created with FAILED status.
#### Delete transfer
```
    DELETE localhost:8080/api/trans/1
```
Response:
```
    HTTP 200 OK
    {
        "id": 1,
        "sourceAccountId": 1,
        "destinationAccountId": 3,
        "amount": 150,
        "currency": {
            "currencyCode": "USD",
            "defaultFractionDigits": 2,
            "displayName": "US Dollar",
            "numericCode": 840,
            "symbol": "$"
        },
        "message": "success",
        "status": "EXECUTED"
    }
```
#### Get all transfers
```
    GET localhost:8080/api/trans
```
Response:
```
    HTTP 200 OK
    [
        {
            "id": 1,
            "sourceAccountId": 1,
            "destinationAccountId": 3,
            "amount": 150,
            "currency": {
                "currencyCode": "USD",
                "defaultFractionDigits": 2,
                "displayName": "US Dollar",
                "numericCode": 840,
                "symbol": "$"
            },
            "message": "success",
            "status": "EXECUTED"
        },
        {
            "id": 2,
            "sourceAccountId": 2,
            "destinationAccountId": 3,
            "amount": 750,
            "currency": {
                "currencyCode": "USD",
                "defaultFractionDigits": 2,
                "displayName": "US Dollar",
                "numericCode": 840,
                "symbol": "$"
            },
            "message": "transfer failed. check payment requirements",
            "status": "FAILED"
        }
    ]
```
#### Get one transfer
```
    GET localhost:8080/api/trans/1
```
Response:
```
    HTTP 200 OK
    {
        "id": 1,
        "sourceAccountId": 1,
        "destinationAccountId": 3,
        "amount": 150,
        "currency": {
            "currencyCode": "USD",
            "defaultFractionDigits": 2,
            "displayName": "US Dollar",
            "numericCode": 840,
            "symbol": "$"
        },
        "message": "success",
        "status": "EXECUTED"
    }
```