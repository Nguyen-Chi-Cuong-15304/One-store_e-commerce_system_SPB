// Các hàm utility
const utils = {
    formatCurrency: (amount) => {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    },

    getStatusClass: (status) => {
        const statusMap = {
            'COMPLETED': 'completed',
            'PENDING': 'pending',
            'HOÀN THÀNH': 'completed',
            'ĐANG XỬ LÝ': 'pending'
        };
        return statusMap[status.toUpperCase()] || '';
    },

    showError: (message) => {
        alert(`Lỗi: ${message}`);
    }
};

// Class chính xử lý Dashboard
class Dashboard {
    constructor() {
        this.charts = {};
        this.initializeEventListeners();
    }

    // Khởi tạo các event listeners
    initializeEventListeners() {
        document.getElementById('refresh-orders').addEventListener('click', () => {
            this.loadDashboardData();
        });
    }

    // Load tất cả dữ liệu dashboard
    async loadDashboardData() {
        try {
            const response = await fetch('/api/dashboard/stats');
            if (!response.ok) {
                throw new Error('Không thể tải dữ liệu dashboard');
            }
            const data = await response.json();
            this.updateDashboard(data);
        } catch (error) {
            utils.showError(error.message);
        }
    }

    // Cập nhật toàn bộ dashboard với dữ liệu mới
    updateDashboard(data) {
        this.updateStats(data);
        this.updateRevenueChart(data.revenueChart);
        this.updateTopProductsChart(data.topProducts);
        this.updateRecentOrders(data.recentOrders);
    }

    // Cập nhật các thống kê
    updateStats(data) {
        document.querySelector('#revenue-card .value').textContent = 
            utils.formatCurrency(data.totalRevenue);
        document.querySelector('#orders-card .value').textContent = 
            data.totalOrders.toLocaleString();
        document.querySelector('#customers-card .value').textContent = 
            data.totalCustomers.toLocaleString();
        document.querySelector('#conversion-card .value').textContent = 
            `${data.conversionRate}%`;
    }

    // Cập nhật biểu đồ doanh thu
    updateRevenueChart(revenueData) {
        if (this.charts.revenue) {
            this.charts.revenue.destroy();
        }

        const ctx = document.getElementById('revenueChart').getContext('2d');
        this.charts.revenue = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: revenueData.map(item => `Tháng ${item.month}`),
                datasets: [{
                    label: 'Doanh thu',
                    data: revenueData.map(item => item.revenue),
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: value => utils.formatCurrency(value)
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: context => utils.formatCurrency(context.raw)
                        }
                    }
                }
            }
        });
    }

    // Cập nhật biểu đồ top sản phẩm
    updateTopProductsChart(productsData) {
        if (this.charts.products) {
            this.charts.products.destroy();
        }

        const ctx = document.getElementById('topProductsChart').getContext('2d');
        this.charts.products = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: productsData.map(product => product.name),
                datasets: [{
                    data: productsData.map(product => product.revenue),
                    backgroundColor: [
                        '#FF6384',
                        '#36A2EB',
                        '#FFCE56',
                        '#4BC0C0',
                        '#9966FF'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: context => utils.formatCurrency(context.raw)
                        }
                    }
                }
            }
        });
    }

    // Cập nhật bảng đơn hàng gần đây
    updateRecentOrders(ordersData) {
        const tbody = document.querySelector('#recentOrdersTable tbody');
        tbody.innerHTML = ordersData.map(order => `
            <tr>
                <td>${order.orderid}</td>
                <td>${order.name}</td>
                <td>${order.address}</td>
                <td>${utils.formatCurrency(parseFloat(order.total_amount))}</td>
                <td>
                    <span class="status ${utils.getStatusClass(order.status)}">
                        ${order.status}
                    </span>
                </td>
            </tr>
        `).join('');
    }
}

// Khởi tạo dashboard khi trang load xong
document.addEventListener('DOMContentLoaded', () => {
    const dashboard = new Dashboard();
    dashboard.loadDashboardData();
    
    // Auto refresh mỗi 5 phút
    setInterval(() => {
        dashboard.loadDashboardData();
    }, 5 * 60 * 1000);
});