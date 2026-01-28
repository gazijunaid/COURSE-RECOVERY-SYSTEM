# Course Recovery System (CRS)

## Overview
A Java GUI application designed to help educational institutions manage student course recovery. The system allows Academic Officers and Course Administrators to plan, track, and evaluate course recovery plans for students who have failed courses.

## Target Users
1. **Academic Officer** - Full system access including user management
2. **Course Administrator** - Course and student management, recovery plans

## Default Login Credentials
- **Academic Officer**: `admin` / `admin123`
- **Course Administrator**: `courseadmin` / `course123`

## Key Features
1. **User Management** - Add/Update/Deactivate user accounts, password reset, login/logout logging
2. **Course Recovery Plans** - Create recovery plans with milestones and track progress
3. **Eligibility Check** - CGPA calculation and eligibility verification (CGPA >= 2.0, <= 3 failed courses)
4. **Academic Reports** - Generate and export reports to PDF
5. **Email Notifications** - Automated notifications for account management and recovery plans

## Project Structure
```
/src/crs/
  /model/         - Data model classes (User, Student, Course, RecoveryPlan, etc.)
  /service/       - Business logic services
  /util/          - Utility classes (FileManager, Validation, Password)
  /gui/           - Swing GUI components
  Main.java       - Application entry point

/lib/             - External libraries (iText PDF, JavaMail)
/data/            - Data storage files (binary and text)
/nbproject/       - NetBeans project configuration
```

## OOP Concepts Implemented
- **Inheritance**: User (abstract) -> AcademicOfficer, CourseAdministrator
- **Encapsulation**: Private fields with public getters/setters
- **Polymorphism**: Method overriding (getRole(), hasPermission())
- **Abstraction**: Abstract User class with abstract methods
- **Packages**: Organized into model, service, util, gui packages

## How to Run
1. In Replit: Click "Run" to start the VNC desktop and the application
2. In NetBeans: Open project and run Main.java
3. Command line: `./run.sh` or `java -cp "lib/*:build/classes" crs.Main`

## Data Storage
- User accounts: `data/users.dat` (binary)
- Login logs: `data/login_logs.dat` (binary)
- Students: `data/students.dat` (binary)
- Courses: `data/courses.dat` (binary)
- Recovery plans: `data/recovery_plans.dat` (binary)
- Email logs: `data/email_log.txt` (text)

## Importing Sample Data
1. Go to Students panel -> Click "Import CSV"
2. Select `data/students.csv` file
3. Go to Courses panel -> Click "Import CSV"  
4. Select `data/courses.csv` file

## Third-Party Libraries
- **iText PDF 5.5.13.3** - PDF report generation
- **JavaMail 1.6.2** - Email notification service

## Recent Changes
- Initial project creation with full feature implementation
- Created model classes with OOP hierarchy
- Implemented all service layers
- Built modern Swing GUI with professional styling
- Added PDF export and email notification capabilities
