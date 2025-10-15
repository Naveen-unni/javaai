
# Blood Tracking System

A simple Blood Tracking System built with **Java 17**, **Javalin**, **SQLite**, and **Tailwind CSS**.  
It allows viewing, adding, and searching blood donors via a web interface.

---

## Project Structure

blood-tracking-system/
│
├─ pom.xml # Maven build file
├─ blood.db # SQLite database (auto-created)
│
├─ src/main/java/com/example/bloodtracking/
│ ├─ Main.java # Entry point, starts Javalin server
│ ├─ model/Donor.java # Donor data model
│ └─ dao/
│ ├─ DatabaseConnection.java # DB initialization & connection
│ └─ DonorDAO.java # CRUD operations for donors
│
└─ src/main/resources/public/
└─ index.html # Frontend with Tailwind CSS and JS 


---

## How to Run Locally

### Prerequisites
- Java 17+  
```bash
java -version

Maven
mvn -version

Clone and Build
git clone https://github.com/Naveen-unni/blood-tracking-system.git
cd blood-tracking-system
mvn clean package

Run the Project
Windows
PowerShell
java -cp "target/classes;target/dependency/*" com.example.bloodtracking.Main
CMD
java -cp target\classes;target\dependency\* com.example.bloodtracking.Main


Linux / Mac
java -cp target/classes:target/dependency/* com.example.bloodtracking.Main

http://localhost:7000

## How the Code Works

### Backend

- **DatabaseConnection.java**:  
  Initializes SQLite DB and creates `donors` table. Inserts example donors on first run.

- **DonorDAO.java**:  
  Handles adding, fetching, and searching donors from the database.

- **Main.java**:  
  Starts Javalin server on port `7000` and sets up REST APIs:
  - `GET /donors` → Get all donors
  - `POST /donors` → Add new donor
  - `GET /donors/search?bloodGroup=A+&location=Kochi` → Search donors

---

### Frontend (`index.html`)

- Static HTML + Tailwind CSS.  
- Uses JS `fetch()` to call backend APIs.  
- Provides:
  - Table for all donors
  - Form to add a new donor
  - Search section to filter by blood group or location

---

### Database

- **SQLite** (`blood.db`) automatically created in project folder.  
- No manual DB setup needed.

---

### Features

- View all donors
- Add new donors
- Search donors by blood group or location
