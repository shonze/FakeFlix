# FakeFlix

FakeFlix is a movie streaming platform with both an **Android app** and a **web server**. It provides a seamless experience for browsing, selecting, and watching movies.

---

## 📌 Project Overview

FakeFlix consists of two main components:

- **Android App** – A mobile application for browsing movies, managing user preferences, and streaming content.
- **Web Server** – A Node.js backend that handles user authentication, movie data, and interactions with a recommendation system implemented in C++.

The backend also serves the React-based web UI, allowing users to interact with FakeFlix from a browser.

---

## 🚀 How to Run the Project

### 1️⃣ **Run the Backend (Web Server)**
1. Make sure to put the right **Ports**, **MongoDB Connection String** and **JWT_secret_token**.
   Check out the **docker-compose.yml** file, this is the arguments you should change:

   ![image](https://github.com/user-attachments/assets/b06aa9fc-d33b-42bb-bed0-057b5c21d40b)

   And
   
   ![image](https://github.com/user-attachments/assets/017a2865-7764-40f1-b112-2ea0161bf354)

   
3. Navigate to the project root and run:
   ```bash
   docker-compose up --build
   ```
   This starts the **Node.js web server**, **MongoDB**, and **C++ recommendation system** inside Docker containers.

### 2️⃣ **Run the Android App**

1. Open **src/androidApp** directory in Android Studio.
3. Ensure an emulator or physical device is connected.
4. Build and run the app from Android Studio.

The app will interact with the backend to fetch movie data and recommendations.

---

## ⚙️ **Project Architecture**

### **Backend:**

- **Node.js & Express.js** – Handles API requests.
- **MongoDB** – Stores movie and user data.
- **C++ Recommendation System** – Provides movie recommendations based on user preferences.
- **React Frontend** – Web-based UI for browsing and streaming movies.

### **Android App:**

- **Java** – Main programming language.
- **Retrofit** – Handles API requests.
- **Room Database** – Manages offline data.
- **MVVM Architecture** – Ensures scalability and maintainability.

---

## 📌 **Work Process Summary**

1. **Planning & Design** – Defined requirements, UI/UX wireframes.
2. **Backend Development** – Built API, database, and authentication system.
3. **Android Development** – Created UI, integrated backend, implemented movie playback.
4. **Testing & Debugging** – Ensured smooth performance across devices.
5. **Deployment** – Prepared Dockerized containers for easy deployment.
