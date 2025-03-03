# Reposphere 🌐

Reposphere is a simple, sleek GitHub application that allows users to seamlessly view and search their GitHub repositories. By utilizing the GitHub API, Reposphere provides an intuitive interface where users can access detailed repository information and interact with their repositories in real-time. The app also offers smooth Google Sign-In authentication powered by Firebase.

## Features 🚀

- **Google Sign-In Authentication**: Sign in effortlessly with your Google account using Firebase Authentication.
- **View GitHub Repositories**: Fetch and display a comprehensive list of repositories from the authenticated GitHub account.
- **Search Repositories**: Quickly search for repositories based on user-defined queries like name, description, or language using the GitHub Search API.
- **Repository Details**: View rich repository details, including stars, forks, and other key metadata to explore repositories more deeply.

## Tech Stack 🛠️

- **Kotlin**: The main language for the development of the app.
- **GitHub API**: Fetches repositories and related data.
- **Firebase Authentication**: Provides easy Google Sign-In for users.
- **Room Database**: Stores repository data locally for offline use.
- **Jetpack Components**: Leverages components like RecyclerView, ViewModel, and LiveData for UI and lifecycle management.

## Architecture 🏗️

Reposphere follows a **clean architecture** pattern for scalability and maintainability, ensuring that the codebase remains modular and easy to test.

### Core Components 🔑

- **Authentication**: Manages user sign-in using Firebase Authentication and Google Sign-In.
- **Main**: Displays the list of repositories fetched from the GitHub API, presented in a user-friendly interface.
- **Search**: Enables users to search for repositories by name, description, or language using the GitHub Search API.

## Installation ⚙️

To get started with Reposphere, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/reposphere.git
