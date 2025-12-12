import api from './axios';

// Auth
export const authService = {
    login: (data) => api.post('/auth/login', data),
    register: (data) => api.post('/auth/register', data),
    getCurrentUser: () => api.get('/auth/me'),
    updateProfile: (data) => api.put('/auth/profile', data),
};

// Mieszkania
export const mieszkaniaService = {
    getAll: () => api.get('/mieszkania'),
    getById: (id) => api.get(`/mieszkania/${id}`),
    search: (params) => api.get('/mieszkania/filter', { params }),
    getMyMieszkania: () => api.get('/mieszkania/my'),
    create: (data) => api.post('/mieszkania', data),
    update: (id, data) => api.put(`/mieszkania/${id}`, data),
    delete: (id) => api.delete(`/mieszkania/${id}`),
};

// Chat
export const chatService = {
    getMyChats: () => api.get('/chat'),
    getChatById: (chatId) => api.get(`/chat/${chatId}`),
    getOrCreateChat: (userId) => api.get(`/chat/user/${userId}`),
    sendMessage: (recipientId, tresc) => api.post('/chat/send', { recipientId, tresc }),
};

// Admin
export const adminService = {
    getAllUsers: () => api.get('/admin/users'),
    deleteUser: (id) => api.delete(`/admin/users/${id}`),
    changeUserRole: (id, roleName) => api.put(`/admin/users/${id}/role`, { roleName }),
    getAllMieszkania: () => api.get('/admin/mieszkania'),
    getPendingMieszkania: () => api.get('/admin/mieszkania/pending'),
    approveMieszkanie: (id) => api.put(`/admin/mieszkania/${id}/approve`),
    rejectMieszkanie: (id) => api.put(`/admin/mieszkania/${id}/reject`),
};
