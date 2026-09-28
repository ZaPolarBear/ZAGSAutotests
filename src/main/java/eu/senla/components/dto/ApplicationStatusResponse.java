package eu.senla.components.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationStatusResponse {

    private String requestId;
    private ApplicationStatusResponseData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApplicationStatusResponseData {
        private String dateofapplication;
        private String kindofapplication;
        private String statusofapplication;
    }
}