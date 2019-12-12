Given a Player class - an instance of which can communicate with other Players.

###The requirements are as follows:

1. create 2 Player instances

1. one of the players should send a message to second player (let's call this player "initiator")

1. when a player receives a message, it should reply with a message that contains the received message concatenated with the value of a counter holding the number of messages this player already sent.

1. finalize the program (gracefully) after the initiator sent 10 messages and received back 10 messages (stop condition)

1. both players should run in the same java process (strong requirement)

1. document for every class the responsibilities it has.

Please use core Java as much as possible without additional frameworks like Spring etc; focus on design and not on the technology.

Please include a maven project with the source code to build the jar and a shell script to start the program.

Everything not specified is to be decided by you; everything specified is a hard requirement.

You should send your source code as an archive attached to the e-mail; inline links for downloading will be ignored.

### Getting Start

1. Compile the Application.

    `mvn clean install -DskipTests`
    
1. Start the Application.
    
   ```
   java -jar target/players-app-1.0-SNAPSHOT-jar-with-dependencies.jar {chat name} [{member nic1}],[{member nic2}] {initiator nic} {inviting message}   
   ``` 
    
1. Result end stop
    ```
    bash-3.2$ ./start.sh
    initializing..
    Player {player1} has been joined to chat {game}.
    Player {player2} has been joined to chat {game}.
    chat {game} started..
    thread_id: 1 player: player2 send message: Hey
    thread_id: 11 player: player1 received message: Hey,1
    thread_id: 11 player: player1 send message: Hey,1
    thread_id: 12 player: player2 received message: Hey,1,1
    thread_id: 12 player: player2 send message: Hey,1,1
    thread_id: 13 player: player1 received message: Hey,1,1,2
    thread_id: 13 player: player1 send message: Hey,1,1,2
    thread_id: 14 player: player2 received message: Hey,1,1,2,2
    thread_id: 14 player: player2 send message: Hey,1,1,2,2
    thread_id: 14 player: player1 received message: Hey,1,1,2,2,3
    thread_id: 14 player: player1 send message: Hey,1,1,2,2,3
    thread_id: 14 player: player2 received message: Hey,1,1,2,2,3,3
    thread_id: 14 player: player2 send message: Hey,1,1,2,2,3,3
    thread_id: 14 player: player1 received message: Hey,1,1,2,2,3,3,4
    thread_id: 14 player: player1 send message: Hey,1,1,2,2,3,3,4
    thread_id: 14 player: player2 received message: Hey,1,1,2,2,3,3,4,4
    thread_id: 14 player: player2 send message: Hey,1,1,2,2,3,3,4,4
    thread_id: 14 player: player1 received message: Hey,1,1,2,2,3,3,4,4,5
    thread_id: 14 player: player1 send message: Hey,1,1,2,2,3,3,4,4,5
    thread_id: 14 player: player2 received message: Hey,1,1,2,2,3,3,4,4,5,5
    thread_id: 14 player: player2 send message: Hey,1,1,2,2,3,3,4,4,5,5
    thread_id: 14 player: player1 received message: Hey,1,1,2,2,3,3,4,4,5,5,6
    thread_id: 14 player: player1 send message: Hey,1,1,2,2,3,3,4,4,5,5,6
    thread_id: 14 player: player2 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6
    thread_id: 14 player: player2 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6
    thread_id: 13 player: player1 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7
    thread_id: 13 player: player1 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7
    thread_id: 13 player: player2 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7
    thread_id: 13 player: player2 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7
    thread_id: 13 player: player1 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8
    thread_id: 13 player: player1 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8
    thread_id: 13 player: player2 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8
    thread_id: 13 player: player2 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8
    thread_id: 13 player: player1 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9
    thread_id: 13 player: player1 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9
    thread_id: 13 player: player2 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9
    thread_id: 13 player: player2 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9
    thread_id: 13 player: player1 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10
    thread_id: 13 player: player1 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10
    thread_id: 13 player: player2 received message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10
    thread_id: 13 player: player2 send message: Hey,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10
   ```