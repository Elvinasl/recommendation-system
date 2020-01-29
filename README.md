# recommendation-system

The idea is to create a system that could be used in order to generate recommendations for the content based on the previously user’s liked content. 
  
By using the REST API, the clientcould create a new project and fill it with the desired data that will be used for future predictions. Data has to contain labels (column names). These labels can be anything, for instance music genre or image resolutions, it’s all up to the client. 

All the data can be inserted using an API beforehand or it can be inserted on every requestin the future. In case of the second option, the system will automatically insert (or update) the data if it does not exist yet.The output of the API result might be changed based on the users behavior. Every time the user makes a request with liked/disliked content, the system will track it. Based on users previously liked content, the system will use this data to predict the next content the user might like.
