import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../../api/services';
import { useAuth } from '../../context/AuthContext';
import './Profile.css';

const Profile = () => {
    const navigate = useNavigate();
    const { user, setUser } = useAuth();
    
    const [formData, setFormData] = useState({
        username: '',
        surname: '',
        phoneNumber: ''
    });
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        if (user) {
            setFormData({
                username: user.username || '',
                surname: user.surname || '',
                phoneNumber: user.phoneNumber || ''
            });
        }
    }, [user]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        try {
            const response = await authService.updateProfile(formData);
            
            // Aktualizuj dane użytkownika w kontekście
            if (setUser && response.data) {
                setUser(response.data);
                localStorage.setItem('user', JSON.stringify(response.data));
            }
            
            setSuccess('Profil został zaktualizowany pomyślnie!');
        } catch (err) {
            setError(err.response?.data?.message || 'Nie udało się zaktualizować profilu');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="profile-page">
            <div className="profile-container">
                <div className="profile-header">
                    <div className="profile-avatar">👤</div>
                    <h1>Mój profil</h1>
                    <p className="profile-email">{user?.email}</p>
                </div>

                <form onSubmit={handleSubmit} className="profile-form">
                    {success && <div className="success-message">{success}</div>}
                    {error && <div className="error-message">{error}</div>}

                    <div className="form-group">
                        <label htmlFor="username">Imię</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                            minLength={2}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="surname">Nazwisko</label>
                        <input
                            type="text"
                            id="surname"
                            name="surname"
                            value={formData.surname}
                            onChange={handleChange}
                            required
                            minLength={2}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="phoneNumber">Numer telefonu</label>
                        <input
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group readonly">
                        <label>Email</label>
                        <input
                            type="email"
                            value={user?.email || ''}
                            disabled
                        />
                        <span className="readonly-note">Email nie może być zmieniony</span>
                    </div>

                    <div className="profile-actions">
                        <button 
                            type="submit" 
                            className="btn-save"
                            disabled={loading}
                        >
                            {loading ? 'Zapisywanie...' : 'Zapisz zmiany'}
                        </button>
                        <button 
                            type="button" 
                            className="btn-cancel"
                            onClick={() => navigate(-1)}
                        >
                            Anuluj
                        </button>
                    </div>
                </form>

                <div className="profile-info">
                    <h3>Twoje role</h3>
                    <div className="roles-list">
                        {user?.roles?.map((role, index) => (
                            <span key={index} className={`role-badge ${role.toLowerCase()}`}>
                                {role === 'ADMIN' ? '👑 Administrator' : '👤 Użytkownik'}
                            </span>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;
