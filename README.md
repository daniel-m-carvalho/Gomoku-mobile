# Gomoku Royale

## Specification and work requirements

The evaluation of the practical component of the course is based on the Gomoku Royale application, to be developed throughout the semester. The main functionality of the application is to support the playing of Gomoku games between users who have the application installed on their device. The game description can be found [here](link). The choice of rule variant to use is left to the discretion of the authors.

This document contains the specification of the requirements for Option C of the practical work. In this option, games are played using the devices of the involved players, and it is assumed that there is an HTTP API developed by the students within the scope of the DAW discipline.

Each game is preceded by a matchmaking phase, where two players who wish to play are associated. The availability to play is expressed by entering the lobby. The matchmaking attempt is performed automatically as soon as there are two players in the lobby. The game starts when the matchmaking succeeds. If matchmaking fails, the player whose device is responsible for the failure is excluded from the lobby, while the other player re-enters, becoming available to play again. If the excluded player wishes to try matchmaking again, they must re-enter the lobby.

Each player controls a set of pieces: black or white. Players alternate placing pieces on the board. The player controlling the black pieces starts. The first player to create an uninterrupted horizontal, vertical, or diagonal line of 5 of their own pieces wins. When the game ends, players are assigned scores according to the outcome. This score contributes to the player's ranking in the global leaderboard. Details regarding the scoring system are left to the discretion of the authors.

The Gomoku Royale application contains, at a minimum, the following screens:

- Screen for collecting user credentials;
- Screen for playing the game;
- Screen for displaying the global leaderboard;
- Screen for displaying information about the application authors.

The screen for collecting user credentials is used to gather the information necessary for non-anonymous interactions with the HTTP API. If the API requires prior registration, this registration can also be done within the scope of this screen.

The screen for playing the game is used to make moves, observe the current state of the board, and, when the game ends, announce the winner.

The screen for displaying the global leaderboard contains the list of players and their accumulated scores. Each item in the list contains the necessary elements to identify the player, their position in the leaderboard, and their accumulated score. This screen allows searching for players by their name and includes a shortcut for immediate navigation to the current player's position in the leaderboard.

The screen for displaying information about the authors contains the identification of all members of the group. The identification of each member consists of their student number and first and last names. The screen also contains a button to send an email to the group members, for example, to congratulate them on their excellent work.

### Demo

This short video demonstrates the application in action:

https://youtu.be/7L5N0qKDR0Y

### Authors

- Daniel Carvalho
- Francisco Saraiva
- Gonçalo Frutuoso

---

Instituto Superior de Engenharia de Lisboa<br>
BSc in Computer Science and Engineering<br>
Programming in Mobile Devices<br>
Winter Semester of 2023/2024
