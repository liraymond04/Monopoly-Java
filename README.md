# My Personal Project: Monopoly

## About

This is a **Java** clone of *Monopoly* created for a CPSC 210 project based
on the [official rule-set](https://www.officialgamerules.org/monopoly).
Monopoly is a turn-based multiplayer board game, where the objective
of the game is to not go bankrupt and outlive the other players by
acquiring properties and collecting rent.

## Who is this for?

*Monopoly* is a fun game that can be played with friends and family, its
combination of strategy and luck makes it simultaneously engaging and
accessible to a wide variety of people. This project aims to bring the
Monopoly experience to players as a cross-platform desktop application.

## Why Monopoly?

As a player of the official PC port of *MONOPOLY® PLUS* from Ubisoft,
I was frustrated with many of the user interface choices, such as:
- Only being able to change transaction values by 1, making inputting
large values difficult
- Unclear controls for how to select properties to exchange during trades

Many of these issues with the user interface are due to the game being designed
for gamepad controllers. This usually makes the game unintuitive and unplayable
for desktop users, most of whom use mouse and keyboard to play.

As a project for CPSC 210, I think Monopoly is a decently complex game that
will be interesting to design and implement, and is achievable within my
limits as a programmer.

## User Stories

- As a user, I want to be able to choose how many players to start a game with ✓
- As a user, I want to be able to roll dice to choose how many squares to move ✓
- As a user, I want to be able to buy or auction un-owned properties that I land on
- As a user, I want to be able to pay rent to players whose un-mortgaged properties I land on
- As a user, I want to be able to draw a card when I land on CHANCE or COMMUNITY CHEST
- As a user, I want to be able to collect money when I pass GO ✓
- As a user, I want to be able to sell properties to other players
- As a user, I want to be able to buy and sell houses and hotels on properties whose colour-set I own
- As a user, I want to be able to mortgage and lift mortgages on properties I own
- As a user, I want to be able to end my turn and pass it to another player ✓
- As a user, I want to be able to save my game from the pause menu ✓
- As a user, I want to be able to be warned to quit without saving, when quitting from the pause menu ✓
- As a user, I want to be able to choose a game to load from the main menu ✓

## Instructions for Grader

- You can generate the first required action related to adding Xs to a Y by adding players to a game in the main menu screen
- You can generate the second required action related to adding Xs to a Y by pressing M to add balance to player
- You can generate a third action related to adding Xs to a Y by rolling a dice to change the player's position on a board
- You can generate a fourth action related to adding Xs to a Y by adding 200 to a player's balance if they pass GO
- You can locate my visual component by opening or creating a new game, and viewing the game board
- You can save the state of my application by opening the pause menu and selecting the option to save the game
- You can load the state of my application by choosing a save to load from the main menu screen

### Main menu
- You can press LEFT and RIGHT arrows to change the number of players to create a game with
- You can press DOWN and UP arrows to change menu option
- You can press ENTER is used to select menu option
- You can press ESCAPE to exit pop-up menu

### Inside game
- You can press ESCAPE to open pause menu
- You can press DOWN and UP arrows to change menu option
- You can press ENTER to select menu option

### Debug functions in game
- You can press F to print name of current player
- You can press G to change turn to next player
- You can press H to print balance of player
- You can press J to move player 39 squares
- You can press M to add 10 dollars to the current player's balance
 

