import { ShoppingStore } from './shopping-store.js';
import { categories } from './categories.js';
import { recipeBundles } from './recipes.js';

document.addEventListener('DOMContentLoaded', () => {
  const store = new ShoppingStore();
  let currentCategory = 'all';

  const itemsListEl = document.getElementById('items-list');
  const catScrollerEl = document.getElementById('category-scroller');
  const listSelectorEl = document.getElementById('list-selector');
  const quickAddForm = document.getElementById('quick-add-form');
  const quickAddInput = document.getElementById('quick-add-input');
  const progressFill = document.getElementById('progress-fill');
  const progressRatioEl = document.getElementById('progress-ratio');
  const budgetPillEl = document.getElementById('budget-pill');
  const frameToggleBtn = document.getElementById('frame-toggle-btn');
  const appStage = document.getElementById('app-stage');
  const recipeSelectEl = document.getElementById('recipe-select');
  const importRecipeBtn = document.getElementById('import-recipe-btn');

  // Populate Recipe Options
  if (recipeSelectEl) {
    recipeSelectEl.innerHTML = recipeBundles.map(r => `
      <option value="${r.id}">${r.name} (${r.items.length} items)</option>
    `).join('');
  }

  // Render Category Filter Pills
  if (catScrollerEl) {
    catScrollerEl.innerHTML = categories.map(c => `
      <button class="cat-pill ${c.id === 'all' ? 'active' : ''}" data-cat="${c.id}">
        <span>${c.emoji}</span>
        <span>${c.name}</span>
      </button>
    `).join('');

    catScrollerEl.addEventListener('click', (e) => {
      const btn = e.target.closest('.cat-pill');
      if (!btn) return;
      catScrollerEl.querySelectorAll('.cat-pill').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      currentCategory = btn.getAttribute('data-cat');
      renderItems();
    });
  }

  // Render Items
  function renderItems() {
    if (!itemsListEl) return;
    const activeList = store.getActiveList();
    let filtered = activeList.items;

    if (currentCategory !== 'all') {
      filtered = filtered.filter(i => i.category === currentCategory);
    }

    if (filtered.length === 0) {
      itemsListEl.innerHTML = `
        <div style="text-align:center; padding: 3rem 1rem; color: var(--text-muted);">
          <div style="font-size:2.5rem; margin-bottom:0.5rem;">🧺</div>
          <p style="font-weight:600;">No items in this category.</p>
          <p style="font-size:0.8rem;">Type an item name below to quick-add.</p>
        </div>
      `;
    } else {
      itemsListEl.innerHTML = filtered.map(item => `
        <div class="item-card ${item.completed ? 'completed' : ''}" data-id="${item.id}">
          <button class="item-check-btn" data-action="toggle" data-id="${item.id}">
            ${item.completed ? '✓' : ''}
          </button>
          <div class="item-details">
            <div class="item-name">${item.name}</div>
            <div class="item-meta">
              <span>Qty: ${item.qty} ${item.unit}</span>
              <span>&bull;</span>
              <span style="text-transform:capitalize;">${item.category}</span>
            </div>
          </div>
          <span class="item-price">$${(item.estPrice || 0).toFixed(2)}</span>
          <button class="item-delete-btn" data-action="delete" data-id="${item.id}" title="Remove item">&times;</button>
        </div>
      `).join('');
    }

    updateMetrics();
  }

  // Update Metrics & Budget UI
  function updateMetrics() {
    const metrics = store.getMetrics();
    if (progressFill) progressFill.style.width = `${metrics.percent}%`;
    if (progressRatioEl) progressRatioEl.textContent = `${metrics.completedItems} of ${metrics.totalItems} done (${metrics.percent}%)`;
    if (budgetPillEl) {
      budgetPillEl.textContent = `$${metrics.totalEstCost.toFixed(2)} / $${metrics.budget.toFixed(2)}`;
      if (metrics.isOverBudget) {
        budgetPillEl.style.background = '#FEE2E2';
        budgetPillEl.style.color = '#B91C1C';
      } else {
        budgetPillEl.style.background = '#D1FAE5';
        budgetPillEl.style.color = '#065F46';
      }
    }
  }

  renderItems();

  // Item List Actions (Toggle / Delete)
  itemsListEl?.addEventListener('click', (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const id = btn.getAttribute('data-id');
    const action = btn.getAttribute('data-action');

    if (action === 'toggle') {
      store.toggleItem(id);
      renderItems();
    } else if (action === 'delete') {
      store.deleteItem(id);
      renderItems();
    }
  });

  // Quick Add Item
  quickAddForm?.addEventListener('submit', (e) => {
    e.preventDefault();
    const val = quickAddInput?.value.trim();
    if (!val) return;

    store.addItem({
      name: val,
      qty: 1,
      unit: 'pcs',
      estPrice: 3.50
    });

    quickAddInput.value = '';
    renderItems();
  });

  // Import Recipe
  importRecipeBtn?.addEventListener('click', () => {
    const selectedId = recipeSelectEl?.value;
    const recipe = recipeBundles.find(r => r.id === selectedId);
    if (recipe) {
      recipe.items.forEach(item => {
        store.addItem(item);
      });
      renderItems();
    }
  });

  // Toggle Mobile Frame vs Desktop Fullscreen
  frameToggleBtn?.addEventListener('click', () => {
    appStage?.classList.toggle('fullscreen-mode');
    const isFull = appStage?.classList.contains('fullscreen-mode');
    frameToggleBtn.textContent = isFull ? '📱 Switch to Phone Mockup' : '🖥️ Switch to Fullscreen';
  });

  // List Selector
  listSelectorEl?.addEventListener('change', (e) => {
    store.setActiveList(e.target.value);
    renderItems();
  });
});
