# Smart shopping list mobile app

A mobile-first progressive web application for tracking grocery lists, pantry inventory, and shopping budgets.

## Overview

Smart Cart provides a fast checklist interface for in-store shopping. It organizes items into smart categories automatically, tracks progress with a completion meter, and alerts when estimated costs exceed the target list budget.

## Key features

- **Dual display viewport:** Run full-screen on desktop or view inside a mobile simulator frame (iPhone 16 Pro mockup).
- **Smart category detection:** Automatically files items into Produce, Dairy, Bakery, Meat, Pantry, or Household based on keywords.
- **Budget and completion tracker:** Calculates total estimated spending against a preset budget with instant warning alerts.
- **In-store shopping workflow:** One-tap strike-through for checked items with completion percentage bar.
- **Recipe ingredient importer:** One-click import of ingredients for common recipes (Fresh Guacamole, Mediterranean Salmon Bowl, Overnight Protein Oats).
- **Multiple list support:** Switch between Weekly Groceries, BBQ Party Supplies, and custom lists.
- **Local persistence:** Stores items and list configurations in localStorage.

## Project structure

```
smart-shopping-list-app/
├── assets/
│   ├── images/
│   │   └── hero.jpg
│   └── svgs/
│       ├── logo.svg
│       └── icons.svg
├── css/
│   ├── main.css
│   └── components.css
├── js/
│   ├── app.js
│   ├── categories.js
│   ├── recipes.js
│   └── shopping-store.js
├── tests/
│   └── runner.js
├── index.html
├── package.json
└── README.md
```

## Running locally

Serve using Python or Node:

```powershell
# Using Python
python -m http.server 8000

# Or using Node
npm start
```

Visit `http://localhost:8000` in your web browser.

## Running tests

Run the test suite with Node:

```powershell
npm test
# or
node tests/runner.js
```

## License

MIT License.
