# PMI_projectwork
Scoreboard Application:

At the start of the application, if the file "project.xml" exists within the file folder,
it will load its contents into the program. 
If not, the program will start with an empty scoreboard.

At the menu of the program, the user will be asked to select an action. 
These actions include: 
1.) Register: adds a student to the scoreboard given their name and score.
2.) List: lists out all students on the scoreboard with their scores.
3.) Modify: given a student's name, allows the user to change the score of said student.
4.) Delete: given a student's name, deletes said student.
5.) Exit: closes the program.

Upon closing the application, all students on the scoreboard are saved to "project.xml".

XML Formatting:
1.) File starts with a "board" tag which will contain all students.
2.) Each student object is a "student" tag containing a "name" tag and a "score" tag.
3.) The "name" tag and "score" tag contain the name and score of the student respectively.
