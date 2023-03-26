package Upbeat;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConfigMessage
{
    private long m;
    private long n;
    private long init_plan_min;
    private long init_plan_sec;
    private long init_budget;
    private long init_center_dep;
    private long plan_rev_min;
    private long plan_rev_sec;
    private long rev_cost;
    private long max_dep;
    private long interest_pct;
}
