package br.com.evasion.src.unit.impl;

import br.com.evasion.src.unit.config.TestConfiguration;
import br.com.evasion.src.dto.StudantDTO;
import br.com.evasion.src.entity.StudantEntity;
import br.com.evasion.src.enums.ErrorsEnum;
import br.com.evasion.src.exception.GeneralException;
import br.com.evasion.src.repository.StudantEntityRepository;
import br.com.evasion.src.service.KNNService;
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
public class KNNServiceTest {

    @InjectMocks
    KNNService KNNService;

    @Mock
    StudantEntityRepository studantEntityRepository;

    @Test
    public void processKNNonUserCreatedTest() throws Exception {
        List<StudantEntity> studantEntityList = new ArrayList<>();
        studantEntityList.add(createStudantEntity().get());
        studantEntityList.add(createStudantEntity().get());
        when(studantEntityRepository.findByIsStudantSample(any())).thenReturn(studantEntityList);
        KNNService.processKNNonUserCreated(createStudantDTO(null));
        verify(studantEntityRepository, times(1)).findByIsStudantSample(any());
    }

    @Test
    public void processKNNonUserCreatedSampleSmallTest() throws Exception {
        List<StudantEntity> studantEntityList = new ArrayList<>();
        studantEntityList.add(createStudantEntity().get());
        when(studantEntityRepository.findByIsStudantSample(any())).thenReturn(studantEntityList);
        GeneralException e = assertThrows(GeneralException.class,
                () -> KNNService.processKNNonUserCreated(createStudantDTO(null)));
        verify(studantEntityRepository, times(1)).findByIsStudantSample(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getHttpStatus());
    }

    @Test
    public void processKNNonUserCreatedMissingSampleTest() {
        List<StudantEntity> studantEntityList = new ArrayList<>();
        when(studantEntityRepository.findByIsStudantSample(any())).thenReturn(studantEntityList);
        GeneralException e = assertThrows(GeneralException.class,
                () -> KNNService.processKNNonUserCreated(createStudantDTO(null)));
        verify(studantEntityRepository, times(1)).findByIsStudantSample(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.NOT_SAMPLE_OF_STUDANTS.getHttpStatus());
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
