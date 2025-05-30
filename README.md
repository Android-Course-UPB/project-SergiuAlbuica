Albuica Sergiu-Mihail \
SCPD1-B

# Shopping List App
A smart shopping list application built with Kotlin and Jetpack Compose that helps users organize their shopping experience.

## Features

- **Product Search**: Search for products with real-time suggestions from the Open Food Facts database
- **Local Fallback**: Continue working even when offline with local product suggestions
- **Category Management**: Create, view, and delete custom categories to organize your shopping list
- **User-Friendly Interface**: Clean, modern UI built with Jetpack Compose

## Architecture

This application follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Model**: Room Database and API services
- **View**: Jetpack Compose UI components
- **ViewModel**: Manages UI-related data and business logic

## Tech Stack

- Kotlin
- Jetpack Compose
- Retrofit for API integration
- OkHttp for network requests
- Room Database (for local storage)
- Coroutines for asynchronous operations
- Unit Tests

## API Integration

The app integrates with the [Open Food Facts API](https://world.openfoodfacts.org/) to provide product information and suggestions. A local fallback mechanism ensures the app remains functional even when offline or experiencing network issues.

## Usage

- **Add Items**: Tap the + button to add new items to your shopping list
- **Search Products**: Type in the item name to get product suggestions
- **Manage Categories**: Navigate to the category management screen to organize your shopping experience
- **Delete Items/Categories**: Use the delete icon to remove items or categories