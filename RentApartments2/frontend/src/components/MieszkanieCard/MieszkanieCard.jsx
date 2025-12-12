import './MieszkanieCard.css';
import { Link } from 'react-router-dom';

const MieszkanieCard = ({ mieszkanie }) => {
    const getStatusBadge = (status) => {
        switch (status) {
            case 'APPROVED':
                return <span className="status-badge approved">Zatwierdzone</span>;
            case 'PENDING':
                return <span className="status-badge pending">Oczekujące</span>;
            case 'REJECTED':
                return <span className="status-badge rejected">Odrzucone</span>;
            default:
                return null;
        }
    };

    return (
        <div className="mieszkanie-card">
            <div className="card-image">
                {mieszkanie.zdjeciaUrls && mieszkanie.zdjeciaUrls.length > 0 ? (
                    <img src={mieszkanie.zdjeciaUrls[0]} alt={mieszkanie.tytul} />
                ) : (
                    <div className="no-image">
                        <span>🏠</span>
                        <p>Brak zdjęcia</p>
                    </div>
                )}
                {getStatusBadge(mieszkanie.status)}
            </div>
            
            <div className="card-content">
                <h3 className="card-title">{mieszkanie.tytul}</h3>
                
                <div className="card-location">
                    📍 {mieszkanie.adres?.miasto}, {mieszkanie.adres?.ulica} {mieszkanie.adres?.numer}
                </div>
                
                <div className="card-details">
                    <span className="detail">
                        📐 {mieszkanie.powierzchnia} m²
                    </span>
                    <span className="detail">
                        🚪 {mieszkanie.liczbaPokoi} {mieszkanie.liczbaPokoi === 1 ? 'pokój' : 'pokoje'}
                    </span>
                </div>
                
                <div className="card-footer">
                    <div className="price">
                        <span className="price-value">{mieszkanie.cenaMiesieczna?.toLocaleString()} zł</span>
                        <span className="price-period">/ miesiąc</span>
                    </div>
                    
                    <Link to={`/mieszkania/${mieszkanie.id}`} className="btn-view">
                        Zobacz więcej
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default MieszkanieCard;
