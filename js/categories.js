/**
 * Smart Category Assigner
 * Matches grocery item names to categories automatically.
 */
export const categories = [
  { id: 'all', name: 'All Items', emoji: '🛒' },
  { id: 'produce', name: 'Produce', emoji: '🥦', color: '#10B981' },
  { id: 'dairy', name: 'Dairy & Eggs', emoji: '🥛', color: '#0284C7' },
  { id: 'bakery', name: 'Bakery', emoji: '🍞', color: '#F59E0B' },
  { id: 'meat', name: 'Meat & Seafood', emoji: '🥩', color: '#EF4444' },
  { id: 'pantry', name: 'Pantry', emoji: '🥫', color: '#8B5CF6' },
  { id: 'household', name: 'Household', emoji: '🧼', color: '#64748B' }
];

export function autoDetectCategory(itemName) {
  const lower = (itemName || '').toLowerCase();
  
  if (/apple|banana|avocado|onion|tomato|lime|lemon|cilantro|spinach|garlic|lettuce|berr|orange|potato|carrot|kale|grape|mango|pepper|cucumber|celery/i.test(lower)) {
    return 'produce';
  }
  if (/milk|cheese|egg|butter|yogurt|cream|cheddar|parmesan|mozzarella/i.test(lower)) {
    return 'dairy';
  }
  if (/bread|sourdough|bagel|croissant|tortilla|bun|pita|muffin|loaf/i.test(lower)) {
    return 'bakery';
  }
  if (/salmon|chicken|beef|steak|turkey|shrimp|fish|pork|bacon|tuna|lamb/i.test(lower)) {
    return 'meat';
  }
  if (/rice|pasta|oil|olive oil|cereal|beans|sauce|flour|sugar|spices|quinoa|oats|seeds/i.test(lower)) {
    return 'pantry';
  }
  if (/soap|detergent|paper towel|tissue|sponge|cleaner|shampoo|toothpaste/i.test(lower)) {
    return 'household';
  }
  return 'pantry';
}
