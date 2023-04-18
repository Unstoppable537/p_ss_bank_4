package com.bank.account.Controller;

import com.bank.account.Entity.AccountDetails;
import com.bank.account.Entity.Dto.AccountDetailsDto;
import com.bank.account.Service.AccountDetailsService;
import com.bank.account.Mapper.AccountDetailsMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с сущностями AccountDetails через API REST.
 */
@RequestMapping("/accounts")
@RestController
public class AccountDetailsControllerImpl implements AccountDetailsController {

    private final AccountDetailsService accountDetailsService;
    private final AccountDetailsMapper accountDetailsMapper;

    private static final Logger logger = LoggerFactory.getLogger(AccountDetailsControllerImpl.class);

    /**
     * Создает новый экземпляр контроллера для работы с сущностями AccountDetails через API REST.
     *
     * @param accountDetailsService объект сервиса для работы с сущностями AccountDetails
     * @param accountDetailsMapper  объект модуля конвертирования сущностей AccountDetails и AccountDetailsDto
     */
    @Autowired
    public AccountDetailsControllerImpl(AccountDetailsService accountDetailsService, AccountDetailsMapper accountDetailsMapper) {
        this.accountDetailsService = accountDetailsService;
        this.accountDetailsMapper = accountDetailsMapper;
    }

    /**
     * Получает информацию о счете по его номеру.
     *
     * @param id аккаунта
     * @return DTO AccountDetailsDto
     */
    @Override
    public AccountDetailsDto getAccountDetailsById(Long id) {
        logger.info("Запрос информации о счете с номером: " + id);
        AccountDetails accountDetails = accountDetailsService.findById(id);
        if (accountDetails == null) {
            String message = String.format("Аккаунт с id %s не существует", id);
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
        return accountDetailsMapper.convertToDto(accountDetails);
    }

    /**
     * Создает новый счет.
     *
     * @param accountDetailsDto DTO AccountDetailsDto
     */
    @Override
    public void createAccount(AccountDetailsDto accountDetailsDto) {
        logger.info("Создание нового счета");
        AccountDetails accountDetails = accountDetailsMapper.convertToEntity(accountDetailsDto);
        accountDetailsService.createAccount(accountDetails);
    }

    /**
     * Обновляет информацию о счете
     *
     * @param accountDetailsDto DTO AccountDetailsDto
     */
    @Override
    public void updateAccount(Long id, @RequestBody AccountDetailsDto accountDetailsDto) {
        logger.info("Обновление счета с id: " + id);
        AccountDetails accountDetails = accountDetailsMapper.convertToEntity(accountDetailsDto);
        accountDetails.setId(id);
        accountDetailsService.updateAccount(accountDetails);
    }

    /**
     * Удаляет счет по его id.
     *
     * @param id id счета
     */
    @Override
    public void deleteAccountById(Long id) {
        logger.info("Удаление счета с id: " + id);
        accountDetailsService.deleteAccountById(id);
    }

    /**
     * Получает список всех счетов.
     *
     * @return множество DTO AccountDetailsDto
     */
    @Override
    public Set<AccountDetailsDto> getAllAccounts() {
        logger.info("Запрос всех счетов");
        Set<AccountDetails> accountDetails = accountDetailsService.getAllAccounts();
        return accountDetails.stream()
                .map(accountDetailsMapper::convertToDto)
                .collect(Collectors.toSet());
    }
}