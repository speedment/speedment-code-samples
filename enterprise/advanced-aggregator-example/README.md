# speedment-aggregator-example
Example on how to use the Speedment Enterprise aggregator. 

## Installation
These examples are intended to be used together with the [MySQL Employees](https://dev.mysql.com/doc/employee/en/) database. Make sure the database exists accessible with username `root` and password `password` on `localhost:3306` before running the application. Alternatively you can configure a different data source by modifying the [application.yml](application.yml)-file.

## Usage
Three controllers are defined.

| HTTP Method | Endpoint            | Description                                              |
| ----------- | ------------------- | -------------------------------------------------------- |
| GET         | /salary/mean        | Compute the mean salary of men and women                 |
| GET         | /salary/variance    | Compute the mean and variance in salary of men and women |
| GET         | /salary/correlation | Compute Pearson's Correlation Coefficient                |