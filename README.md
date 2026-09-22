# 🍽️ SmartMealPlanner — Android App

**SmartMealPlanner** is a modern Android meal planning and recipe application built with **Kotlin, MVVM, Jetpack libraries, and REST APIs**.

The application allows users to discover and search recipes, save favorites, create personalized weekly meal plans, replace meals, and interact with an **AI-powered meal assistant** for recipe and meal-related questions.

This Android application is part of the SmartMealPlanner full-stack platform:

* 📱 **Android App** — Kotlin + MVVM
* 🌐 **Web Application** — React
* ⚙️ **Backend API** — Node.js + Express
* 🛠️ **Admin Panel** — React
* 🗄️ **Database** — MongoDB
* 🤖 **AI Assistant** — Backend AI integration

---

## ✨ Key Features

### 🤖 AI Meal Assistant

Interact with an AI-powered conversational assistant directly from the Android application.

Users can:

* Ask questions about recipes and ingredients
* Get meal ideas based on preferences
* Ask cooking-related questions
* Get food and meal-related guidance
* Have an interactive conversation through a modern chat interface

The Android application communicates with the backend AI service through a REST API.

### 🍳 Recipe Discovery

* Browse Recipes of the Week
* View personalized recommendations
* Explore recipes by category
* View recipe details
* Browse all available recipes

### 🔎 Recipe Search

* Search recipes by keyword
* Real-time search support
* IME keyboard search action
* Scrollable recipe results
* Search results retrieved from the backend API

### ❤️ Favorites

Users can save their favorite recipes and access them from a dedicated favorites section.

### 📅 Personalized Weekly Meal Planner

Users can generate personalized weekly meal plans based on:

* Diet type
* Daily calorie target
* Meals per day
* Cuisine preference
* Maximum cooking time

Example preferences:

```text
Diet: Vegetarian
Daily Calories: 2000
Meals Per Day: 3
Cuisine: Indian
Cooking Time: < 30 minutes
```

The application then displays a personalized weekly meal plan.

### 🔄 Meal Replacement

Users can replace meals in their generated plan when they want an alternative recipe.

### 👤 User Profile

* View user information
* Manage profile-related functionality
* Maintain authenticated sessions
* Securely log out

### 🔐 Authentication

The application uses token-based authentication.

Authentication tokens are stored using **Jetpack DataStore Preferences** and automatically attached to authenticated API requests using an **OkHttp Interceptor**.

---

# 📱 Application Screens

The application includes the following major screens:

* Login
* Registration
* Home Dashboard
* Recipe Details
* All Recipes
* Favorites
* User Profile
* Meal Preferences
* Weekly Meal Plan
* Saved Meal Plan
* AI Chat Assistant

---

# 🤖 AI Assistant Architecture

The AI assistant follows a simple client-server architecture:

```text
┌─────────────────────────────┐
│       Android App           │
│                             │
│      ChatActivity           │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│        ChatViewModel        │
│                             │
│ UI State / Business Logic   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│       Retrofit / OkHttp     │
│                             │
│       POST /ai/chat         │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│      Node.js Backend        │
│                             │
│       AI Service            │
└─────────────────────────────┘
```

This keeps the Android UI independent from the backend AI implementation.

---

# 🏗️ Application Architecture

SmartMealPlanner follows the **MVVM (Model-View-ViewModel)** architecture pattern.

```text
┌─────────────────────────────────┐
│              UI                 │
│                                 │
│ Activities / Fragments          │
│ RecyclerViews / Adapters        │
└───────────────┬─────────────────┘
                │
                ▼
┌─────────────────────────────────┐
│           ViewModel             │
│                                 │
│ UI State / Business Logic       │
└───────────────┬─────────────────┘
                │
                ▼
┌─────────────────────────────────┐
│             Data                │
│                                 │
│ API / Models / TokenManager     │
└───────────────┬─────────────────┘
                │
                ▼
┌─────────────────────────────────┐
│        Node.js Backend          │
│                                 │
│           REST API              │
└───────────────┬─────────────────┘
                │
                ▼
┌─────────────────────────────────┐
│            MongoDB              │
└─────────────────────────────────┘
```

### Architecture Responsibilities

**UI Layer**

Responsible for:

* Rendering application screens
* Handling user interactions
* Displaying loading and error states
* Observing ViewModel data

**ViewModel Layer**

Responsible for:

* Managing UI state
* Calling repository/API functionality
* Handling asynchronous operations
* Surviving configuration changes

