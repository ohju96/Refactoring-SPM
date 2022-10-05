package project.SPM.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class PapagoSmsDto {

    private String phoneNm;

    @NotNull
    @Size(min = 1, max = 40, message = "40글자 이내로 작성해주세요.(SMS 기준 : 한글 40글자)")
    private String message;
}
