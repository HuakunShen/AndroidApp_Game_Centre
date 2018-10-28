git log -1: get last commit
git diff: compare current repo with last committed repo
### if not added:

```git
git checkout -- fileName: revert all changes in fileName before adding and committing
git checkout --: revert all changes before adding and committing
```

### if added(staged):
```git
git reset: unstage all staged files, revert add .
```

### If  Committed

``` git
git log: show all commit history, remember the first few character of the commit id
git show <first few chars of the commit id>: show changes on this commit
git revert <commit id>: revert to specified previous version
git reset --hard <commit id>: reset everything to this commit, all commits before this are 
		still there, but all commits after it are removed permanently.
```

   

Revert changes on online repo: assume there is no conflict in files, remove a file locally,
then push, the removed file will also be removed on github (at least works for github)

```git
git push -f: force push, overwrite anything on github
```



### Force pull

<ol>
    <li>git fetch: download all changes on github that we don't have locally</li>
    <li>git reset --hard origin/master: for pull, reset to origin/master, which is the github version
eset --hard origin/<branch name>: other option for force-pull other branches</li>
</ol>