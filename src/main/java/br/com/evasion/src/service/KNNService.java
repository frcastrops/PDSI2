package br.com.evasion.src.service;

import br.com.evasion.src.dto.StudantDTO;
import br.com.evasion.src.entity.StudantEntity;
import br.com.evasion.src.enums.ErrorsEnum;
import br.com.evasion.src.exception.GeneralException;
import br.com.evasion.src.repository.StudantEntityRepository;
import br.com.evasion.src.utils.BuilderObjectsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import weka.Run;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KNNService {

        private final StudantEntityRepository studantEntityRepository;

        public static final Integer NUM_OF_NEIGHBORS = 2;

        public void processKNNonUserCreated (StudantDTO dto) throws Exception {
                try {
                        ArrayList<Attribute> attInfo = new ArrayList<>();
                        List<StudantEntity> listStudantEntity =
                                studantEntityRepository.findByIsStudantSample(Boolean.TRUE);
                        List<StudantDTO> listStudantDTO = new ArrayList<>();
                        listStudantEntity.forEach(entity -> {
                                listStudantDTO.add(BuilderObjectsUtils.buildStudantDTO(entity));
                        });
                        Instances data = new Instances("TestInstances", attInfo, 11);
                        //TODO o número 3 é o numero de vizinhos para dar match com o novo registro que esta entrando na tabela
                        Classifier ibk = new IBk();

                        validateSample(listStudantDTO);

                        Attribute studantePeriod = new Attribute("studantePeriod");
                        Attribute studanteCRA = new Attribute("studanteCRA");
                        Attribute studantNumAge = new Attribute("studantNumAge");
                        Attribute studantGender = new Attribute("studantGender");
                        Attribute studantNumDisciplines = new Attribute("studantNumDisciplines");
                        Attribute studantLiveAlone = new Attribute("studantLiveAlone");
                        Attribute studantHasSon = new Attribute("studantHasSon");
                        Attribute studantHaveJob = new Attribute("studantHaveJob");
                        Attribute studantWillEvade = new Attribute("studantWillEvade");

                        attInfo.add(studantePeriod);
                        attInfo.add(studanteCRA);
                        attInfo.add(studantNumAge);
                        attInfo.add(studantGender);
                        attInfo.add(studantNumDisciplines);
                        attInfo.add(studantLiveAlone);
                        attInfo.add(studantHasSon);
                        attInfo.add(studantHaveJob);
                        attInfo.add(studantWillEvade);
                        data.setClassIndex(data.numAttributes() - 1);

                        for (StudantDTO studantDTO : listStudantDTO) {
                                double[] studantValueAttributes = new double[data.numAttributes()];
                                studantValueAttributes[0] = studantDTO.getStudantPeriod() ? 1L : 0L;
                                studantValueAttributes[1] = studantDTO.getStudantCRA();
                                studantValueAttributes[2] = studantDTO.getStudantNumAge();
                                studantValueAttributes[3] = studantDTO.getStudantGender() ? 1L : 0L;
                                studantValueAttributes[4] = studantDTO.getStudantNumDisciplines();
                                studantValueAttributes[5] = studantDTO.getStudantLiveAlone() ? 1L : 0L;
                                studantValueAttributes[6] = studantDTO.getStudantHasSon() ? 1L : 0L;
                                studantValueAttributes[7] = studantDTO.getStudantHaveJob() ? 1L : 0L;
                                studantValueAttributes[8] = studantDTO.getStudantWillEvade() ? 1L : 0L;
                                data.add(new DenseInstance(1.0, studantValueAttributes));
                        }

                        double[] newStudantValueAttributes = new double[data.numAttributes()];
                        newStudantValueAttributes[0] = dto.getStudantPeriod() ? 1L : 0L;
                        newStudantValueAttributes[1] = dto.getStudantCRA();
                        newStudantValueAttributes[2] = dto.getStudantNumAge();
                        newStudantValueAttributes[3] = dto.getStudantGender() ? 1L : 0L;
                        newStudantValueAttributes[4] = dto.getStudantNumDisciplines();
                        newStudantValueAttributes[5] = dto.getStudantLiveAlone() ? 1L : 0L;
                        newStudantValueAttributes[6] = dto.getStudantHasSon() ? 1L : 0L;
                        newStudantValueAttributes[7] = dto.getStudantHaveJob() ? 1L : 0L;
                        data.add(new DenseInstance(1.0, newStudantValueAttributes));

                        Instance newStundantInstance = data.instance(data.numInstances()-1);
                        data.delete(data.numInstances()-1);
                        ibk.buildClassifier(data);
                        dto.setStudantWillEvade(ibk.classifyInstance(newStundantInstance) == 1L ? Boolean.TRUE : Boolean.FALSE);
                } catch (Exception e) {
                        throw e;
                }

        }

        private void validateSample (List<StudantDTO> listStudantDTO) {
                if (listStudantDTO.isEmpty() || listStudantDTO.size() < NUM_OF_NEIGHBORS) {
                        throw new GeneralException(ErrorsEnum.NOT_SAMPLE_OF_STUDANTS);
                }
        }
}
