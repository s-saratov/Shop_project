package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.ConfirmationCode;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.ConfirmationCodeRepository;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository repository;

    public ConfirmationServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public String generateConfirmationCode(User user) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusMinutes(5);
        ConfirmationCode codeEntity = new ConfirmationCode(code, expired, user);
        repository.save(codeEntity);
        return code;
    }

    @Override
    public void confirmRegistration(String code) {
        ConfirmationCode confirmationCode = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Confirmation code not found"));

        if (confirmationCode.getExpired().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Confirmation code has expired");
        }

        User user = confirmationCode.getUser();
        user.setActive(true);

        repository.delete(confirmationCode); // Удаляем код из базы
    }



//    public static void main(String[] args) {
//        for (int i = 0; i < 5; i++) {
//            System.out.println(UUID.randomUUID());
//        }
//    }

}
