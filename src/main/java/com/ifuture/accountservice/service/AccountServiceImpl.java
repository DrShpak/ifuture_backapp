package com.ifuture.accountservice.service;

import com.ifuture.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repo;

    public AccountServiceImpl(AccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public Long getAmount(Integer id) {
        var account = repo.findById(id).orElse(null);
        if (account != null)
            return account.getValue();
        else
            throw new EntityNotFoundException();
    }

    @Override
    @Transactional
    public void addAmount(Integer id, Long value) {
        var account = repo.findById(id).orElse(null);
        if (account != null) {
            if (account.getValue() + value < 0)
                throw new RuntimeException();
            else {
                var balance = account.getValue();
                account.setValue(balance + value);
                repo.save(account);
            }
        } else {
            throw new EntityNotFoundException();
        }
    }
}
