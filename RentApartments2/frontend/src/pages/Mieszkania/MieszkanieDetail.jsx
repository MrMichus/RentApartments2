import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { mieszkaniaService } from '../../api/services';
import { useAuth } from '../../context/AuthContext';
import './MieszkanieDetail.css';

const MieszkanieDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user, isAuthenticated } = useAuth();
    
    const [mieszkanie, setMieszkanie] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [currentImageIndex, setCurrentImageIndex] = useState(0);

    useEffect(() => {
        fetchMieszkanie();
    }, [id]);

    const fetchMieszkanie = async () => {
        try {
            setLoading(true);
            const response = await mieszkaniaService.getById(id);
            setMieszkanie(response.data);
        } catch (err) {
            setError('Nie udało się załadować mieszkania');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const nextImage = () => {
        if (mieszkanie?.zdjeciaUrls?.length > 0) {
            setCurrentImageIndex((prev) => 
                (prev + 1) % mieszkanie.zdjeciaUrls.length
            );
        }
    };

    const prevImage = () => {
        if (mieszkanie?.zdjeciaUrls?.length > 0) {
            setCurrentImageIndex((prev) => 
                prev === 0 ? mieszkanie.zdjeciaUrls.length - 1 : prev - 1
            );
        }
    };

    const handleSendMessage = () => {
        // Przekieruj do chatu z właścicielem mieszkania
        navigate(`/chat/${mieszkanie.owner?.id}`);
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Ładowanie...</p>
            </div>
        );
    }

    if (!mieszkanie) {
        return (
            <div className="error-container">
                <h2>Mieszkanie nie znalezione</h2>
                <Link to="/mieszkania" className="btn-back">Wróć do listy</Link>
            </div>
        );
    }

    const hasImages = mieszkanie.zdjeciaUrls && mieszkanie.zdjeciaUrls.length > 0;

    return (
        <div className="mieszkanie-detail-page">
            <div className="detail-header">
                <Link to="/mieszkania" className="btn-back-link">
                    ← Wróć do listy
                </Link>
            </div>

            <div className="detail-container">
                {/* Galeria zdjęć */}
                <div className="detail-gallery">
                    {hasImages ? (
                        <div className="gallery-wrapper">
                            <div className="gallery-main">
                                <img 
                                    src={mieszkanie.zdjeciaUrls[currentImageIndex]} 
                                    alt={`${mieszkanie.tytul} - zdjęcie ${currentImageIndex + 1}`} 
                                />
                                
                                {mieszkanie.zdjeciaUrls.length > 1 && (
                                    <>
                                        <button className="gallery-btn prev" onClick={prevImage}>
                                            ‹
                                        </button>
                                        <button className="gallery-btn next" onClick={nextImage}>
                                            ›
                                        </button>
                                        <div className="gallery-counter">
                                            {currentImageIndex + 1} / {mieszkanie.zdjeciaUrls.length}
                                        </div>
                                    </>
                                )}
                            </div>
                            
                            {mieszkanie.zdjeciaUrls.length > 1 && (
                                <div className="gallery-thumbnails">
                                    {mieszkanie.zdjeciaUrls.map((url, index) => (
                                        <div 
                                            key={index}
                                            className={`thumbnail ${index === currentImageIndex ? 'active' : ''}`}
                                            onClick={() => setCurrentImageIndex(index)}
                                        >
                                            <img src={url} alt={`Miniatura ${index + 1}`} />
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    ) : (
                        <div className="no-image-large">
                            <span>🏠</span>
                            <p>Brak zdjęć</p>
                        </div>
                    )}
                </div>

                <div className="detail-info">
                    <div className="detail-main">
                        <h1>{mieszkanie.tytul}</h1>
                        
                        <div className="detail-location">
                            📍 {mieszkanie.adres?.ulica} {mieszkanie.adres?.numer}, 
                            {' '}{mieszkanie.adres?.kodPocztowy} {mieszkanie.adres?.miasto}
                        </div>

                        <div className="detail-price">
                            <span className="price-value">{mieszkanie.cenaMiesieczna?.toLocaleString()} zł</span>
                            <span className="price-period">/ miesiąc</span>
                        </div>

                        <div className="detail-specs">
                            <div className="spec">
                                <span className="spec-icon">📐</span>
                                <span className="spec-label">Powierzchnia</span>
                                <span className="spec-value">{mieszkanie.powierzchnia} m²</span>
                            </div>
                            <div className="spec">
                                <span className="spec-icon">🚪</span>
                                <span className="spec-label">Pokoje</span>
                                <span className="spec-value">{mieszkanie.liczbaPokoi}</span>
                            </div>
                        </div>

                        <div className="detail-description">
                            <h3>Opis</h3>
                            <p>{mieszkanie.opis}</p>
                        </div>
                    </div>

                    {/* Sidebar z danymi właściciela */}
                    <div className="detail-sidebar">
                        {error && <div className="error-message">{error}</div>}

                        <div className="owner-card">
                            <h3>Właściciel</h3>
                            <div className="owner-info">
                                <span className="owner-avatar">👤</span>
                                <div className="owner-details">
                                    <p className="owner-name">
                                        {mieszkanie.owner?.username} {mieszkanie.owner?.surname}
                                    </p>
                                    <p className="owner-email">
                                        ✉️ {mieszkanie.owner?.email}
                                    </p>
                                    {mieszkanie.owner?.phoneNumber && (
                                        <p className="owner-phone">
                                            📞 {mieszkanie.owner?.phoneNumber}
                                        </p>
                                    )}
                                </div>
                            </div>

                            {isAuthenticated() && mieszkanie.owner?.id !== user?.id && (
                                <button 
                                    className="btn-send-message"
                                    onClick={handleSendMessage}
                                >
                                    💬 Wyślij wiadomość
                                </button>
                            )}

                            {!isAuthenticated() && (
                                <div className="auth-prompt">
                                    <p>Zaloguj się, aby skontaktować się z właścicielem</p>
                                    <Link to="/login" className="btn-login-prompt">Zaloguj się</Link>
                                </div>
                            )}
                        </div>

                        {/* Adres */}
                        <div className="address-card">
                            <h3>Adres</h3>
                            <p className="address-line">
                                {mieszkanie.adres?.ulica} {mieszkanie.adres?.numer}
                            </p>
                            <p className="address-line">
                                {mieszkanie.adres?.kodPocztowy} {mieszkanie.adres?.miasto}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MieszkanieDetail;
