#ReadWide

Introduction
ReadWide is a mobile app that helps people track the range and variety of the books they read based on metrics they can choose for themselves.

 -- Objective --
Core functions (for 4-day assignment deadline):
A user should be able to search for a book, tag it with their own unique metrics, and save it to their reading history.
Long-term goals:
In future, a user could be able to:
-	see a list of the books that they've read filtered by timeframe, by re-read counts, or other options
-	change the metrics that they choose to track
-	compare the books they've read during different time periods
-	create an account with email or other authentication to make their account accessible from multiple devices
-	access their data on a computer, import or export data to csv, share their read lists, tags and more with friends

 -- Tech Stack --
The application itself is built for Android in Java, from Android 7.0 Nougat. It connects with a MongoDB Atlas database via MongoDB Stitch and is supported by several additional libraries including:
-	To find books, the application accesses the openlibrary.org RESTful API using retrofit
-	Information is transferred in json format and converted using both Gson and Jackson libraries depending on the object required. 
-	Cover images are loaded from URL using Picasso
-	Some threading is managed with rxjava
-	Charts are made using hellocharts

 -- How you built the application --
I approached this application as a chance to learn new things and was therefore unsure how much I would be able to put together in such a short time frame. Every step of the way, I had to look up things I wasn’t familiar with, and I often tried multiple ways of solving a problem. Though this made the code itself somewhat less cohesive than might be ideal, it gave me a chance to learn a lot more as I could try a mix of approaches.

Though the central goal of the app is for users to see the data of the books that they have read by their own metrics, it would be difficult to do so without being able to first save books, which in turn is easiest if one can search for books in existing libraries. I therefore decided to start building this application by implementing a search function to find books on an existing database of books. I did some research on existing online libraries and chose openlibrary.org as it is free, open, and easy to use.

As openlibrary.org has a RESTful API, I started by learning about REST and how to integrate it in Java. I ended up using Retrofit for this purpose, creating Java Objects by sending the results through GSon. Later, I found that I needed more flexibility in the interpretation of json in specific sections which Gson could not provide, and I integrated Jackson ObjectMapper alongside the existing Gson code to achieve this.

Once I had the search function working, I created the database to store the user data, including the tags they choose to track and the books that they’ve read. As the purpose of the app is to allow the user to define these things themselves, I decided that the flexibility of MongoDB would be more appropriate for this. Initially, I created this on the localhost while I developed the next functions.

With the database established, the application could then pull information from there to populate the options when marking a book as read. This is then saved back to the database, along with any changes to the metrics that the user has defined, for instance if they choose ‘other’ for one of the criteria and enter a new value this will be saved and offered automatically in future.

The final function that I managed to look at during this week is the visual overview of the data that the user has saved. On startup, the application shows the total number of books read, and pie charts for each of the areas that a user has chosen to track. As I didn’t have time to give users the ability to add or change those, this will currently only show genre and length. However, if additional metrics were added, they would automatically be included here.

After the demonstration, I decided that I would like to attempt to deploy the database to the cloud as this would mean that I could run the app on any device for testing or submission. After some research, I chose to use the MongoDB tools, loading the database onto MongoDB Atlas with AWS, and linking to it via MongoDB Stitch. This dramatically changed the way that the application connects to the database, making some AsyncTask classes that I had created unnecessary. I am not yet willing to delete these, just in case I need them again.
