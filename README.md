# ReadWide
ReadWide helps people track the range and variety of the books they read based on the metrics they choose.

 -- Objective --
Core functions (for 4 day assignment deadline):
A user should be able to search for a book, tag it with their own unique metrics, and save it to their reading history.
Long-term goals:
In future, a user could be able to:
 - see a list of the books that they've read byt timeframe or by re-read counts
 - change the metrics that they choose to track
 - compare the books they've read for different time periods
 - create an account with email or other authentication to make their account accessible from multiple devices
 - access their data on a computer, import or export data to csv, share their read lists, tags etc with friends

 -- Tech Stack --
The application itself is built with Android in Java, from Android 7.0 Nougat. It connects with a MongoDB Atlas database via MongoDB Stitch and is supported by several additional libraries including:
- To find books, the application accesses the openlibrary.org RESTful API using retrofit
- Information is transferred in json format and converted using both Gson and Jackson libraries depending on the object required. Specifically, Jackson is used to convert json to HashMaps and back, while Gson is used for everything else.
- Cover images are loaded from URL using Picasso
- Some threading is managed with rxjava
- Charts are made using hellocharts

 -- How you built the application --
I built the application primarily using AndriodStudio with Java.
Though the central goal of the app is for users to see the data of the books that they have read by their own metrics, it would be difficult to do so without being able to save books, which in turn is easiest if one can search for books in existing libraries.
I therefore decided to start building this application by implementing a search function to find books. I did some research on existing online libraries and chose openlibrary.org as it is free, open, and easy to use.
As openlibrary.org has a RESTful API, I started by learning about REST and how to integrate it in Java. I ended up using Retrofit for this purpose, creating Java Objects by sending the results through GSon.
Some challenges I encountered at this stage included establishing the connection correctly.
- build localhost mongoDB database
- fill radio buttons from metrics in db
- save book with marked tags to db
- let user enter new answer to metrics
- save new answers to database to be options in future
- display total books read on start up
- display metrics as piecharts
- deploy database to cloud
- testing on other device found new issues

 -- How to run the application --
If you trust me as an "unknown source" you can download the "app-release.apk" from the following google drive folder:
https://drive.google.com/open?id=1vQKu7bOqpa3nUxJ4CshaztNimdY99Y0d

 -- How to test the application --
At this stage, testing is been done entirely by running on an Android Emulator and on a Nokia 7 plus running Android version 9. Though JUnit tests would be helpful, I decided this was not my priority given the tight timeline.

 -- Software tools used --
Android Studio, 
MongoDB via Ubuntu on the localhost, then later moved to Atlas accessed through Stitch.

 -- Tangible results --
I achieved considerably more than I thought I would be able to in a single week, with an application c

 -- Screenshots or animated GIFs showing user flows (if customer-facing) --

 -- Links to web pages or installers for desktop applications --

 -- Links to code --
https://github.com/peggysaurus/ReadWide
