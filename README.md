# Student Academic Performance Analytics System

A dependency-free Java Swing application for the OOTS PBL project **Student Academic Performance Analytics System**.

## Features
- Minimal Java Swing dashboard with total students, average GPA, top GPA, and students needing support.
- Search/filter the student table by ID, name, branch, semester, GPA, or performance; clear the search with one click.
- Charts tab with GPA distribution and average marks by subject, drawn with Java2D (no third-party libraries).
- Add students with Branch and Semester dropdowns.
- Enter/update marks by selecting a student and semester; the subject list changes for semesters 1–8.
- Student reports, top performers, performance filtering, and Admin Summary.
- Input validation for duplicate/blank IDs, names, email addresses, and marks outside 0–100 (including NaN/infinity).
- OOP design: encapsulation, inheritance, abstraction, method overloading and overriding.
- Local persistent storage in `data/students.txt`; sample data is included.
- Faculty Report menu option is removed, as requested.

## Run the application

### Windows / VS Code
1. Install JDK 17 or later.
2. Extract the ZIP and open the project folder in VS Code.
3. Run `run.bat`, or open `src/Main.java` and use the Java Run action.

### Linux/macOS
From the project folder, run:

```sh
sh run.sh
```

### Manual compile/run
Run from the project folder so the relative `data/students.txt` path resolves correctly:

```sh
javac -d out src/*.java
java -cp out Main
```

## Search and charts
- Type into **Search records** to filter rows as you type. Search matches across all displayed table columns.
- Open **Charts & Analytics** to see the distribution of student GPA and the highest available subject averages (up to eight subjects).
- Charts refresh when records are refreshed after entering marks or adding a student.

## Semester-specific subjects
The Enter Marks dialog includes a semester selector. Each semester has a separate subject list. Semester 4 retains the original OOTS, DSA, DBMS, OS and DM subjects for compatibility with the included sample data. Course codes for other semesters are distinct so similarly named subjects in different semesters do not overwrite each other.

## Local storage
Student records are persisted in `data/students.txt` using a structured delimiter-separated text format. `StorageManager.save()` writes the records and `StorageManager.load()` reconstructs them when the application starts. No database, server, Maven or external dependency is required.

## Project structure
```text
src/     Java classes and Swing UI
 data/   Local student records (students.txt)
 docs/   Project/report implementation notes
run.bat  Windows launcher
run.sh   Linux/macOS launcher
```