**Data Layer**

Responsible for:

* REST API communication
* Authentication
* Request/response models
* Token management

---

# 🛠️ Tech Stack

## Language

* Kotlin

## Architecture

* MVVM
* Separation of UI and data responsibilities

## Android / Jetpack

* Android SDK
* ViewModel
* LiveData
* DataStore Preferences
* Navigation Component
* Kotlin Coroutines

## Networking

* Retrofit 2
* OkHttp
* Gson
* REST APIs

## UI

* XML Layouts
* ViewBinding
* RecyclerView
* Material Design 3
* Custom Adapters

## Image Loading

* Glide

## Authentication

* Token-based authentication
* DataStore Preferences
* OkHttp Interceptor

## Backend Integration

* Node.js
* Express.js
* REST API
* MongoDB

## Development Tools

* Android Studio
* Gradle
* Git
* GitHub

---

# 📂 Project Structure

```text
com.example.smartmealplanner
│
├── adapter
│   ├── CategoryAdapter
│   ├── RecommendationAdapter
│   ├── RecipeWeekAdapter
│   ├── AllRecipesAdapter
│   ├── MealPlanAdapter
│   └── ChatAdapter
│
├── data
│   │
│   ├── api
│   │   ├── RetrofitClient
│   │   ├── ApiService
│   │   ├── AuthApi
│   │   ├── MealPlanApiService
│   │   ├── TokenManager
│   │   └── AuthInterceptor
│   │
│   └── model
│       ├── Recipe
│       ├── Category
│       ├── User
│       ├── Auth
│       ├── RecipeResponse
│       ├── SearchResponse
│       ├── SingleRecipeResponse
│       ├── MealPlan
│       ├── MealPlanModels
│       ├── MealPreferences
│       ├── ChatRequest
│       ├── ChatResponse
│       └── ChatMessage
│
├── ui
│   │
│   ├── activity
│   │   ├── MainActivity
│   │   ├── HomeActivity
│   │   ├── RecipeActivity
│   │   ├── AllRecipesActivity
│   │   ├── FavoriteRecipesActivity
│   │   ├── ProfileActivity
│   │   ├── MealPreferencesActivity
│   │   ├── SavedMealPlanActivity
│   │   ├── WeeklyMealPlanActivity
│   │   └── ChatActivity
│   │
│   ├── auth
│   │   ├── LoginFragment
│   │   ├── RegisterFragment
│   │   └── AuthViewModel
│   │
│   ├── viewmodel
│   │   ├── HomeViewModel
│   │   ├── ChatViewModel
│   │   ├── ChatViewModelFactory
│   │   └── ...
│   │
│   └── common
│       ├── Interfaces
│       └── ClickListeners
│
└── utils
    └── Shared utility classes
```

---

# 🔌 Backend Integration

The Android application communicates with the SmartMealPlanner Node.js backend using REST APIs.

```text
Android Application
        │
        │ Retrofit
        │
        ▼
     OkHttp
        │
        ▼
   REST API
        │
        ▼
 Node.js / Express
        │
        ├──────────────► MongoDB
        │
        └──────────────► AI Service
```

### Main API Areas

The Android application communicates with backend APIs for:

* Authentication
* User information
* Recipe retrieval
* Recipe search
* Favorites
* Meal planning
* Meal replacement
* AI chat

---

# 🔐 Authentication Flow

SmartMealPlanner uses token-based authentication.

```text
┌───────────────┐
│     Login     │
└───────┬───────┘
        │
        ▼
┌───────────────────┐
│   Node.js API     │
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│ Authentication    │
│      Token        │
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│ DataStore         │
│ Preferences       │
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│ OkHttp Interceptor│
└────────┬──────────┘
         │
         ▼
 Authenticated API
    Requests
```

The token is retrieved when authenticated requests are made and attached to the request through the OkHttp interceptor.

---

# 📅 Meal Planning Flow

```text
User Preferences
       │
       ▼
┌──────────────────────┐
│ Meal Preferences     │
│                      │
│ Diet                 │
│ Calories             │
│ Meals / Day          │
│ Cuisine              │
│ Cooking Time         │
└──────────┬───────────┘
           │
           ▼
    Backend API
           │
           ▼
  Meal Plan Generation
           │
           ▼
┌──────────────────────┐
│ Weekly Meal Plan     │
│                      │
│ Breakfast            │
│ Lunch                │
│ Dinner               │
└──────────┬───────────┘
           │
           ▼
     Meal Replacement
```

