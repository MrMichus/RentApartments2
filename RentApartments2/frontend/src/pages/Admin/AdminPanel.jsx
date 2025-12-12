import { useState, useEffect } from 'react';
import { adminService, mieszkaniaService } from '../../api/services';
import './AdminPanel.css';

const AdminPanel = () => {
    const [activeTab, setActiveTab] = useState('mieszkania');
    const [users, setUsers] = useState([]);
    const [mieszkania, setMieszkania] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [actionLoading, setActionLoading] = useState(null);

    useEffect(() => {
        if (activeTab === 'users') {
            fetchUsers();
        } else {
            fetchMieszkania();
        }
    }, [activeTab]);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            const response = await adminService.getAllUsers();
            setUsers(response.data);
        } catch (err) {
            setError('Błąd podczas ładowania użytkowników');
        } finally {
            setLoading(false);
        }
    };

    const fetchMieszkania = async () => {
        try {
            setLoading(true);
            const response = await adminService.getAllMieszkania();
            setMieszkania(response.data);
        } catch (err) {
            setError('Błąd podczas ładowania mieszkań');
        } finally {
            setLoading(false);
        }
    };

    const handleApprove = async (id) => {
        setActionLoading(id);
        try {
            await adminService.approveMieszkanie(id);
            setSuccess('Ogłoszenie zostało zatwierdzone');
            fetchMieszkania();
        } catch (err) {
            setError('Błąd podczas zatwierdzania');
        } finally {
            setActionLoading(null);
            setTimeout(() => setSuccess(''), 3000);
        }
    };

    const handleReject = async (id) => {
        setActionLoading(id);
        try {
            await adminService.rejectMieszkanie(id);
            setSuccess('Ogłoszenie zostało odrzucone');
            fetchMieszkania();
        } catch (err) {
            setError('Błąd podczas odrzucania');
        } finally {
            setActionLoading(null);
            setTimeout(() => setSuccess(''), 3000);
        }
    };

    const handleDeleteMieszkanie = async (id) => {
        if (!window.confirm('Czy na pewno chcesz usunąć to ogłoszenie?')) return;
        
        setActionLoading(id);
        try {
            await mieszkaniaService.delete(id);
            setSuccess('Ogłoszenie zostało usunięte');
            fetchMieszkania();
        } catch (err) {
            setError('Błąd podczas usuwania');
        } finally {
            setActionLoading(null);
            setTimeout(() => setSuccess(''), 3000);
        }
    };

    const handleChangeRole = async (userId, newRole) => {
        setActionLoading(userId);
        try {
            await adminService.changeUserRole(userId, newRole);
            setSuccess('Rola została zmieniona');
            fetchUsers();
        } catch (err) {
            setError('Błąd podczas zmiany roli');
        } finally {
            setActionLoading(null);
            setTimeout(() => setSuccess(''), 3000);
        }
    };

    const handleDeleteUser = async (userId) => {
        if (!window.confirm('Czy na pewno chcesz usunąć tego użytkownika?')) return;
        
        setActionLoading(userId);
        try {
            await adminService.deleteUser(userId);
            setSuccess('Użytkownik został usunięty');
            fetchUsers();
        } catch (err) {
            setError('Błąd podczas usuwania użytkownika');
        } finally {
            setActionLoading(null);
            setTimeout(() => setSuccess(''), 3000);
        }
    };

    const getStatusBadge = (status) => {
        switch (status) {
            case 'APPROVED':
                return <span className="badge approved">✓ Zatwierdzone</span>;
            case 'PENDING':
                return <span className="badge pending">⏳ Oczekujące</span>;
            case 'REJECTED':
                return <span className="badge rejected">✕ Odrzucone</span>;
            default:
                return <span className="badge">{status}</span>;
        }
    };

    return (
        <div className="admin-page">
            <div className="admin-header">
                <h1>🛠️ Panel Administratora</h1>
                <p>Zarządzaj użytkownikami i ogłoszeniami</p>
            </div>

            {error && <div className="error-message">{error}</div>}
            {success && <div className="success-message">{success}</div>}

            <div className="admin-tabs">
                <button
                    className={`tab ${activeTab === 'mieszkania' ? 'active' : ''}`}
                    onClick={() => setActiveTab('mieszkania')}
                >
                    🏠 Mieszkania
                </button>
                <button
                    className={`tab ${activeTab === 'users' ? 'active' : ''}`}
                    onClick={() => setActiveTab('users')}
                >
                    👥 Użytkownicy
                </button>
            </div>

            <div className="admin-content">
                {loading ? (
                    <div className="loading-container">
                        <div className="loading-spinner"></div>
                        <p>Ładowanie...</p>
                    </div>
                ) : activeTab === 'mieszkania' ? (
                    <div className="admin-table-container">
                        <div className="table-header">
                            <h2>Wszystkie ogłoszenia</h2>
                            <span className="count">{mieszkania.length} ogłoszeń</span>
                        </div>

                        {mieszkania.length === 0 ? (
                            <div className="empty-state">
                                <p>Brak ogłoszeń do wyświetlenia</p>
                            </div>
                        ) : (
                            <table className="admin-table">
                                <thead>
                                    <tr>
                                        <th>Mieszkanie</th>
                                        <th>Właściciel</th>
                                        <th>Cena</th>
                                        <th>Status</th>
                                        <th>Akcje</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {mieszkania.map(m => (
                                        <tr key={m.id}>
                                            <td>
                                                <div className="mieszkanie-cell">
                                                    <strong>{m.tytul}</strong>
                                                    <span>{m.adres?.miasto}, {m.adres?.ulica}</span>
                                                </div>
                                            </td>
                                            <td>{m.wlascicielName}</td>
                                            <td className="price">{m.cenaMiesieczna?.toLocaleString()} zł</td>
                                            <td>{getStatusBadge(m.status)}</td>
                                            <td>
                                                <div className="action-buttons">
                                                    {m.status === 'PENDING' && (
                                                        <>
                                                            <button
                                                                className="btn-approve"
                                                                onClick={() => handleApprove(m.id)}
                                                                disabled={actionLoading === m.id}
                                                            >
                                                                ✓
                                                            </button>
                                                            <button
                                                                className="btn-reject"
                                                                onClick={() => handleReject(m.id)}
                                                                disabled={actionLoading === m.id}
                                                            >
                                                                ✕
                                                            </button>
                                                        </>
                                                    )}
                                                    <button
                                                        className="btn-delete"
                                                        onClick={() => handleDeleteMieszkanie(m.id)}
                                                        disabled={actionLoading === m.id}
                                                    >
                                                        🗑️
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                ) : (
                    <div className="admin-table-container">
                        <div className="table-header">
                            <h2>Wszyscy użytkownicy</h2>
                            <span className="count">{users.length} użytkowników</span>
                        </div>

                        {users.length === 0 ? (
                            <div className="empty-state">
                                <p>Brak użytkowników do wyświetlenia</p>
                            </div>
                        ) : (
                            <table className="admin-table">
                                <thead>
                                    <tr>
                                        <th>Użytkownik</th>
                                        <th>Email</th>
                                        <th>Telefon</th>
                                        <th>Rola</th>
                                        <th>Akcje</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {users.map(u => (
                                        <tr key={u.id}>
                                            <td>
                                                <div className="user-cell">
                                                    <span className="avatar">👤</span>
                                                    <strong>{u.username} {u.surname}</strong>
                                                </div>
                                            </td>
                                            <td>{u.email}</td>
                                            <td>{u.phoneNumber}</td>
                                            <td>
                                                <select
                                                    value={u.roles?.includes('ADMIN') ? 'ADMIN' : 'USER'}
                                                    onChange={(e) => handleChangeRole(u.id, e.target.value)}
                                                    disabled={actionLoading === u.id}
                                                    className="role-select"
                                                >
                                                    <option value="USER">USER</option>
                                                    <option value="ADMIN">ADMIN</option>
                                                </select>
                                            </td>
                                            <td>
                                                <div className="action-buttons">
                                                    <button
                                                        className="btn-delete"
                                                        onClick={() => handleDeleteUser(u.id)}
                                                        disabled={actionLoading === u.id}
                                                    >
                                                        🗑️
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminPanel;
