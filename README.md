# Employee Leave Management System

A small full-stack training project using Java 21, Spring Boot, Spring Data JPA, Hibernate, MySQL, React and CSS.

## Structure
- backend: model, dao, service, controller, utility and main packages
- frontend: React with Vite
- database: two-table MySQL schema

## Setup
1. Run database/leave_management.sql using MySQL Workbench.
2. Configure your MySQL username and password in backend/src/main/resources/application.properties.
3. In backend run `mvn spring-boot:run`.
4. In frontend run `npm install` then `npm run dev`.
5. Open http://localhost:5173.

To set up an administrator, register a normal account and execute:
```sql
UPDATE employee SET role = 'ADMIN' WHERE email = 'your-admin@example.com';
```
Then sign in again. Never expose administrator registration publicly.

## Endpoints
- POST /api/auth/register
- POST /api/auth/login
- POST /api/leaves
- GET /api/leaves/my
- GET /api/admin/leaves
- PUT /api/admin/leaves/{id}/approve
- PUT /api/admin/leaves/{id}/reject

## Notes
Passwords are BCrypt hashed. Login tokens are random, server-side in-memory sessions and are cleared when the backend restarts. This is a local demo, not a production authentication implementation. The UI uses sessionStorage for the current login. Only two database tables are required.

The backend follows a simple layered structure based on the trainer's project organisation, adapted for Spring Data JPA as required by this case study.
