package br.com.evasion.src.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStudantRequest {

    private String userName;

    private String userEmail;

    private String idStudantRegistration;

    private Boolean studantPeriod;

    private Integer studantCRA;

    private Integer studantNumAge;

    private Boolean studantGender;

    private Integer studantNumDisciplines;

    private Boolean studantLiveAlone;

    private Boolean studantHasSon;

    private Boolean studantHaveJob;

    private Boolean studantWillEvade;

    private Boolean studantSample;
}
