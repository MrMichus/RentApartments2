import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
    const { user, logout, isAdmin, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">
                    🏠 RentApartments
                </Link>

                <div className="navbar-menu">
                    <Link to="/" className="navbar-link">Strona główna</Link>
                    <Link to="/mieszkania" className="navbar-link">Mieszkania</Link>

                    {isAuthenticated() ? (
                        <>
                            <Link to="/moje-mieszkania" className="navbar-link">Moje ogłoszenia</Link>
                            <Link to="/dodaj-mieszkanie" className="navbar-link">Dodaj ogłoszenie</Link>
                            <Link to="/chat" className="navbar-link">Wiadomości</Link>
                            
                            {isAdmin() && (
                                <Link to="/admin" className="navbar-link admin-link">Panel Admina</Link>
                            )}

                            <div className="navbar-user">
                                <Link to="/profil" className="user-name">
                                    👤 {user?.username} {user?.surname}
                                </Link>
                                <button onClick={handleLogout} className="btn-logout">
                                    Wyloguj
                                </button>
                            </div>
                        </>
                    ) : (
                        <div className="navbar-auth">
                            <Link to="/login" className="btn-login">Zaloguj się</Link>
                            <Link to="/register" className="btn-register">Zarejestruj się</Link>
                        </div>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
