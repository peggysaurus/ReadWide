# ReadWide
ReadWide helps people track the range and variety of the books they read based on the metrics they choose.

 -- Objective --
Core functions:
A user should be able to search for a book, tag it with the metrics they have chosen, and save it to their reading history.
Long-term goals:
A user should be able to compare the books they've read for different time periods, change the metrics that they choose to track


 -- Tech Stack --
The application itself is built with Android in Java, with a MongoDB database. This is supported by several additional libraries including.
retrofit, rxjava, Gson, Jackson, Picasso
- To find books, the application accesses the openlibrary.org RESTful API using retrofit
- Information is transferred in json format and converted using both Gson and Jackson libraries depending on the object required. Specifically, Jackson is used to convert json to HashMaps and back, while Gson is used for everything else.
- Cover images are loaded from URL using Picasso
- Some threading is managed with rxjava


