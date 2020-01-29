# recommendation-system

The idea is to create a system that could be used to generate recommendations for the content based on the previous user’s liked content.

By using the REST API, the client could create a new project and fill it with the desired data that will be used for future predictions. Data has to contain labels (column names). These labels can be anything, for instance, music genre or image resolutions, it’s all up to the client.

All the data can be inserted using an API beforehand or it can be inserted on every request in the future. In the case of the second option, the system will automatically insert (or update) the data if it does not exist yet. The output of the API result might be changed based on the user's behavior. Every time the user makes a request with liked/disliked content, the system will track it. Based on the user's previously liked content, the system will use this data to predict the next content the user might like.