---



# 🚀 Getting Started

## Prerequisites

Make sure the following are installed:

* Android Studio
* JDK 17 or higher
* Android SDK 36
* Git
* An Android emulator or physical Android device

### Minimum Android Version

```text
Minimum SDK: 24
Compile SDK: 36
Target SDK: 36
```

---

## 1. Clone the Repository

```bash
git clone https://github.com/jeenwalvaishali/SMP-FrontendAndroidAPP.git
```

Navigate into the project:

```bash
cd SMP-FrontendAndroidAPP
```

---

## 2. Open the Project

Open the project in **Android Studio**.

Allow Android Studio to:

* Download required Gradle dependencies
* Sync the project
* Build the application

---

## 3. Configure the Backend URL

The Android application reads the backend URL from `local.properties`.

Add:

```properties
BASE_URL=https://your-backend-url.com/
```

The backend URL should point to the deployed SmartMealPlanner API.

For local development, use the appropriate URL for your emulator/device configuration.

> Do not commit private configuration or secrets to GitHub.

---

## 4. Sync Gradle

In Android Studio:

```text
File → Sync Project with Gradle Files
```

Wait for the Gradle synchronization to complete.

---

## 5. Run the Application

Connect an Android device or start an Android emulator.

Then run:

```text
Run → Run 'app'
```

---

# 🧭 Application Flow

A typical user flow is:

```text
Launch App
    │
    ▼
Login / Register
    │
    ▼
Home Dashboard
    │
    ├──────────────► Discover Recipes
    │
    ├──────────────► Search Recipes
    │
    ├──────────────► View Favorites
    │
    ├──────────────► AI Assistant
    │
    └──────────────► Meal Planner
                         │
                         ▼
                  Set Preferences
                         │
                         ▼
                  Generate Plan
                         │
                         ▼
                  Replace Meals
```

---

# 🧪 Error & State Handling

The application handles common API and UI states including:

* Loading states
* API responses
* Empty results
* Authentication state
* Network/API errors
* Invalid user input
* Session/token handling

Kotlin Coroutines are used for asynchronous network operations so that network requests do not block the main UI thread.

---

# 🔒 Security Considerations

The application follows several security practices:

* Authentication tokens are stored using DataStore Preferences
* Authentication headers are added through OkHttp
* API communication is separated from UI components
* Backend authentication and authorization are handled by the server
* Private configuration is kept outside the source code through `local.properties`

> Production deployments should additionally use HTTPS, secure secret management, token expiration/refresh strategies, and appropriate backend authorization controls.

---

# 🌐 Related Repositories

### Android Application

📱 SmartMealPlanner Android App

https://github.com/jeenwalvaishali/SMP-FrontendAndroidAPP

### Backend API

⚙️ SmartMealPlanner Backend

https://github.com/jeenwalvaishali/SmartMealPlanner-backend

### Admin Panel

🛠️ SmartMealPlanner Admin Panel

https://github.com/jeenwalvaishali/SMP-Admin-Panel

---

# 💡 Engineering Highlights

This project demonstrates practical experience with:

* Android application development using Kotlin
* MVVM architecture
* REST API integration
* Retrofit and OkHttp
* Kotlin Coroutines
* DataStore-based token management
* Authentication flows
* RecyclerView and custom adapters
* Search and filtering
* Personalized meal-plan generation
* AI API integration
* Meal replacement workflows
* Backend-driven application architecture
* Error and loading state management
* Git/GitHub-based development


---

# 👩‍💻 Author

**Vaishali Jeenwal**

Software Engineer | Android | React | Node.js

Built as a full-stack software engineering project to explore modern application development, API integration, personalized meal planning, and AI-powered user experiences.


## 📱 Screenshots

### Login Screen

<img src="docs/images/SMP_LoginPage.jpg" width="250">

### Home Screen

<img src="docs/images/SMP_HomePage.jpg" width="250">

### Category Home

<img src="docs/images/SMP_CategoryHome.jpg" width="250">

### Favorite Page

<img src="docs/images/SMP_FavoritePage.jpg" width="250">

### Recipe Details Page

<img src="docs/images/SMP_RecipeDetailsPage.jpg" width="250">

### Search Result

<img src="docs/images/SMP_SearchResultPage.jpg" width="250">

### Settings Page

<img src="docs/images/SMP_SettingPage.jpg" width="250">

### Menu Option

<img src="docs/images/SMP_MenuOptions.jpg" width="250">

---
