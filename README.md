Squash MVC Application and Rest API
 
Short review
 
This is a register application, wherewith a group of friends can record their squash contests.
 
The application can provide a user interface, which shows all played contests, all locations of the contests, and all results.
The data of the contests are also in the database.
 
 
The type of the users of the application and their permissions
 
In this application there are two kinds of users:
•         player
•         administrator
 
•         The administrator (editing permission):
o   The administrator can edit the data (insert new elements or update).
o   The administrator is registered from the beginning.
o   The administrator can add new players into the database: if he registers a new player, he gives a username and a password. The password is generated, if the player wants to log in the first time, he has to change the password.
o   The administrator can add new locations into the database.
o   The administrator can register new contests.
•         The player (reading permission):
o   If the player wants to enter the site the first time, he must change the earlier generated password. After the entry, the player can reach all information about all contests.
o   The player can use two different search boxes: he can search for the players, or he can look for the locations.
 
The data of the location
 
•         name of the location
•         the address of the location
•         Rent (for an hour): This amount of money is defined in HUF. The page is showing the rent also in EUR.  The exchange is supported by another application (REST API), which is connected to another application (napiarfolyam.hu).
 
The data of the contests
 
•         players name (referenced to the identity numbers of the players via foreign key).
•         result
•         location (referenced to the identification number of the current location via foreign key).
•         date
