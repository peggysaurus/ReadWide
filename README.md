# ReadWide
ReadWide helps people track the range and variety of the books they read based on the metrics they choose.

 -- Objective --
Core functions (for 4 day assignment deadline):
A user should be able to search for a book, tag it with their own unique metrics, and save it to their reading history.
Long-term goals:
In future, a user should be able to
 - compare the books they've read for different time periods
 - change the metrics that they choose to track
 - see a list of the books that they've read byt timeframe or by re-read counts
 - create an account as a new user
 - log out/log in to different accounts


 -- Tech Stack --
The application itself is built with Android in Java, from Android 7.0 Nougat. It connects with a MongoDB v4.1.10 database and is supported by several additional libraries including:
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
Some challenges I encountered at this

 -- How to run the application --
Currently, the application is set up to communicate with the MongoDB on the localhost of a computer running an Android emulator. You will therefore need to load and open the database on your own machine first.
If the database is launched on another server, or if the application is loaded onto a different device, then the connection information will need to be changed in the code.

 -- How to test the application --
At this stage, testing is been done entirely on an Android Emulator. Though JUnit tests would be helpful, I decided this was not my priority given the tight timeline.

 -- Software tools used --

 -- Tangible results --

 -- Screenshots or animated GIFs showing user flows (if customer-facing) --

 -- Links to web pages or installers for desktop applications --

 -- Links to code --
https://github.com/peggysaurus/ReadWide
