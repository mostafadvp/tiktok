package ir.jamareh.tiktok_aa.model.request.job;

import lombok.Data;

import java.util.List;

@Data
public class JobAccountRequest {
    private Long jobId;
    private List<Long> accounts;
}
