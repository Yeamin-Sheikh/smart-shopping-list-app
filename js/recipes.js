/**
 * Pre-defined Meal Recipe Bundles with High-Res Visuals
 */
export const recipeBundles = [
  {
    id: 'avocado-toast',
    name: 'Artisan Avocado Toast Deluxe',
    image: 'recipe-avocado-toast.jpg',
    description: 'Rustic sourdough, ripe hass avocado, poached eggs, and chili flakes.',
    items: [
      { name: 'Artisan Sourdough Loaf', category: 'bakery', qty: 1, unit: 'loaf', estPrice: 5.50 },
      { name: 'Hass Avocados (2 pcs)', category: 'produce', qty: 2, unit: 'pcs', estPrice: 3.50 },
      { name: 'Free-Range Large Eggs (Dozen)', category: 'dairy', qty: 1, unit: 'carton', estPrice: 4.99 },
      { name: 'Crushed Red Pepper Flakes', category: 'pantry', qty: 1, unit: 'jar', estPrice: 2.25 },
      { name: 'Microgreens Salad Blend', category: 'produce', qty: 1, unit: 'box', estPrice: 3.49 }
    ]
  },
  {
    id: 'smoothie-bowl',
    name: 'Superfood Acai Smoothie Bowl',
    image: 'recipe-smoothie-bowl.jpg',
    description: 'Pure organic acai blend topped with fresh berries, banana, and chia seeds.',
    items: [
      { name: 'Organic Frozen Acai Puree', category: 'frozen', qty: 1, unit: 'pack', estPrice: 6.99 },
      { name: 'Fresh Blueberries', category: 'produce', qty: 1, unit: 'punnet', estPrice: 3.99 },
      { name: 'Fresh Strawberries', category: 'produce', qty: 1, unit: 'clamshell', estPrice: 4.29 },
      { name: 'Organic Bananas', category: 'produce', qty: 1, unit: 'bunch', estPrice: 1.89 },
      { name: 'Organic Black Chia Seeds', category: 'pantry', qty: 1, unit: 'pouch', estPrice: 4.99 }
    ]
  },
  {
    id: 'salmon-bowl',
    name: 'Mediterranean Salmon Bowl',
    image: 'recipe-avocado-toast.jpg',
    description: 'Pan-seared Atlantic salmon with tricolor quinoa and baby spinach.',
    items: [
      { name: 'Fresh Atlantic Salmon Fillet', category: 'meat', qty: 1, unit: 'lb', estPrice: 12.99 },
      { name: 'Organic Quinoa', category: 'pantry', qty: 1, unit: 'box', estPrice: 4.29 },
      { name: 'Baby Spinach', category: 'produce', qty: 1, unit: 'tub', estPrice: 3.50 },
      { name: 'Extra Virgin Olive Oil', category: 'pantry', qty: 1, unit: 'bottle', estPrice: 8.99 },
      { name: 'Feta Cheese Block', category: 'dairy', qty: 1, unit: 'pack', estPrice: 4.75 }
    ]
  }
];
