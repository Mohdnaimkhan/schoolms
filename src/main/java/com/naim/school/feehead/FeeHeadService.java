package com.naim.school.feehead;

import com.naim.school.sms.BusinessException;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeHeadService {

    private final FeeHeadRepository repository;

    public List<FeeHead> getAllFeeHeads() {

        return repository.findAll();

    }

    public FeeHead getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Fee Head not found."));

    }

    @Transactional
    public void save(FeeHead feeHead) {

        if (feeHead.getId() == null) {

            if (repository.existsByName(feeHead.getName())) {

                throw new BusinessException("Fee Head already exists.");

            }

        } else {

            if (repository.existsByNameAndIdNot(feeHead.getName(), feeHead.getId())) {

                throw new BusinessException("Fee Head already exists.");

            }

        }

        repository.save(feeHead);

    }

    @Transactional
    public void changeStatus(Long id) {

        FeeHead feeHead = getById(id);

        feeHead.setActive(!feeHead.getActive());

        repository.save(feeHead);

    }

}
