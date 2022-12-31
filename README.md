# Pismo demo application
## How to run

To run locally simply clone this repository and execute:
```sh
gradle bootrun
```

This should compile and start the application, including an embedded H2 database and the database schema installation.

## Running commands

You can either run the commands below, or skip to the Open API section which provides a UI for simple testing.

After starting the application as described in the 'How to run' section,the three main endpoints consist of:

- Creating an account by providing a document number
- Creating a transaction for a given account
- Retrieving an account by a given account id

### Creating an account

From the command line executing the following commands will create an account:

``` sh
curl -d '{"document_number":1234}' -H 'Content-Type: application/json' http://localhost:8080/accounts
```

The response will contain the document id provided for validation purposes,as well as the account number allocated by the system.

### Retriving account details

If you require the document number for future reference the following command will retrieve the details.
Note that you can only run this after creating an account, otherwise you will recive an "account not found" error:

```sh
curl http://localhost:8080/accounts/1
```

### Creating a transaction

Once an account has been created, it is possible to create associated transactions using the account id that was returned by the system during account creation.

```sh
curl -d '{ "account_id": 1, "operation_type_id": 4, "amount": 1}' -H 'Content-Type: application/json' http://localhost:8080/transactions
```

The operation type consists of the following:

| Operation id | Operation name       | Operation type |
|--------------|----------------------|----------------|
| 1            | PURCHASE             | Debit          |
| 2            | INSTALLMENT PURCHASE | Debit          |
| 3            | WITHDRAWAL           | Debit          |
| 4            | PAYMENT              | Credit         |

And the system will confirm that debits must have the operation type 1-3 with 4 reserved for credit amounts.

## Building and running a docker container

The following simple command will generate a local container, ready for pushing to a repository.

```sh
gradle clean docker
```
Once built you can then run the container with:
```sh
docker run --publish 8080:8080 uk.snowhunter.pismo/pismo:0.0.1-SNAPSHOT
```
Alternatively it can be executed directly via gradle with:

```sh
gradle dockerRun
```

This will start the container that was created via the `gradle docker` command, exposing the port 8080 for the application to use.
You can also combine them into `gradle clean docker dockerRun` to build and run the container in a single step.

## Open API Documentation

As mentioned above, there is an alternative to curl for testing the API via Open API alternatively known as Swagger 3.
After starting the application, navigation to the following url will provide an Open API UI.

    http://localhost:8080/swagger-ui/index.html

This will provide you with a web interface for submitting requests to the service. Alternatively if static documentation is required, this can be generated via the following command:
```sh
    gradle clean generateOpenApiDocs
```
This will generate the openapi.json file for integration into your existing Open API UI interface.