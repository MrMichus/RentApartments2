import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { mieszkaniaService } from '../../api/services';
import './MyMieszkania.css';

const MyMieszkania = () => {
    const [mieszkania, setMieszkania] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [deleteLoading, setDeleteLoading] = useState(null);

    useEffect(() => {
        fetchMyMieszkania();
    }, []);

    const fetchMyMieszkania = async () => {
        try {
            setLoading(true);
            const response = await mieszkaniaService.getMyMieszkania();
            setMieszkania(response.data);
        } catch (err) {
            setError('Błąd podczas ładowania Twoich mieszkań');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Czy na pewno chcesz usunąć to ogłoszenie?')) {
            return;
        }

        setDeleteLoading(id);
        try {
            await mieszkaniaService.delete(id);
            setMieszkania(mieszkania.filter(m => m.id !== id));
        } catch (err) {
            setError('Błąd podczas usuwania ogłoszenia');
        } finally {
            setDeleteLoading(null);
        }
    };

    const getStatusInfo = (status) => {
        switch (status) {
            case 'APPROVED':
                return { text: 'Zatwierdzone', class: 'approved', icon: '✓' };
            case 'PENDING':
                return { text: 'Oczekujące', class: 'pending', icon: '⏳' };
            case 'REJECTED':
                return { text: 'Odrzucone', class: 'rejected', icon: '✕' };
            default:
                return { text: status, class: '', icon: '' };
        }
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Ładowanie...</p>
            </div>
        );
    }

    return (
        <div className="my-mieszkania-page">
            <div className="page-header-with-action">
                <div>
                    <h1>Moje ogłoszenia</h1>
                    <p>Zarządzaj swoimi mieszkaniami</p>
                </div>
                <Link to="/dodaj-mieszkanie" className="btn-add">
                    ➕ Dodaj nowe
                </Link>
            </div>

            {error && <div className="error-message">{error}</div>}

            {mieszkania.length === 0 ? (
                <div className="empty-state">
                    <span>🏠</span>
                    <h3>Brak ogłoszeń</h3>
                    <p>Nie dodałeś jeszcze żadnego mieszkania.</p>
                    <Link to="/dodaj-mieszkanie" className="btn-primary">
                        Dodaj pierwsze ogłoszenie
                    </Link>
                </div>
            ) : (
                <div className="mieszkania-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Mieszkanie</th>
                                <th>Lokalizacja</th>
                                <th>Cena</th>
                                <th>Status</th>
                                <th>Akcje</th>
                            </tr>
                        </thead>
                        <tbody>
                            {mieszkania.map(m => {
                                const statusInfo = getStatusInfo(m.status);
                                return (
                                    <tr key={m.id}>
                                        <td className="mieszkanie-cell">
                                            <div className="mieszkanie-info">
                                                <strong>{m.tytul}</strong>
                                                <span>{m.powierzchnia} m² • {m.liczbaPokoi} pok.</span>
                                            </div>
                                        </td>
                                        <td>
                                            {m.adres?.miasto}, {m.adres?.ulica}
                                        </td>
                                        <td className="price-cell">
                                            {m.cenaMiesieczna?.toLocaleString()} zł
                                        </td>
                                        <td>
                                            <span className={`status-badge ${statusInfo.class}`}>
                                                {statusInfo.icon} {statusInfo.text}
                                            </span>
                                        </td>
                                        <td className="actions-cell">
                                            <Link to={`/mieszkania/${m.id}`} className="btn-action view">
                                                👁️
                                            </Link>
                                            <Link to={`/edytuj-mieszkanie/${m.id}`} className="btn-action edit">
                                                ✏️
                                            </Link>
                                            <button
                                                className="btn-action delete"
                                                onClick={() => handleDelete(m.id)}
                                                disabled={deleteLoading === m.id}
                                            >
                                                {deleteLoading === m.id ? '...' : '🗑️'}
                                            </button>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default MyMieszkania;
