package eu.senla.components.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private String requestId;
    private UserResponseData data;

    @Data
    public static class UserResponseData {
        @JsonProperty("applicantid")
        private Long applicantId;
        @JsonProperty("applicationid")
        private Long applicationId;
        @JsonProperty("citizenid")
        private Long citizenId;
        @JsonProperty("merrigecertificateid")
        private Long marriageCertificateId;

        public UserResponseData() {
        }
    }
}