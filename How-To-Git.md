What git does:
==============

* Stores your code online
* Keeps track of changes
* Lets multiple people work on the same code

How do I get the repository?
----------------------------

```
git clone https://github.com/Kenkron/MobileComputingLab4.git
```

I changed the code.  How do I share it?
---------------------------------------

Add all your files to git's "staging area" (the list of files you're about to record the changes to)

```
git add newfile
git add changed_file
```

Then "commit" (tell git to record the changes)

```
git commit -m "A comment describing what you did"
```

Then "push" (sent the changes to the internet so other people can see them)

```
git push
```

**TIP:**

If you just want to add everything you changed, do this:

```
#you still have to add new files
git add newfile
#the -a will add all changed files, and commit
git commit -a -m "A comment describing what you did"
```

Someone else changed the code.  How do I see it?
------------------------------------------------

```
git pull
```

Somethimes, if you and the other person both changed the same file, you'll get a merge confict. Don't panic, this is normal. Git will give you an error message and list the files with problems. Just open the file, and look for something like this:


```
void main() {
<<<<<<< HEAD
	printf("I like banannas");
=======
	printf("I hate banannas");
>>>>>>> branch-a
}
```

Figure out which of those two things is correct, fix it, then commit the changes.  Then complain to the other person that changed your file.