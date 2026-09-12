# Smart Shopping List (Native Android App)

Native Android application for managing grocery shopping lists, tracking aisle locations, and monitoring trip budgets in real time.

Built with Java, AndroidX, Material Design 3, and a local SQLite database engine.

## Features

- **Real-Time Grocery Checklist:** Toggle items with instant strikethrough styling and real-time trip completion calculation.
- **Store and Aisle Organization:** Assign specific stores (e.g. Costco, Trader Joe's) and aisles (e.g. Aisle 4B) to each item to speed up in-store trips.
- **Trip Budget and Spending Analytics:** Set custom trip or monthly spending limits. Monitor estimated cart total versus in-cart purchased items.
- **Category Department Filtering:** Browse items by Produce, Dairy and Eggs, Bakery, Meat and Seafood, Pantry, Frozen, Beverages, or custom departments.
- **Recurring Staples:** Mark essential household staples as recurring. When clearing completed items, recurring staples automatically reset for the next grocery run.
- **Priority Badging:** Color-coded priority indicators (High, Medium, Low) for items that require immediate attention.
- **Local SQLite Engine:** Zero cloud accounts required. All lists, items, and historical budgets persist on-device via `SQLiteOpenHelper`.

## Project Structure

```
smart-shopping-list-app/
├── app/
│   ├── build.gradle                       # Module build settings and dependencies
│   ├── proguard-rules.pro                 # Proguard optimization rules
│   └── src/main/
│       ├── AndroidManifest.xml            # Application manifest and activity declarations
│       ├── java/com/yeaminsheikh/smartshopping/
│       │   ├── MainActivity.java          # Primary dashboard, search, filter chips, items list
│       │   ├── AddEditItemActivity.java   # Item creator and editor form
│       │   ├── BudgetOverviewActivity.java# Spending breakdown and budget limit controls
│       │   ├── CategoryManagerActivity.java# Department category management
│       │   ├── adapters/                  # RecyclerView adapters for items and chips
│       │   ├── database/DatabaseHelper.java# SQLite schema, migrations, queries, aggregations
│       │   ├── models/                    # Data models (ShoppingItem, Category, BudgetSummary)
│       │   └── utils/                     # Formatters and SharedPreferences managers
│       └── res/
│           ├── drawable/                  # Vector drawables and priority badges
│           ├── layout/                    # Activity and list item XML layouts
│           ├── values/                    # Colors, strings, dimens, and Material themes
│           └── mipmap-anydpi-v26/         # Adaptive launcher icons
├── gradle/wrapper/                        # Gradle 8.5 wrapper distribution
├── build.gradle                           # Top-level Gradle script
├── settings.gradle                        # Project settings
├── config.json                            # Local project configuration
└── tests/verify_android_app.py            # Automated structure and logic verification suite
```

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34 (compileSdk 34, minSdk 24, targetSdk 34)

## How to Build and Run

### Option 1: Android Studio (Recommended)
1. Open Android Studio.
2. Select **File > Open** and choose the `smart-shopping-list-app` folder.
3. Allow Gradle to sync dependencies automatically.
4. Select a connected physical device or Android Virtual Device (AVD) running API 24 or newer.
5. Click **Run** (or press Shift + F10).

### Option 2: Command Line (Gradle Wrapper)
```bash
# Debug APK compilation
./gradlew assembleDebug

# Run unit tests
./gradlew test
```
The compiled APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.

## Running Verification Tests

Run the built-in test suite to verify XML layouts, manifest bindings, SQLite algorithms, and Gradle compatibility:

```bash
python tests/verify_android_app.py
```
