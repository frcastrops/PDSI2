package br.com.evasion.src.service;

import br.com.evasion.src.dto.StudantDTO;
import br.com.evasion.src.entity.StudantEntity;
import br.com.evasion.src.enums.ErrorsEnum;
import br.com.evasion.src.exception.GeneralException;
import br.com.evasion.src.repository.StudantEntityRepository;
import br.com.evasion.src.response.FindStudantReportResponse;
import br.com.evasion.src.response.FindUserResponse;
import br.com.evasion.src.utils.BuilderObjectsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudantService {

    private final KNNService knnService;

    private final StudantEntityRepository studantEntityRepository;
    public void createOrUpdateStudant (StudantDTO dto) {
        try {
            processKNNonUserCreated(dto);
            Optional<StudantEntity> studantEntity =
                    studantEntityRepository.findByIdStudantRegistration(dto.getIdStudantRegistration());

            if (studantEntity.isEmpty()) {
                studantEntityRepository.save(BuilderObjectsUtils.buildStudantEntity(dto));
            } else {
                studantEntity.get().setUserName(dto.getUserName());
                studantEntity.get().setUserEmail(dto.getUserEmail());
                studantEntity.get().setStudantCRA(dto.getStudantCRA());
                studantEntity.get().setStudantPeriod(dto.getStudantPeriod());
                studantEntity.get().setStudantGender(dto.getStudantGender());
                studantEntity.get().setStudantHaveJob(dto.getStudantHaveJob());
                studantEntity.get().setStudantHasSon(dto.getStudantHasSon());
                studantEntity.get().setStudantLiveAlone(dto.getStudantLiveAlone());
                studantEntity.get().setStudantNumAge(dto.getStudantNumAge());
                studantEntity.get().setStudantNumDisciplines(dto.getStudantNumDisciplines());
                studantEntity.get().setStudantWillEvade(dto.getStudantWillEvade());
                studantEntityRepository.save(studantEntity.get());
            }
        } catch (Exception e){
            throw new GeneralException(ErrorsEnum.INTERNAL);
        }

    }

    public void deleteStudant (String idStudantRegistration) {
        Optional<StudantEntity> studantEntity =
                studantEntityRepository.findByIdStudantRegistration(idStudantRegistration);
        if (studantEntity.isEmpty()) {
            throw new GeneralException(ErrorsEnum.STUDANT_NOT_EXIST);
        }
        studantEntityRepository
                .delete(studantEntity.get());
    }

    private void processKNNonUserCreated (StudantDTO dto) throws Exception{
        if(dto.getStudantWillEvade() == null) {
            knnService.processKNNonUserCreated(dto);
        }
    }

    public List<FindStudantReportResponse> findByStudanteWillEvade () {
        try {
            return
            BuilderObjectsUtils
                    .buildFindStudantReportResponseList(
            BuilderObjectsUtils
                    .buildStudantDTOList(studantEntityRepository
                            .findByStudantWillEvadeAndIsStudantSampleIsNullOrIsStudantSampleIsFalse(Boolean.TRUE)));
        } catch (RuntimeException e){
            throw new GeneralException(ErrorsEnum.INTERNAL);
        }
    }

}
