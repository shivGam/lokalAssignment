# Android Intern Assignment - Job Listing App

## Overview

https://github.com/user-attachments/assets/102b8148-5dee-4d2d-aae8-f0ba315440ad

---

## Features

1. **Bottom Navigation**:  
   - **Jobs**: Displays job listings fetched from the API.  
   - **Bookmarks**: Shows jobs that have been bookmarked.

2. **Job Listings with Pagination**:  
   - The app fetches job data in a paginated way from the provided API.  

3. **Job Details Screen**:  
   - Clicking on a job card opens a detailed view with more information about the selected job.

4. **Offline Storage**:  
   - Bookmarking jobs allows users to access them even without an internet connection.  
   - Bookmarked jobs are saved in a local database.

5. **State Handling**:  
   - The app displays appropriate states throughout its flow such as loading, error, or empty states, providing a smooth user experience.

6. **Industry Practices & MVVM Architecture**:  
   - The app follows industry best practices like Utils,Constants.
   - It uses the **MVVM architecture**, which separates the business logic, multiple repositories have been used to ensure clean separation of concerns.

## Libraries and Tools Used

- **Retrofit**: For network requests to fetch job data from the API.
- **Room**: To store bookmarked jobs locally for offline access.
- **LiveData and ViewModel**: To handle UI-related data in a lifecycle-conscious way.
- **Material Design Components**: To create a modern, user-friendly UI and used XML.
- **No library for pagination.**
