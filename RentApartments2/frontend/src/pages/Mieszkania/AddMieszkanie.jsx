import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { mieszkaniaService } from "../../api/services";
import "./AddMieszkanie.css";

const AddMieszkanie = ({ editMode = false }) => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [loading, setLoading] = useState(false);
  const [fetchingData, setFetchingData] = useState(editMode);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [formData, setFormData] = useState({
    tytul: "",
    opis: "",
    cenaMiesieczna: "",
    powierzchnia: "",
    liczbaPokoi: "",
    ulica: "",
    numer: "",
    numerMieszkania: "",
    kodPocztowy: "",
    miasto: "",
    zdjeciaUrls: [""],
  });

  useEffect(() => {
    if (editMode && id) {
      fetchMieszkanie();
    }
  }, [editMode, id]);

  const fetchMieszkanie = async () => {
    try {
      setFetchingData(true);
      const response = await mieszkaniaService.getById(id);
      const m = response.data;

      setFormData({
        tytul: m.tytul || "",
        opis: m.opis || "",
        cenaMiesieczna: m.cenaMiesieczna?.toString() || "",
        powierzchnia: m.powierzchnia?.toString() || "",
        liczbaPokoi: m.liczbaPokoi?.toString() || "",
        ulica: m.adres?.ulica || "",
        numer: m.adres?.numer || "",
        numerMieszkania: m.adres?.numerMieszkania || "",
        kodPocztowy: m.adres?.kodPocztowy || "",
        miasto: m.adres?.miasto || "",
        zdjeciaUrls: m.zdjeciaUrls?.length > 0 ? m.zdjeciaUrls : [""],
      });
    } catch (err) {
      setError("Nie udało się załadować danych mieszkania");
      console.error(err);
    } finally {
      setFetchingData(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleZdjecieChange = (index, value) => {
    const newZdjecia = [...formData.zdjeciaUrls];
    newZdjecia[index] = value;
    setFormData({ ...formData, zdjeciaUrls: newZdjecia });
  };

  const addZdjecieField = () => {
    if (formData.zdjeciaUrls.length >= 10) {
      setError("Maksymalna liczba zdjęć to 10");
      return;
    }
    setFormData({
      ...formData,
      zdjeciaUrls: [...formData.zdjeciaUrls, ""],
    });
  };

  const removeZdjecieField = (index) => {
    const newZdjecia = formData.zdjeciaUrls.filter((_, i) => i !== index);
    setFormData({
      ...formData,
      zdjeciaUrls: newZdjecia.length ? newZdjecia : [""],
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      // Filtruj puste URL-e zdjęć
      const filteredZdjecia = formData.zdjeciaUrls.filter(
        (url) => url.trim() !== ""
      );

      // Walidacja limitu zdjęć
      if (filteredZdjecia.length > 10) {
        setError("Maksymalna liczba zdjęć to 10");
        setLoading(false);
        return;
      }

      const mieszkanieData = {
        tytul: formData.tytul,
        opis: formData.opis,
        cenaMiesieczna: parseFloat(formData.cenaMiesieczna),
        powierzchnia: parseFloat(formData.powierzchnia),
        liczbaPokoi: parseInt(formData.liczbaPokoi),
        ulica: formData.ulica,
        numer: formData.numer,
        kodPocztowy: formData.kodPocztowy,
        miasto: formData.miasto,
        zdjeciaUrls: filteredZdjecia.length > 0 ? filteredZdjecia : null,
      };

      if (editMode && id) {
        await mieszkaniaService.update(id, mieszkanieData);
        setSuccess(
          "Ogłoszenie zostało zaktualizowane i czeka na ponowne zatwierdzenie przez administratora!"
        );
      } else {
        await mieszkaniaService.create(mieszkanieData);
        setSuccess(
          "Ogłoszenie zostało dodane i czeka na zatwierdzenie przez administratora!"
        );
      }
      setTimeout(() => navigate("/moje-mieszkania"), 2000);
    } catch (err) {
      setError(
        err.response?.data?.message || "Błąd podczas zapisywania ogłoszenia"
      );
    } finally {
      setLoading(false);
    }
  };

  if (fetchingData) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Ładowanie danych...</p>
      </div>
    );
  }

  return (
    <div className="add-mieszkanie-page">
      <div className="form-container">
        <div className="form-header">
          <h1>{editMode ? "Edytuj ogłoszenie" : "Dodaj nowe ogłoszenie"}</h1>
          <p>
            {editMode
              ? "Zaktualizuj dane swojego mieszkania"
              : "Wypełnij formularz, aby dodać mieszkanie do wynajęcia"}
          </p>
        </div>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        <form onSubmit={handleSubmit} className="mieszkanie-form">
          <div className="form-section">
            <h3>📝 Informacje podstawowe</h3>

            <div className="form-group">
              <label htmlFor="tytul">Tytuł ogłoszenia *</label>
              <input
                type="text"
                id="tytul"
                name="tytul"
                value={formData.tytul}
                onChange={handleChange}
                placeholder="np. Przytulne mieszkanie w centrum"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="opis">Opis *</label>
              <textarea
                id="opis"
                name="opis"
                value={formData.opis}
                onChange={handleChange}
                placeholder="Opisz mieszkanie, jego zalety i wyposażenie..."
                rows={5}
                required
              />
            </div>

            <div className="form-row-3">
              <div className="form-group">
                <label htmlFor="cenaMiesieczna">Cena miesięczna (PLN) *</label>
                <input
                  type="number"
                  id="cenaMiesieczna"
                  name="cenaMiesieczna"
                  value={formData.cenaMiesieczna}
                  onChange={handleChange}
                  placeholder="2500"
                  min="0"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="powierzchnia">Powierzchnia (m²) *</label>
                <input
                  type="number"
                  id="powierzchnia"
                  name="powierzchnia"
                  value={formData.powierzchnia}
                  onChange={handleChange}
                  placeholder="50"
                  min="1"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="liczbaPokoi">Liczba pokoi *</label>
                <select
                  id="liczbaPokoi"
                  name="liczbaPokoi"
                  value={formData.liczbaPokoi}
                  onChange={handleChange}
                  required
                >
                  <option value="">Wybierz</option>
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4</option>
                  <option value="5">5+</option>
                </select>
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>📍 Adres</h3>

            <div className="form-row-2">
              <div className="form-group">
                <label htmlFor="ulica">Ulica *</label>
                <input
                  type="text"
                  id="ulica"
                  name="ulica"
                  value={formData.ulica}
                  onChange={handleChange}
                  placeholder="ul. Przykładowa"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="numer">Numer budynku *</label>
                <input
                  type="text"
                  id="numer"
                  name="numer"
                  value={formData.numer}
                  onChange={handleChange}
                  placeholder="10"
                  required
                />
              </div>
            </div>

            <div className="form-row-3">
              <div className="form-group">
                <label htmlFor="numerMieszkania">Nr mieszkania</label>
                <input
                  type="text"
                  id="numerMieszkania"
                  name="numerMieszkania"
                  value={formData.numerMieszkania}
                  onChange={handleChange}
                  placeholder="5"
                />
              </div>

              <div className="form-group">
                <label htmlFor="kodPocztowy">Kod pocztowy *</label>
                <input
                  type="text"
                  id="kodPocztowy"
                  name="kodPocztowy"
                  value={formData.kodPocztowy}
                  onChange={handleChange}
                  placeholder="00-000"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="miasto">Miasto *</label>
                <input
                  type="text"
                  id="miasto"
                  name="miasto"
                  value={formData.miasto}
                  onChange={handleChange}
                  placeholder="Warszawa"
                  required
                />
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>📷 Zdjęcia</h3>
            <p className="section-description">
              Dodaj linki do zdjęć mieszkania (opcjonalne)
            </p>

            {formData.zdjeciaUrls.map((url, index) => (
              <div key={index} className="zdjecie-input-row">
                <div className="form-group" style={{ flex: 1 }}>
                  <input
                    type="url"
                    value={url}
                    onChange={(e) => handleZdjecieChange(index, e.target.value)}
                    placeholder="https://example.com/zdjecie.jpg"
                  />
                </div>
                <button
                  type="button"
                  className="btn-remove-zdjecie"
                  onClick={() => removeZdjecieField(index)}
                  title="Usuń zdjęcie"
                >
                  ✕
                </button>
              </div>
            ))}

            <button
              type="button"
              className="btn-add-zdjecie"
              onClick={addZdjecieField}
            >
              + Dodaj kolejne zdjęcie
            </button>
          </div>

          {!editMode && (
            <div className="form-info">
              <span>ℹ️</span>
              <p>
                Po dodaniu ogłoszenie będzie czekać na zatwierdzenie przez
                administratora.
              </p>
            </div>
          )}

          <div className="form-actions">
            <button
              type="button"
              className="btn-cancel"
              onClick={() => navigate(-1)}
            >
              Anuluj
            </button>
            <button type="submit" className="btn-submit" disabled={loading}>
              {loading
                ? "Zapisywanie..."
                : editMode
                ? "Zapisz zmiany"
                : "Dodaj ogłoszenie"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddMieszkanie;
