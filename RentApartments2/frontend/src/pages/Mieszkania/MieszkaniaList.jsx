import { useState, useEffect } from 'react';
import { mieszkaniaService } from '../../api/services';
import MieszkanieCard from '../../components/MieszkanieCard/MieszkanieCard';
import './Mieszkania.css';

const MieszkaniaList = () => {
    const [mieszkania, setMieszkania] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [filters, setFilters] = useState({
        miasto: '',
        cenaMin: '',
        cenaMax: '',
        liczbaPokoi: ''
    });

    useEffect(() => {
        fetchMieszkania();
    }, []);

    const fetchMieszkania = async () => {
        try {
            setLoading(true);
            const response = await mieszkaniaService.getAll();
            // Pokazuj tylko zatwierdzone mieszkania dla zwykłych użytkowników
            const approved = response.data.filter(m => m.status === 'APPROVED');
            setMieszkania(approved);
        } catch (err) {
            setError('Błąd podczas ładowania mieszkań');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleFilterChange = (e) => {
        setFilters({
            ...filters,
            [e.target.name]: e.target.value
        });
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            const response = await mieszkaniaService.search(filters);
            const approved = response.data.filter(m => m.status === 'APPROVED');
            setMieszkania(approved);
        } catch (err) {
            setError('Błąd podczas wyszukiwania');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const clearFilters = () => {
        setFilters({
            miasto: '',
            cenaMin: '',
            cenaMax: '',
            liczbaPokoi: ''
        });
        fetchMieszkania();
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Ładowanie mieszkań...</p>
            </div>
        );
    }

    return (
        <div className="mieszkania-page">
            <div className="page-header">
                <h1>Dostępne mieszkania</h1>
                <p>Znajdź swoje wymarzone mieszkanie</p>
            </div>

            <div className="filters-section">
                <form onSubmit={handleSearch} className="filters-form">
                    <div className="filter-group">
                        <label>Miasto</label>
                        <input
                            type="text"
                            name="miasto"
                            value={filters.miasto}
                            onChange={handleFilterChange}
                            placeholder="np. Warszawa"
                        />
                    </div>

                    <div className="filter-group">
                        <label>Cena od</label>
                        <input
                            type="number"
                            name="cenaMin"
                            value={filters.cenaMin}
                            onChange={handleFilterChange}
                            placeholder="Min PLN"
                        />
                    </div>

                    <div className="filter-group">
                        <label>Cena do</label>
                        <input
                            type="number"
                            name="cenaMax"
                            value={filters.cenaMax}
                            onChange={handleFilterChange}
                            placeholder="Max PLN"
                        />
                    </div>

                    <div className="filter-group">
                        <label>Pokoje</label>
                        <select name="liczbaPokoi" value={filters.liczbaPokoi} onChange={handleFilterChange}>
                            <option value="">Wszystkie</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4+</option>
                        </select>
                    </div>

                    <div className="filter-buttons">
                        <button type="submit" className="btn-search">
                            🔍 Szukaj
                        </button>
                        <button type="button" className="btn-clear" onClick={clearFilters}>
                            ✕ Wyczyść
                        </button>
                    </div>
                </form>
            </div>

            {error && <div className="error-message">{error}</div>}

            <div className="mieszkania-count">
                Znaleziono: <strong>{mieszkania.length}</strong> {mieszkania.length === 1 ? 'mieszkanie' : 'mieszkań'}
            </div>

            {mieszkania.length === 0 ? (
                <div className="no-results">
                    <span>🏠</span>
                    <h3>Brak mieszkań</h3>
                    <p>Nie znaleziono mieszkań spełniających kryteria wyszukiwania.</p>
                </div>
            ) : (
                <div className="mieszkania-grid">
                    {mieszkania.map(mieszkanie => (
                        <MieszkanieCard key={mieszkanie.id} mieszkanie={mieszkanie} />
                    ))}
                </div>
            )}
        </div>
    );
};

export default MieszkaniaList;
