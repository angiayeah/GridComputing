# Grid Computing
Final Project of Distributed Computing System

## Features
- Connection between GS&RM, RM&Node, JobSender&RM
- RM is able to receive jobs from JobSender
- RM assign jobs to free nodes
- RM hand in jobs to GS when all the nodes under it are busy
- GS assign job to RM who are less busy
- RM has to accept job from GS
- When RM gives job to a Node, it starts a Thread