/**
 * Pre-defined Meal Recipe Bundles
 */
export const recipeBundles = [
  {
    id: 'guacamole',
    name: 'Fresh Guacamole Kit',
    items: [
      { name: 'Hass Avocados (3 pcs)', category: 'produce', qty: 3, unit: 'pcs', estPrice: 4.50 },
      { name: 'Red Onion', category: 'produce', qty: 1, unit: 'pc', estPrice: 0.95 },
      { name: 'Fresh Limes', category: 'produce', qty: 2, unit: 'pcs', estPrice: 1.20 },
      { name: 'Organic Cilantro Bunch', category: 'produce', qty: 1, unit: 'bunch', estPrice: 1.49 },
      { name: 'Tortilla Chips Bag', category: 'pantry', qty: 1, unit: 'bag', estPrice: 3.99 }
    ]
  },
  {
    id: 'salmon-bowl',
    name: 'Mediterranean Salmon Bowl',
    items: [
      { name: 'Fresh Atlantic Salmon Fillet', category: 'meat', qty: 1, unit: 'lb', estPrice: 12.99 },
      { name: 'Organic Quinoa', category: 'pantry', qty: 1, unit: 'box', estPrice: 4.29 },
      { name: 'Baby Spinach', category: 'produce', qty: 1, unit: 'tub', estPrice: 3.50 },
      { name: 'Extra Virgin Olive Oil', category: 'pantry', qty: 1, unit: 'bottle', estPrice: 8.99 },
      { name: 'Feta Cheese Block', category: 'dairy', qty: 1, unit: 'pack', estPrice: 4.75 }
    ]
  },
  {
    id: 'overnight-oats',
    name: 'Overnight Protein Oats',
    items: [
      { name: 'Rolled Oats Bag', category: 'pantry', qty: 1, unit: 'bag', estPrice: 3.89 },
      { name: 'Almond Milk (Unsweetened)', category: 'dairy', qty: 1, unit: 'carton', estPrice: 3.29 },
      { name: 'Chia Seeds', category: 'pantry', qty: 1, unit: 'pouch', estPrice: 4.99 },
      { name: 'Fresh Blueberries', category: 'produce', qty: 1, unit: 'punnet', estPrice: 3.99 }
    ]
  }
];
