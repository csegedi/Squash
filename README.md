# Squash MVC Application and Rest API
 
## Short review
 
    This is a register application, wherewith a group of friends can record their squash contests.
The application can provide a user interface that shows all played contests, all locations of the contests, and all results. The data of the games are also in the database.
 
## The type of the users of the application and their permissions
 
In this application, there are two kinds of users:
* player
* administrator
 
* The administrator (editing permission):
    * The administrator can edit the data (insert new elements or update).
    * The administrator is registered from the beginning.
    * The administrator can add new players into the database: if he registers a new player, he gives a username and a password. The password is generated; if the player wants to log in the first time, he must change the password.
    * The administrator can add new locations to the database.
    * The administrator can register new contests.
   
* The player (reading permission):
    * If the player wants to enter the site the first time, he must change the earlier generated password. After the entry, the player can reach all information about all contests.
    * The player can use two different search boxes: search for the players, or look for the locations.
 
## The data of the location
 
* Name of the location where the game was played
* the address of the location
* Rent (for an hour): This amount of money is defined in HUF. The page is showing the rent also in EUR. The exchange is supported by another application (REST API) connected to another application (napiarfolyam.hu).
 
## The data of the contests
 
* players name (referenced to the identity numbers of the players via foreign key).
* result
* location (referenced to the identification number of the current location via foreign key).
* date
