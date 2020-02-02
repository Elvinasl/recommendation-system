# recommendation-system

### Build status
| Branch  | Status |
| ------------- | ------------- |
| master  |  ![](https://github.com/Elvinasl/recommendation-system/workflows/Build/badge.svg?branch=master)
| develop  | ![](https://github.com/Elvinasl/recommendation-system/workflows/Build/badge.svg?branch=develop)|

### Running the project
#### Back-end
Rename the *.properties.example to *.properties in src/main/java/recommendator/resources/
And change the properties.

| Type  | Command | Port | Database |
| ------------- | ------------- | ------------- | ------------- |
| Run project | mvn clean package cargo:run | 8080 | Not integrated |
| Integration tests |  mvn clean install -Pintegration | - | Integrated (h2) |

#### Front-end
This project includes a front-end to demonstrate the possibilities of the REST-API. The front-end can be found in the folder front-end. To run this front-end, the user should set up his/her webserver. During development, the team used Intellij's internal webserver. In Intellij, right-mouse click on the file 'front-end/index.html', select 'open with browser' and choose the browser you want. The front-end is only in HTML and JavaScript, but it needs a webserver to run because it loads HTML and JavaScript dynamically.

When the back-end server is not running on 'localhost:8080' or has some extra parameters. Then the host should be changed at the top of the file 'front-end/js/Navigator.js'.

A quick note for the front-end is that the repository already contains a dataset for uploading, this dataset can be found in the front-end folder and is called 'music.json'.

## Project 
### Background
Recommendator is a project created for the minor Advanced Java at NHL Stenden. This project is created with the mindset to learn more about REST API and Spring. Because of that, we were not allowed to use Spring boot.

### Idea
The idea for the Recommendator is to create a REST API that can be used by software developers to add a recommendation system to there product.

Developers can add datasets to their project that will be used for the predictions. Datasets contain column names and rows. These column names are used as "labels". Each label has it's own weight that can be used to specify the importance of that column.

Predictions can be made with the given data and works as follows:
The client's users like/dislike rows, given that we know which labels belong to which row we can predict which labels the user likes the most. Using the algorithm each row is given points based on the rank of how much the user will like that specific row.

### Usage front-end
When the dataset is uploaded you can navigate to the project page again and click the list-icon behind a project. This list is the recommendations when no userID is given. In order to get recommendations for a specific user, it is possible to type the userID in the text area above the recommendations table. After inserting a userID, voting buttons appear. Clicking on these buttons will change the recommendations based on the rows that are liked or disliked.

