# Loan Management System

## Setup Instructions
1. Clone the repo:  
   ```bash
   git clone <repo-url>
   ```
2. Navigate to the project folder:  
   ```bash
   cd loan-management-system
   ```
3. Configure database in `application.properties`.
4. Build and run:  
   ```bash
   ./mvnw spring-boot:run
   ```
5. Access app at: `http://localhost:8080/login`

## Tech Stack
- **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA  
- **Database:** MySQL 8  
- **Frontend:** HTML, CSS, Vanilla JS  
- **Build Tool:** Maven

## Assumptions
- Users have unique usernames.  
- RO and Approver roles are predefined.  
- Session-based login is used; no JWT.  
- Frontend is basic and served from Spring Boot static resources.

## How to Use
- **RO Dashboard:**  
  - Punch new applications.  
  - View applications assigned to RO.
- **Approver Dashboard:**  
  - View unclaimed applications.  
  - Claim applications.  
  - Approve/reject workflow steps; after final approval, application moves to Approved tab; if rejected, moves to Rejected tab.

