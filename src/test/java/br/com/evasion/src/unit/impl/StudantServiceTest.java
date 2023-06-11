package br.com.evasion.src.unit.impl;

import br.com.evasion.src.dto.StudantDTO;
import br.com.evasion.src.entity.StudantEntity;
import br.com.evasion.src.enums.ErrorsEnum;
import br.com.evasion.src.exception.GeneralException;
import br.com.evasion.src.repository.StudantEntityRepository;
import br.com.evasion.src.service.KNNService;
import br.com.evasion.src.service.StudantService;
import br.com.evasion.src.unit.config.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class StudantServiceTest {

    @InjectMocks
    StudantService studantService;

    @Mock
    StudantEntityRepository studantEntityRepository;

    @Mock
    KNNService knnService;
    @Test
    public void createOrUpdateNewStudantTest() throws Exception {
        doNothing().when(knnService).processKNNonUserCreated(any());
        when(studantEntityRepository.findByIdStudantRegistration(any())).thenReturn(Optional.empty());
        studantService.createOrUpdateStudant(createStudantDTO(null));
        verify(knnService, times(1)).processKNNonUserCreated(any());
        verify(studantEntityRepository, times(1)).findByIdStudantRegistration(any());
    }

    @Test
    public void createOrUpdateAlreadyStudantTest() throws Exception {
        doNothing().when(knnService).processKNNonUserCreated(any());
        when(studantEntityRepository.findByIdStudantRegistration(any())).thenReturn(createStudantEntity());
        studantService.createOrUpdateStudant(createStudantDTO(null));
        verify(knnService, times(1)).processKNNonUserCreated(any());
        verify(studantEntityRepository, times(1)).findByIdStudantRegistration(any());
    }

    @Test
    public void createOrUpdateNewStudantAndHasEvadeTest() throws Exception {
        doNothing().when(knnService).processKNNonUserCreated(any());
        when(studantEntityRepository.findByIdStudantRegistration(any())).thenReturn(Optional.empty());
        studantService.createOrUpdateStudant(createStudantDTO(Boolean.TRUE));
        verify(knnService, times(0)).processKNNonUserCreated(any());
        verify(studantEntityRepository, times(1)).findByIdStudantRegistration(any());
    }

    @Test
    public void createOrUpdateAlreadyStudantAndHasEvadeTest() throws Exception {
        doNothing().when(knnService).processKNNonUserCreated(any());
        when(studantEntityRepository.findByIdStudantRegistration(any())).thenReturn(createStudantEntity());
        studantService.createOrUpdateStudant(createStudantDTO(Boolean.TRUE));
        verify(knnService, times(0)).processKNNonUserCreated(any());
        verify(studantEntityRepository, times(1)).findByIdStudantRegistration(any());
    }

    @Test
    public void createOrUpdateThrowExceptionTest() throws Exception {
        doNothing().when(knnService).processKNNonUserCreated(any());
        when(studantEntityRepository.findByIdStudantRegistration(any())).thenThrow(RuntimeException.class);
        GeneralException e = assertThrows(GeneralException.class, () -> studantService.createOrUpdateStudant(createStudantDTO(Boolean.TRUE)));
        verify(knnService, times(0)).processKNNonUserCreated(any());
        verify(studantEntityRepository, times(1)).findByIdStudantRegistration(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.INTERNAL.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.INTERNAL.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.INTERNAL.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.INTERNAL.getHttpStatus());
    }

    @Test
    public void deleteUserTest() {
        when(studantEntityRepository.findByIdStudantRegistration(any())).thenReturn(createStudantEntity());
        doNothing().when(studantEntityRepository).delete(any());
        studantService.deleteStudant(any());
        verify(studantEntityRepository, times(1)).findByIdStudantRegistration(any());
        verify(studantEntityRepository, times(1)).delete(any());
    }

    @Test
    public void deleteUserNotFoundTest() {
        when(studantEntityRepository.findByIdStudantRegistration(any())).thenReturn(Optional.empty());
        doNothing().when(studantEntityRepository).delete(any());
        GeneralException e = assertThrows(GeneralException.class, () -> studantService.deleteStudant(any()));
        verify(studantEntityRepository, times(1)).findByIdStudantRegistration(any());
        verify(studantEntityRepository, times(0)).delete(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.STUDANT_NOT_EXIST.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.STUDANT_NOT_EXIST.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.STUDANT_NOT_EXIST.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.STUDANT_NOT_EXIST.getHttpStatus());
    }

    @Test
    public void findByStudanteWillEvadeTest() {
        List<StudantEntity> studantEntityList = new ArrayList<>();
        studantEntityList.add(createStudantEntity().get());
        when(studantEntityRepository
                .findByStudantWillEvadeAndIsStudantSampleIsNullOrIsStudantSampleIsFalse(any()))
                .thenReturn(studantEntityList);
        studantService.findByStudanteWillEvade();
        verify(studantEntityRepository, times(1))
                .findByStudantWillEvadeAndIsStudantSampleIsNullOrIsStudantSampleIsFalse(any());
    }

    @Test
    public void findByStudanteWillEvadeThrowExceptionTest() {
        when(studantEntityRepository
                .findByStudantWillEvadeAndIsStudantSampleIsNullOrIsStudantSampleIsFalse(any()))
                .thenThrow(RuntimeException.class);
        GeneralException e = assertThrows(GeneralException.class, () ->  studantService.findByStudanteWillEvade());
        verify(studantEntityRepository, times(1))
                .findByStudantWillEvadeAndIsStudantSampleIsNullOrIsStudantSampleIsFalse(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.INTERNAL.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.INTERNAL.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.INTERNAL.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.INTERNAL.getHttpStatus());
    }


    private Optional<StudantEntity> createStudantEntity() {
        return Optional.ofNullable(StudantEntity.builder()
                        .idStudantRegistration("123456")
                        .userName("Teste")
                        .userEmail("teste@teste")
                        .studantCRA(95)
                        .studantPeriod(Boolean.TRUE)
                        .studantGender(Boolean.TRUE)
                        .studantHaveJob(Boolean.TRUE)
                        .studantHasSon(Boolean.TRUE)
                        .studantLiveAlone(Boolean.TRUE)
                        .studantNumAge(33)
                        .studantNumDisciplines(5)
                        .studantWillEvade(Boolean.TRUE)
                        .build());
    }

    private StudantDTO createStudantDTO(Boolean evade) {
        return StudantDTO.builder()
                .idStudantRegistration("123456")
                .userName("Teste")
                .userEmail("teste@teste")
                .studantCRA(95)
                .studantPeriod(Boolean.TRUE)
                .studantGender(Boolean.TRUE)
                .studantHaveJob(Boolean.TRUE)
                .studantHasSon(Boolean.TRUE)
                .studantLiveAlone(Boolean.TRUE)
                .studantNumAge(33)
                .studantNumDisciplines(5)
                .studantWillEvade(evade)
                .idStudant(BigInteger.ONE)
                .build();
    }

}
