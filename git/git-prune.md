git prune
==

`prune` deletes the refs to the branch that doesn't exist on remote branch,

```
# While fetching
$ git fetch --prune <name>

# Only prune, don't fetch
$ git remote prune <name>
```

### Example

`feature-1` branch has been already deleted.

```
$ git fetch --prune origin
From https://github.com/wkodate/til
 - [deleted]         (none)     -> origin/feature-1
remote: Enumerating objects: 9, done.
remote: Counting objects: 100% (9/9), done.
remote: Compressing objects: 100% (4/4), done.
remote: Total 5 (delta 1), reused 0 (delta 0), pack-reused 0
Unpacking objects: 100% (5/5), done.
   8e96d71..71bb127  master     -> origin/master
```

It was confirmed that the ref to the `feature-1` branch was deleted.
