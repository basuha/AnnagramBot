# AnnagramBot
<https://t.me/AnnagramBot>
---
My Java pet project.  
This bot takes russian word from database and mixes the letters in it. User should guess the word. For every guessed words user obtains the points. The bot can be used both in group chats and in private ones.  
_Hosted on [Heroku](https://www.heroku.com), uses [Heroku Postgres](https://www.heroku.com/postgres) database.  
by Arkadiy Nadyrov 2020_
***
### Commands
Description of bot commands:
##### _/task_
Shows a task for a specific chat consisting of ten anagrams:  
![Screenshot](/screenshots/task_screenshot.png)  
The longer the word remains unsolved, the more complex it is considered, and the more points you can get for guessing it. Each guessed word makes an increment of the complexity of the words not guessed.  
***
Every chat has it own task, and it own score table:  
![Screenshot](/screenshots/taskbase_screenshot.png)  
***
##### _/rank_
Shows a score table for a specific chat:    
![Screenshot](/screenshots/rank_screenshot.png)  
***
For every thousand points, a star is added to the names of the players:  
![Screenshot](/screenshots/star_screenshot.png)  
***
##### _/overall_
Shows the overall score table:  
![Screenshot](/screenshots/overall_screenshot.png)  
***
