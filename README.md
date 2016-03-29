# Grid Computing
Final Project of DCS course

## Basic Features
- Connection between GS&RM, RM&Node, JobSender&RM


## GS communication
- GS assign job to RM who are less busy (RM has to accept, unless its offline)
- Each GS has checker for other GSs, regularly check if they are still online
- If one GS is crashed, anounce all the GS, transfer his RM to others
- GS has checker for each RM under it, regularly check if they are still online
- Once RM is crashed, GS transfer its joblist to another RM under it (hasn't thought about what all the RM under GS are crashed)

## RM communication
- RM is able to receive jobs from JobSender
- RM assign jobs to free nodes (check free nodes first)
- When RM gives job to a Node, it starts a Thread
- RM hand in jobs to GS when all the nodes under it are busy

## Nodes communication
- When the RM that the nodes belong to is not online, ignore the creation procedure
- When a job is done and the RM is not online (thus cannot receive complete message), ignore

## JS communication
- When JS try to give jobs to a RM and it is not online, automatically search for other RMs to give jobs

## TODO
- Run on DAS or other server

## Useful Things
- Concept of **Technichal Debt**, we spent long time to fix bugs, writing catch blocks
- Possible flaw in the system
