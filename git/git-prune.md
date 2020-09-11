git prune
==

`prune` deletes the refs to the branch that doesn't exist on remote branch,

```
# While fetching
$ git fetch --prune <name>

# Only prune, don't fetch
$ git remote prune <name>
```
