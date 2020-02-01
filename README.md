# recommendation-system

### Build status
| Branch  | Status |
| ------------- | ------------- |
| master  |  ![](https://github.com/Elvinasl/recommendation-system/workflows/Build/badge.svg?branch=master)
| develop  | ![](https://github.com/Elvinasl/recommendation-system/workflows/Build/badge.svg?branch=develop)|

### Running the project
Rename the *.properties.example to *.properties in src/main/java/recommendator/resources/
And change the properties.

| Type  | Command | Port | Database |
| ------------- | ------------- | ------------- | ------------- |
| Run project | mvn clean package cargo:run | 8080 | Not integrated |
| Integration tests |  mvn clean install -Pintegration | - | Integrated (h2) |

### Background
Recommendator is a project created for the minor Advanced Java at NHL Stenden. This project is created with the mindset to learn more about REST API and Spring. Because of that, we were not allowed to use Spring boot.

### Idea
The idea for the Recommendator is to create a REST API that can be used by software developers to add a recommendation system to there product.

Developers can add datasets to their project that will be used for the predictions. Datasets contain column names and rows. These column names are used as "labels". Each label has it's own weight that can be used to specify the importance of that column.

Predictions can be made with the given data and works as follows:
The client's users like/dislike rows, given that we know which labels belong to which row we can predict which labels the user likes the most. Using the algorithm each row is given points based on the rank of how much the user will like that specific row.
