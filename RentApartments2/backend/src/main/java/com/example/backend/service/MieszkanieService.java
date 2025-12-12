package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.AdresDTO;
import com.example.backend.dto.MieszkanieDTO;
import com.example.backend.dto.MieszkanieRequest;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.Adres;
import com.example.backend.model.Mieszkanie;
import com.example.backend.model.User;
import com.example.backend.model.Zdjecie;
import com.example.backend.repository.AdresRepository;
import com.example.backend.repository.MieszkanieRepository;
import com.example.backend.repository.ZdjecieRepository;

@Service
public class MieszkanieService {

    @Autowired
    private MieszkanieRepository mieszkanieRepository;

    @Autowired
    private AdresRepository adresRepository;

    @Autowired
    private ZdjecieRepository zdjecieRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    public List<MieszkanieDTO> getAllApproved() {
        return mieszkanieRepository.findAllApproved().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MieszkanieDTO> getAllPending() {
        return mieszkanieRepository.findAllPending().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MieszkanieDTO> getAll() {
        return mieszkanieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MieszkanieDTO getById(Long id) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie znalezione"));
        return convertToDTO(mieszkanie);
    }

    public List<MieszkanieDTO> getMyMieszkania() {
        User currentUser = userService.getCurrentUser();
        return mieszkanieRepository.findByOwner(currentUser).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MieszkanieDTO> search(String query) {
        return mieszkanieRepository.search(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MieszkanieDTO> filter(Double minPrice, Double maxPrice, String miasto, Integer liczbaPokoi) {
        List<Mieszkanie> mieszkania = mieszkanieRepository.findAllApproved();
        
        return mieszkania.stream()
                .filter(m -> minPrice == null || m.getCena_miesieczna() >= minPrice)
                .filter(m -> maxPrice == null || m.getCena_miesieczna() <= maxPrice)
                .filter(m -> miasto == null || miasto.isEmpty() || 
                        m.getAdres().getMiasto().toLowerCase().contains(miasto.toLowerCase()))
                .filter(m -> liczbaPokoi == null || liczbaPokoi == 0 || 
                        (liczbaPokoi >= 4 ? m.getLiczba_pokoi() >= 4 : m.getLiczba_pokoi().equals(liczbaPokoi)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MieszkanieDTO create(MieszkanieRequest request) {
        User currentUser = userService.getCurrentUser();

        // Walidacja limitu zdjęć (max 10)
        if (request.getZdjeciaUrls() != null && request.getZdjeciaUrls().size() > 10) {
            throw new RuntimeException("Maksymalna liczba zdjęć to 10");
        }

        // Utwórz lub znajdź adres
        Adres adres = new Adres();
        adres.setUlica(request.getUlica());
        adres.setNumer(request.getNumer());
        adres.setKodPocztowy(request.getKodPocztowy());
        adres.setMiasto(request.getMiasto());
        adres = adresRepository.save(adres);

        // Utwórz mieszkanie
        Mieszkanie mieszkanie = new Mieszkanie();
        mieszkanie.setTytul(request.getTytul());
        mieszkanie.setOpis(request.getOpis());
        mieszkanie.setCena_miesieczna(request.getCenaMiesieczna());
        mieszkanie.setPowierzchnia(request.getPowierzchnia());
        mieszkanie.setLiczba_pokoi(request.getLiczbaPokoi());
        mieszkanie.setStatus("PENDING");
        mieszkanie.setOwner(currentUser);
        mieszkanie.setAdres(adres);

        mieszkanie = mieszkanieRepository.save(mieszkanie);

        // Dodaj zdjęcia jeśli są
        if (request.getZdjeciaUrls() != null && !request.getZdjeciaUrls().isEmpty()) {
            for (String url : request.getZdjeciaUrls()) {
                Zdjecie zdjecie = new Zdjecie();
                zdjecie.setMieszkanieId(mieszkanie.getId());
                zdjecie.setUrl(url);
                zdjecieRepository.save(zdjecie);
            }
        }

        logService.log(currentUser.getId(), "CREATE_MIESZKANIE", 
                "Utworzono ogłoszenie: " + mieszkanie.getTytul());

        return convertToDTO(mieszkanie);
    }

    @Transactional
    public MieszkanieDTO update(Long id, MieszkanieRequest request) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie znalezione"));

        User currentUser = userService.getCurrentUser();

        // Sprawdź czy użytkownik jest właścicielem lub adminem
        boolean isOwner = mieszkanie.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(ur -> ur.getRole().getRolename().equals("ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Brak uprawnień do edycji tego ogłoszenia");
        }

        // Walidacja limitu zdjęć (max 10)
        if (request.getZdjeciaUrls() != null && request.getZdjeciaUrls().size() > 10) {
            throw new RuntimeException("Maksymalna liczba zdjęć to 10");
        }

        // Aktualizuj adres
        Adres adres = mieszkanie.getAdres();
        adres.setUlica(request.getUlica());
        adres.setNumer(request.getNumer());
        adres.setKodPocztowy(request.getKodPocztowy());
        adres.setMiasto(request.getMiasto());
        adresRepository.save(adres);

        // Aktualizuj mieszkanie
        mieszkanie.setTytul(request.getTytul());
        mieszkanie.setOpis(request.getOpis());
        mieszkanie.setCena_miesieczna(request.getCenaMiesieczna());
        mieszkanie.setPowierzchnia(request.getPowierzchnia());
        mieszkanie.setLiczba_pokoi(request.getLiczbaPokoi());
        
        // Po edycji status wraca do PENDING (chyba że admin edytuje)
        if (!isAdmin) {
            mieszkanie.setStatus("PENDING");
        }

        mieszkanie = mieszkanieRepository.save(mieszkanie);

        logService.log(currentUser.getId(), "UPDATE_MIESZKANIE", 
                "Zaktualizowano ogłoszenie: " + mieszkanie.getTytul());

        return convertToDTO(mieszkanie);
    }

    @Transactional
    public void delete(Long id) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie znalezione"));

        User currentUser = userService.getCurrentUser();

        // Sprawdź czy użytkownik jest właścicielem lub adminem
        boolean isOwner = mieszkanie.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(ur -> ur.getRole().getRolename().equals("ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Brak uprawnień do usunięcia tego ogłoszenia");
        }

        // Usuń zdjęcia z MongoDB
        zdjecieRepository.deleteByMieszkanieId(id);

        logService.log(currentUser.getId(), "DELETE_MIESZKANIE", 
                "Usunięto ogłoszenie: " + mieszkanie.getTytul());

        mieszkanieRepository.delete(mieszkanie);
    }

    @Transactional
    public MieszkanieDTO approve(Long id) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie znalezione"));

        mieszkanie.setStatus("APPROVED");
        mieszkanie = mieszkanieRepository.save(mieszkanie);

        logService.log(userService.getCurrentUserId(), "APPROVE_MIESZKANIE", 
                "Zatwierdzono ogłoszenie: " + mieszkanie.getTytul());

        return convertToDTO(mieszkanie);
    }

    @Transactional
    public MieszkanieDTO reject(Long id) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie znalezione"));

        mieszkanie.setStatus("REJECTED");
        mieszkanie = mieszkanieRepository.save(mieszkanie);

        logService.log(userService.getCurrentUserId(), "REJECT_MIESZKANIE", 
                "Odrzucono ogłoszenie: " + mieszkanie.getTytul());

        return convertToDTO(mieszkanie);
    }

    public MieszkanieDTO convertToDTO(Mieszkanie mieszkanie) {
        MieszkanieDTO dto = new MieszkanieDTO();
        dto.setId(mieszkanie.getId());
        dto.setTytul(mieszkanie.getTytul());
        dto.setOpis(mieszkanie.getOpis());
        dto.setCenaMiesieczna(mieszkanie.getCena_miesieczna());
        dto.setPowierzchnia(mieszkanie.getPowierzchnia());
        dto.setLiczbaPokoi(mieszkanie.getLiczba_pokoi());
        dto.setStatus(mieszkanie.getStatus());

        // Adres
        Adres adres = mieszkanie.getAdres();
        AdresDTO adresDTO = new AdresDTO(
                adres.getId(),
                adres.getUlica(),
                adres.getNumer(),
                adres.getKodPocztowy(),
                adres.getMiasto()
        );
        dto.setAdres(adresDTO);

        // Owner (uproszczony)
        User owner = mieszkanie.getOwner();
        UserDTO ownerDTO = new UserDTO();
        ownerDTO.setId(owner.getId());
        ownerDTO.setUsername(owner.getUsername());
        ownerDTO.setSurname(owner.getSurname());
        ownerDTO.setEmail(owner.getEmail());
        ownerDTO.setPhoneNumber(owner.getPhoneNumber());
        dto.setOwner(ownerDTO);

        // Zdjęcia z MongoDB
        List<Zdjecie> zdjecia = zdjecieRepository.findByMieszkanieId(mieszkanie.getId());
        List<String> zdjeciaUrls = zdjecia.stream()
                .map(Zdjecie::getUrl)
                .collect(Collectors.toList());
        dto.setZdjeciaUrls(zdjeciaUrls);

        return dto;
    }
}
