import assert from 'node:assert';
import fs from 'node:fs';
import path from 'node:path';
import { autoDetectCategory } from '../js/categories.js';
import { recipeBundles } from '../js/recipes.js';
import { ShoppingStore } from '../js/shopping-store.js';

console.log('--- Running Smart Shopping List App Tests ---');

// Mock localStorage
global.localStorage = (() => {
  let store = {};
  return {
    getItem: (key) => store[key] || null,
    setItem: (key, value) => { store[key] = value.toString(); },
    removeItem: (key) => { delete store[key]; },
    clear: () => { store = {}; }
  };
})();

// Test 1: Category Auto Detection
assert.strictEqual(autoDetectCategory('Honeycrisp Apples'), 'produce');
assert.strictEqual(autoDetectCategory('Organic Whole Milk'), 'dairy');
assert.strictEqual(autoDetectCategory('Artisan Sourdough Loaf'), 'bakery');
assert.strictEqual(autoDetectCategory('Atlantic Salmon Steak'), 'meat');
assert.strictEqual(autoDetectCategory('Extra Virgin Olive Oil'), 'pantry');
assert.strictEqual(autoDetectCategory('Dish Detergent Soap'), 'household');
console.log('✓ Automatic item category classification verified');

// Test 2: Recipe Bundles
assert.strictEqual(recipeBundles.length, 3, 'Should have 3 recipe templates');
const avo = recipeBundles.find(r => r.id === 'avocado-toast');
assert.ok(avo, 'Avocado toast recipe bundle should exist');
assert.strictEqual(avo.items.length, 5);
const smoothie = recipeBundles.find(r => r.id === 'smoothie-bowl');
assert.ok(smoothie, 'Smoothie bowl recipe bundle should exist');
assert.strictEqual(smoothie.items.length, 5);
console.log('✓ Recipe templates and item packages verified');

// Test 3: Shopping Store & Item Manipulation
const store = new ShoppingStore('test_shopping_store');
const initialItemsCount = store.getActiveList().items.length;

// Add item
const added = store.addItem({ name: 'Fresh Blueberries', estPrice: 4.99 });
assert.strictEqual(added.category, 'produce');
assert.strictEqual(store.getActiveList().items.length, initialItemsCount + 1);

// Toggle completion
const toggled = store.toggleItem(added.id);
assert.strictEqual(toggled.completed, true);
console.log('✓ Store item addition and completion toggling verified');

// Test 4: Metrics and Budget Tracking
const metrics = store.getMetrics();
assert.ok(metrics.totalItems > 0);
assert.ok(metrics.percent >= 0 && metrics.percent <= 100);
assert.ok(metrics.totalEstCost > 0);
console.log('✓ Store metrics and budget tracking verified');

// Test 5: Delete item
store.deleteItem(added.id);
assert.strictEqual(store.getActiveList().items.length, initialItemsCount);
console.log('✓ Item deletion verified');

// Test 6: Image assets verification
const imagesDir = path.resolve('assets/images');
assert.ok(fs.existsSync(path.join(imagesDir, 'hero.jpg')), 'hero.jpg must exist');
assert.ok(fs.existsSync(path.join(imagesDir, 'recipe-avocado-toast.jpg')), 'recipe-avocado-toast.jpg must exist');
assert.ok(fs.existsSync(path.join(imagesDir, 'recipe-smoothie-bowl.jpg')), 'recipe-smoothie-bowl.jpg must exist');
console.log('✓ High-resolution photography assets verified');

// Test 7: Config validation
const configPath = path.resolve('config.json');
assert.ok(fs.existsSync(configPath), 'config.json must exist');
const config = JSON.parse(fs.readFileSync(configPath, 'utf8'));
assert.strictEqual(config.appName, 'SmartCart Mobile');
assert.ok(config.defaultBudget > 0);
console.log('✓ Configuration file and budget persistence verified');

console.log('\nAll Smart Shopping List App tests passed successfully! (7/7)');
