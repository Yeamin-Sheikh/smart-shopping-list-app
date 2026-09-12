import { autoDetectCategory } from './categories.js';

/**
 * Shopping List & Pantry State Store
 */
export class ShoppingStore {
  constructor(storageKey = 'smart_shopping_lists') {
    this.storageKey = storageKey;
    this.activeListId = 'weekly-groceries';
    this.lists = this.loadLists();
  }

  loadLists() {
    try {
      if (typeof localStorage !== 'undefined') {
        const saved = localStorage.getItem(this.storageKey);
        if (saved) return JSON.parse(saved);
      }
    } catch {
      // Fallback
    }
    return this.getDefaultLists();
  }

  saveLists() {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(this.lists));
      }
    } catch (e) {
      console.warn('Could not persist lists', e);
    }
  }

  getDefaultLists() {
    return {
      'weekly-groceries': {
        name: 'Weekly Groceries',
        budget: 75.00,
        items: [
          { id: 'item-1', name: 'Organic Honeycrisp Apples', category: 'produce', qty: 4, unit: 'pcs', estPrice: 5.50, completed: false },
          { id: 'item-2', name: 'Whole Organic Milk', category: 'dairy', qty: 1, unit: 'gallon', estPrice: 4.89, completed: false },
          { id: 'item-3', name: 'San Francisco Sourdough Bread', category: 'bakery', qty: 1, unit: 'loaf', estPrice: 5.25, completed: true },
          { id: 'item-4', name: 'Free-Range Eggs (Dozen)', category: 'dairy', qty: 1, unit: 'carton', estPrice: 4.99, completed: true },
          { id: 'item-5', name: 'Atlantic Salmon Fillets', category: 'meat', qty: 2, unit: 'fillets', estPrice: 14.50, completed: false },
          { id: 'item-6', name: 'Extra Virgin Olive Oil', category: 'pantry', qty: 1, unit: 'bottle', estPrice: 9.99, completed: false }
        ]
      },
      'party-supplies': {
        name: 'Weekend BBQ & Party',
        budget: 120.00,
        items: [
          { id: 'item-7', name: 'Brioche Burger Buns', category: 'bakery', qty: 2, unit: 'packs', estPrice: 6.50, completed: false },
          { id: 'item-8', name: 'Grass-Fed Ground Beef', category: 'meat', qty: 3, unit: 'lbs', estPrice: 18.00, completed: false },
          { id: 'item-9', name: 'Paper Napkins & Plates', category: 'household', qty: 1, unit: 'pack', estPrice: 7.50, completed: false }
        ]
      }
    };
  }

  getActiveList() {
    return this.lists[this.activeListId] || this.lists['weekly-groceries'];
  }

  setActiveList(listId) {
    if (this.lists[listId]) {
      this.activeListId = listId;
      return true;
    }
    return false;
  }

  addItem(itemData) {
    const active = this.getActiveList();
    const category = itemData.category || autoDetectCategory(itemData.name);
    const newItem = {
      id: 'item-' + Date.now().toString(36),
      name: itemData.name,
      category,
      qty: Number(itemData.qty) || 1,
      unit: itemData.unit || 'pcs',
      estPrice: Number(itemData.estPrice) || 0,
      completed: false
    };

    active.items.unshift(newItem);
    this.saveLists();
    return newItem;
  }

  toggleItem(itemId) {
    const active = this.getActiveList();
    const item = active.items.find(i => i.id === itemId);
    if (item) {
      item.completed = !item.completed;
      this.saveLists();
    }
    return item;
  }

  deleteItem(itemId) {
    const active = this.getActiveList();
    active.items = active.items.filter(i => i.id !== itemId);
    this.saveLists();
  }

  clearCompleted() {
    const active = this.getActiveList();
    active.items = active.items.filter(i => !i.completed);
    this.saveLists();
  }

  getMetrics() {
    const active = this.getActiveList();
    const totalItems = active.items.length;
    const completedItems = active.items.filter(i => i.completed).length;
    const remainingItems = totalItems - completedItems;
    const percent = totalItems === 0 ? 0 : Math.round((completedItems / totalItems) * 100);

    const totalEstCost = Math.round(active.items.reduce((sum, i) => sum + (i.estPrice || 0), 0) * 100) / 100;
    const budget = active.budget || 100;
    const isOverBudget = totalEstCost > budget;

    return {
      totalItems,
      completedItems,
      remainingItems,
      percent,
      totalEstCost,
      budget,
      isOverBudget
    };
  }
}
