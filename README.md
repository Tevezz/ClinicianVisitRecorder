# Clinician Visit Recorder

A high-performance Android MVP built to explore real-time audio capture, background reliability, and offline-first architecture for home healthcare clinicians. 

### 🚀 The Mission
This project was developed as a technical study over two nights to demonstrate how to solve critical mobile infrastructure challenges:
* **Background Reliability:** Maintaining audio integrity during long-running sessions.
* **Large Dataset Performance:** Rendering infinite lists without UI jank.
* **Offline-First Persistence:** Using local storage as the Single Source of Truth.

---

### 🛠️ Tech Stack & Architecture
* **Language:** 100% Kotlin + Coroutines/Flow
* **UI:** Jetpack Compose (Modern Material 3 approach)
* **Architecture:** MVI (Model-View-Intent) + Clean Architecture
* **Navigation:** Navigation 3 (The latest Compose-native navigation API)
* **Networking:** Retrofit + Kotlinx Serialization
* **Paging:** Paging 3 (Used with the Rick and Morty API to simulate large patient rosters)
* **Persistence:** Room (Metadata & File path storage)
* **Dependency Injection:** Hilt
* **Image Loading:** Coil

---

### 🧠 Key Engineering Decisions

#### 1. Foreground Service for Audio Capture (The "Spotify" Question)
I implemented a `ForegroundService` with a `MICROPHONE` type. This ensures the OS grants high-priority resource access and prevents the process from being killed during "hours-long" patient visits—a critical requirement for clinical documentation that standard background tasks can't guarantee.

#### 2. Paging 3 + Retrofit (The "Instagram" Question)
To handle large grids and lists without crashes or jank, I used **Paging 3**. This provides built-in prefetching and state management, ensuring that even with a massive dataset, the memory footprint remains low and the UI stays responsive at all times.

#### 3. Room as SSOT (Single Source of Truth)
Even in a "Network-Optional" environment, a clinician can record a visit. The audio file is saved to internal storage, and the path is persisted in Room immediately. This ensures that data is never lost, even if the app is closed before a sync occurs.

---

### ⏳ Future Improvements & Roadmap
This MVP is a work-in-progress study. Future iterations would include:
* **Full MVI/Clean Arch Polish:** Further decoupling the data sources from the domain layer and refining state flow.
* **Live Transcription Strategy:**
    * **Hybrid Strategy:** Implement real-time STT via API when connected.
    * **Deferred Sync:** Using **WorkManager** to trigger full-file transcription once the device regains connectivity, ensuring a transcript is generated for every offline visit.
* **RemoteMediator:** Implementing a true network + database cache for the patient list to support 100% offline searches and data persistence.

---

### 🏗️ How to Run
1. Clone the repo.
2. Build and Run on an **Android API 31+** device.
