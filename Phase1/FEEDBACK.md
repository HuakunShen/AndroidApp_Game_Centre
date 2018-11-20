# Commit Messages
You have some good commit messages but you also have some atrocious ones. For example, `Ok`, `undo doing` and `still working` are not very useful commit messages. You will need to improve your commit messages for phase 2.

# Participation
We noticed that "Shen Huakun" had very few significant commits. This is going to be a larger issue in Phase 2, and might dramatically affect the mark. Please make sure that everyone on the team is contributing.

# Documentation
Your README is generally good, though you could add a little more detail on how to set up and run your project. For example, giving the git command to clone would be useful. Also specifying which options to click would be useful as well. 

Good job with the TEAM.md file.

# Features
## Required
- Sign up and sign in work well
- Good job with the game launch centre
- Undo works up to the minimum 3 steps.
- Autosave works nicely.
- 4x4 and 5x5 work nicely
- Can't find the scoreboard.

## Bonus
- Arbitrary undos, nice! +1%
- Image import works but is buggy +1%

## Bugs
- Sometimes, when I press new game the app crashes. This usually happens after I press load saved game and then the back button.

- After importing image, if I change the difficulty, it crashes.
	-It crashed when I went on the internet, downloaded an image, then went back to the app, imported it, and tried to start a game. 

# Design and Code Quality
The types of variables should be as high in the inheritance hierarchy as possible, so `private ArrayList<Button> tileButtons;` type should be List, not ArrayList. This applies to every member variable you declare in your project. That allows you to change the object type without having to change any other code.

## JavaDocs
You did a decent job with the javadocs, but I do see some unused variables like `admin` in `LoginInfo`. Either remove these or document why they are there. Also it seems some classes like `ScoreBoardActivity` don't have javadoc. Make sure these are documented for phase 2. Note that even for classes that you imported(like `GestureDetectGridView`) you still have to take ownership over it and document everything and make sure there are no code smells in these classes. Also make sure to document your interfaces like `AutoSave` to explain what it's supposed to be used for. 

## ScoreDatabase
Your `ScoreDatabase` class seems to use an arraylist of arraylists for storing users and their scores. This seems awfully complicated and unnecessary. Why not just create a User class with the score(and other properties) as member variables? Also, finding a score for a user would scale linearly with the number of users times the number of scores, which is not optimal since getting a score is probably something users do often. Think about how you can optimize this process to be more efficient, and easier to read and maintain (Hint, O(1) time should be possible to get a user's score). Also, you shouldn't be using `Object` class which is too vague to understand. Define your own class. 

Also, per-game scoreboard is a requirement, and I don't see any code that implements that. Think about how you would store the scores for a game for phase 2.


# Grade
4.2/10
Your scoreboard is nonexistent so it's an automatic 4/10.
The extra 0.2 is from the bonus.


