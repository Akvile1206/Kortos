# Kortos 
#### *[kortos] f pl* - (playing) card

This is an extension of a university project that we did for Further Java course (https://www.cl.cam.ac.uk/teaching/1920/FJava/). And it is also an experiment in server/client system design. The basic function of the application is to support as much of real card deck operations as possible - from shuffling to checking the bargain - to make it posible to play classic games like King and 1000 over distance. Tthe server side pushes one to have as little different types of messages as possible and make them able to represent a great range of real world operations meanwhile the client side would love to have commands specific to the game at hand - easy to remember and manage. In the current iteration the server side is winning. 

## Commands

### \help
Prints out to the console the possible operations and their semantics.
### \nick [username : string]
Sets the current client's nickname to whatever sequence of characters which follows this command.
### \shuffle [Strating card : int (2-9)] [Number of cards per player : int]
Will shuffle the cards and assign them to players. The players do not get to know their cards until an explicit request is heard.
### \cards
Will check the cards that have been dealt to the player or that the player currently has in hand.
### Deck codes
These codes are used to represent locations in proceeding commands : 0 - table, 1 - current player's taken pile, 2 - bargain/the rest of the deck
### \place [card code : string] [face up? : 0 or 1] [destination code : deck code]
This command places the specified card in the right destination. The card code is made up of card number/letter and the first letter of the suit (e.g. 9S, JH).  Face up determines whether the card code you placed is known to all players.
### \check [is this check public? : 0 or 1] [deck code]
Will check and list cards in a particular deck. 
### \take [source code : deck code] [number of cards : int]
Will take the specified number of cards from the indicated deck. The cards are placed and taken in a first in first out order.

              
