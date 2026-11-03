# Report-to-Code Implementation Map

| Report requirement | Implementation |
|---|---|
| Person/Student/Faculty/Admin hierarchy | `Person.java`, `Student.java`, `Faculty.java`, `Admin.java` |
| Multilevel inheritance | `UndergraduateStudent.java` |
| Encapsulation | Private fields + getters/setters throughout model classes |
| Abstraction | `Report.java` abstract class |
| Polymorphism / overriding | `StudentReport.generateReport()`, role methods |
| Method overloading | `Student.calculateGPA()` and `calculateGPA(boolean)` |
| Grade/GPA logic | `Marks.java`, `Student.java` |
| Exception/input validation | `Main.java`, `Marks.java`, `StorageManager.java` |
| File persistence | `StorageManager.java`, `data/students.txt` |
| Collections | `ArrayList<Student>`, `List<Marks>` |
| Lambda/Comparator analytics | `PerformanceAnalytics.java` |
| Topper/filter views | Main menu options 5 and 6 |
| Role-specific reports | Student, Faculty and Admin report functions |
| 30–50 demonstration records | 35 records included in `data/students.txt` |
| Future database/authentication/charts/web | Intentionally left as future scope from final report |
