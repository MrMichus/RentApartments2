import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Home.css';

const Home = () => {
    const { isAuthenticated } = useAuth();

    return (
        <div className="home">
            <section className="hero">
                <div className="hero-content">
                    <h1>Znajdź wymarzone mieszkanie</h1>
                    <p>Przeglądaj setki ofert wynajmu mieszkań w całej Polsce. 
                       Szybko, łatwo i bezpiecznie.</p>
                    <div className="hero-buttons">
                        <Link to="/mieszkania" className="btn-primary">
                            Przeglądaj oferty
                        </Link>
                        {!isAuthenticated() && (
                            <Link to="/register" className="btn-secondary">
                                Dołącz do nas
                            </Link>
                        )}
                    </div>
                </div>
                <div className="hero-image">
                    <div className="hero-shape"></div>
                </div>
            </section>

            <section className="features">
                <h2>Dlaczego warto wybrać RentApartments?</h2>
                <div className="features-grid">
                    <div className="feature-card">
                        <div className="feature-icon">🔍</div>
                        <h3>Łatwe wyszukiwanie</h3>
                        <p>Filtruj oferty według ceny, lokalizacji i liczby pokoi.</p>
                    </div>
                    <div className="feature-card">
                        <div className="feature-icon">💬</div>
                        <h3>Bezpośredni kontakt</h3>
                        <p>Rozmawiaj z właścicielami przez wbudowany czat.</p>
                    </div>
                    <div className="feature-card">
                        <div className="feature-icon">📅</div>
                        <h3>Rezerwacje online</h3>
                        <p>Zarezerwuj mieszkanie w kilka kliknięć.</p>
                    </div>
                    <div className="feature-card">
                        <div className="feature-icon">✅</div>
                        <h3>Zweryfikowane oferty</h3>
                        <p>Każde ogłoszenie jest sprawdzane przez administratora.</p>
                    </div>
                </div>
            </section>

            <section className="cta">
                <h2>Masz mieszkanie do wynajęcia?</h2>
                <p>Dodaj swoje ogłoszenie i znajdź najemcę już dziś!</p>
                {isAuthenticated() ? (
                    <Link to="/dodaj-mieszkanie" className="btn-cta">
                        Dodaj ogłoszenie
                    </Link>
                ) : (
                    <Link to="/register" className="btn-cta">
                        Zarejestruj się i dodaj
                    </Link>
                )}
            </section>
        </div>
    );
};

export default Home;
