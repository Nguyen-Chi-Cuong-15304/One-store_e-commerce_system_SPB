document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('productSearch');
    const suggestionsContainer = document.getElementById('suggestions');

    // Add error handling for missing elements
    if (!searchInput || !suggestionsContainer) {
        console.error('Required DOM elements not found');
        return;
    }

    searchInput.addEventListener('input', function() {
        const query = searchInput.value.trim();

        if (query.length > 0) {
            fetch(`/product_customer/search?query=${encodeURIComponent(query)}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Data received:', data);
                    // Ensure data is an array
                    const products = Array.isArray(data) ? data : [data];
                    
                    suggestionsContainer.style.display = 'block';
                    suggestionsContainer.innerHTML = '';

                    if (products.length > 0) {
                        const list = document.createElement('ul');
                        list.className = 'suggestions-list';

                        products.forEach(product => {
                            // Validate product object has required properties
                            if (!product.productID || !product.productName) {
                                console.warn('Invalid product data:', product);
                                return;
                            }

                            const listItem = document.createElement('li');
                            // Add price and other relevant information
                            listItem.innerHTML = `
                                <span class="product-name">${product.productName}</span>
                                ${product.price ? `<span class="product-price">${product.price.toFixed(2)} đ</span>` : ''}
                            `;
                            listItem.className = 'suggestion-item';

                            listItem.addEventListener('click', () => {
                                window.location.href = `/product_customer/product?id=${product.productID}`;
                            });

                            list.appendChild(listItem);
                            console.log('add Product:', product);
                        });

                        suggestionsContainer.appendChild(list);
                        console.log('add list successful:', list);
                    } else {
                        const noResults = document.createElement('div');
                        noResults.textContent = 'Không tìm thấy sản phẩm nào';
                        noResults.className = 'no-results';
                        suggestionsContainer.appendChild(noResults);
                    }
                    
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                    suggestionsContainer.innerHTML = `
                        <div class="error-message">
                            Đã xảy ra lỗi khi tìm kiếm. Vui lòng thử lại sau.
                        </div>
                    `;
                });
        } else {
            suggestionsContainer.style.display = 'none';
            suggestionsContainer.innerHTML = '';
        }
    });

    // Add debouncing to prevent too many API calls
    let searchTimeout;
    searchInput.addEventListener('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            // Trigger search here
        }, 300);
    });

    document.addEventListener('click', function(e) {
        if (!suggestionsContainer.contains(e.target) && e.target !== searchInput) {
            suggestionsContainer.innerHTML = '';
            suggestionsContainer.style.display = 'none';
        }
    });
});