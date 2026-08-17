# School Management System

A Spring Boot–based School Management System for handling admissions, attendance,
exams, fees, staff payroll, timetables, certificates, and day-to-day school
administration — with role-based access for Admins, Staff, and Teachers.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language / Runtime | Java, Spring Boot 4.1 |
| Web | Spring MVC (`spring-boot-starter-webmvc`), Thymeleaf |
| Data | Spring Data JPA / Hibernate, MySQL (primary), SQLite (optional/dev) |
| Security | Spring Security (session-based login, role-based access) |
| Validation | Jakarta Bean Validation |
| Build | Maven |
| Frontend | Bootstrap 5, vanilla JS, custom CSS (light/dark theme) |
| Utilities | Lombok |

---

## Features

### Authentication & Access Control
- Session-based login/logout
- Three roles: **ADMIN**, **STAFF**, **TEACHER** — permissions enforced at the
  route level for every module
- User management (create/edit users, link a login to a Teacher profile)

### Dashboard
- Key stats at a glance (students, teachers, fees, attendance)
- App info / about page

### Student Management
- Admission with photo, Aadhaar, PEN, APAAR ID, blood group, category,
  religion, admission type, status, guardian details
- List, profile view, printable profile
- ID card generation (front + back, pulls the school's own logo/name/address
  automatically — see [`student/id-card.html`](src/main/resources/templates/student/id-card.html))
- Student Session — class/section history per academic session (promotion
  trail)

### Teacher Management
- Teacher CRUD (photo, employee code, TEN no., subject, contact details)
- Teacher Session — class/section assignment per academic session, with a
  reassignment review flow

### Academic Structure
- Class Rooms, Sections, Subjects
- Academic Sessions, including a session-close workflow that promotes
  students to the next session

### Attendance
- Daily attendance marking per class/section
- Attendance list and history

### Exams & Results
- Exam creation with subject-wise marks
- Result entry and subject-wise result summaries

### Fees
- Fee Heads (categories) and class-wise Fee Structures
- Fee collection with receipt numbers and payment status
- Fee reports

### Salary (Staff Payroll)
- Per-teacher salary ledger
- Salary payment history (add / edit / view)

### Expenses
- School expense tracking

### Timetable
- Weekly, day-wise timetable per class/section

### Notices
- Notice board with audience targeting (all / staff / teachers / students)

### Certificates
- Bonafide Certificate
- Character Certificate
- Transfer Certificate (generate, print, and keep a record of each one issued)

### Reports
- Students, Attendance, Fees, and Exams reports

### Activity Log
- A permanent, append-only audit trail: every create/update/delete/status
  change across every module, plus login/login-failed/logout events,
  records who did it and when
- Admin-only, with filters by user, module, action, and date range
- Entries can never be edited or removed from within the app

### School Settings
- School profile: name, logo, address, phone, website, principal name and
  signature, affiliation number — used everywhere the school's identity is
  printed (ID cards, certificates, letterheads)
- Database backup download

### Global Search
- Search across students, teachers, classes, sections, fees, and notices from
  anywhere in the app
- Results are grouped by category with a count badge, and student/teacher
  results show their photo (falls back to an icon if no photo is on file)
- Respects role: a Teacher only sees students/classes they're actually
  assigned to

### UI
- Light/dark theme toggle
- Responsive layout — usable on desktop, tablet, and mobile
- File uploads (photos, logo, signature) restricted to image types with
  server-side validation

---

## Data Handling: Soft Delete Only

**Nothing is ever hard-deleted from the database.** Every entity
(students, teachers, sessions, attendance, exams, fees, salary, timetable,
notices, users, etc.) extends a shared `BaseEntity` that carries a `deleted`
flag and a `deletedAt` timestamp.

When code calls the normal `repository.delete(...)` /
`repository.deleteById(...)` (used throughout the app exactly as before),
Hibernate intercepts it via `@SQLDelete` and runs an `UPDATE ... SET
deleted = true, deleted_at = NOW()` instead of a real `DELETE`. A matching
`@SQLRestriction("deleted = false")` on each entity automatically hides
soft-deleted rows from every normal query, so no existing screen shows
"deleted" records — but the rows, and the session-wise history they carry
(which class a student was in each year, past salary payments, old
attendance, etc.), are preserved permanently at the database level.

**Known trade-off:** fields with a uniqueness constraint (admission number,
Aadhaar number, employee code, username, receipt number, etc.) still count a
soft-deleted row as occupying that value, since the row physically remains
in the table. This is intentional — it prevents accidentally reusing an
identifier that once belonged to a real record — but it's worth knowing if a
"deleted" record's unique value ever needs to be reused.

---

## Getting Started

### Prerequisites
- Java (matching the version configured in `pom.xml`)
- Maven (or use the bundled `./mvnw` / `mvnw.cmd`)
- MySQL server (or use the SQLite option for local/dev use)

### Configuration
Database credentials are read from environment variables (see
`.env.example`) — copy it to `.env` or export the variables directly before
running:

```
DB_URL=jdbc:mysql://localhost:3306/school_db
DB_USERNAME=root
DB_PASSWORD=your-password-here
```

### Run

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080` by default (see
`server.port` in `application.properties`).

### Default login
On first run, `DataSeeder` creates a default admin account if none exists
(check the console output / `DataSeeder` for the credentials). **Change this
password immediately after first login** if deploying anywhere beyond local
development.

---

## Project Structure

Each business area lives in its own package under
`src/main/java/com/naim/school/`, following a consistent
`Controller` → `Service` → `Repository` → `Entity` layering:

```
student/          admissions, profiles, ID cards
teacher/          staff profiles
studentsession/   per-session class/section history
teachersession/   per-session class/section assignment
academicsession/  academic year management
attendance/       daily attendance
exam/ result/     exams and results
fee/ feehead/ feestructure/   fee collection & structure
salary/           staff payroll
expense/          school expenses
timetable/        weekly schedule
notice/           notice board
certificate/      bonafide / character / transfer certificates
report/           cross-module reports
schools/          school profile & branding
search/           global search
security/         auth, users, roles
sms/              shared infrastructure (file storage, exceptions, base entity, etc.)
```

---

## Notes for Contributors
- All entity deletes are soft deletes by design — don't add raw SQL
  `DELETE` statements or bypass the repository layer for removing records.
- Business-facing validation errors should be thrown as `BusinessException`
  (see `sms/BusinessException.java`), not a raw `RuntimeException` — the
  global exception handler shows `BusinessException` messages to the user
  as-is, but logs and hides the details of anything else.
- File uploads go through `FileStorageService`, which enforces an image
  type/extension whitelist — don't bypass it for new upload features.
