# Student Academic Performance Analytics System

A dependency-free Java Swing desktop application for tracking student marks, calculating GPA and analysing academic performance. Built for the OOTS PBL project.

> **Note on data:** All student records in `data/students.txt` are **fictional sample data** created for demonstration. Names, IDs and `@college.edu` email addresses do not belong to real people.

## Features
- Minimal Java Swing dashboard with total students, average GPA, top GPA, and students needing support.
- Search/filter the student table by ID, name, branch, semester, GPA, or performance; clear the search with one click.
- Charts tab with GPA distribution and average marks by subject, drawn with Java2D (no third-party libraries).
- Add students with Branch and Semester dropdowns.
- Enter/update marks by selecting a student and semester; the subject list changes for semesters 1-8.
- Student reports, top performers, performance filtering, and Admin Summary.
- Input validation for duplicate/blank IDs, names, email addresses, and marks outside 0-100 (including NaN/infinity).
- OOP design: encapsulation, inheritance, abstraction, method overloading and overriding.
- Local persistent storage in `data/students.txt`; sample data is included.

## Run the application

### Get the code
```sh
git clone https://github.com/Hrity7/student-performance-analytics.git
cd student-performance-analytics
```
Or use **Code > Download ZIP** on GitHub and extract it.

### Windows / VS Code
1. Install JDK 17 or later.
2. Open the project folder in VS Code.
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

## Grading
| Marks | Grade | Grade point |
|---|---|---|
| 90-100 | A+ | 10 |
| 80-89 | A | 9 |
| 70-79 | B | 8 |
| 60-69 | C | 7 |
| 50-59 | D | 6 |
| 40-49 | E | 5 |
| Below 40 | F | 0 |

GPA is the credit-weighted average of grade points.

## Local storage
Student records are persisted in `data/students.txt` using a structured delimiter-separated text format. `StorageManager.save()` writes the records and `StorageManager.load()` reconstructs them when the application starts. No database, server, Maven or external dependency is required.

The app saves changes back to this file, so if you add your own students while testing, avoid committing personal details.

## Project structure
```text
src/                  Java classes and Swing UI
data/                 Local student records (students.txt)
docs/                 Project/report implementation notes
run.bat               Windows launcher
run.sh                Linux/macOS launcher
sample_output.txt     Example of the analytics output
```

## 👥 Team Members

This project was developed as a collaborative effort by our team as part of our academic Project-Based Learning (PBL).

- **Hritwiz Verma** – [@Hrity7](https://github.com/Hrity7)
- **Faiz Hussain** – [@FaizHussain78](https://github.com/FaizHussain78)
- **Krish Dubey** – [@krishxdubey](https://github.com/krishxdubey)
- **Manish Chauhan** – [@manishxchauhan](https://github.com/manishxchauhan)
- **Ishaan Singh** - [@singhishaan505-ui](https://github.com/singhishaan505-ui)
  
All team members contributed to the planning, development, and implementation of this project.

## License
Released under the [MIT License](LICENSE).
