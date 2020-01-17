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

 -- A Note on the Code --
 This project began as part of an assignment for a Master of Software development, with the very short timeframe of 3 and a half days to go from nothing to a demonstable product. The goal of the assignment was to learn new things, and I had little to no familiarity with most of the tools used at the start. A great deal of trial and error was involved, as well as changing how I approached things half way through and the code is therefore not the tidiest or most consistent.
 I hope to return to this project to both progress with the long term goals, and tidy up the current state of the code. However, with other assignments coming up quickly I'm not sure when I'll be able to really focus on it again.
