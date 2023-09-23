package co.mgentertainment.common.apiclient.auth;

import lombok.Builder;
import lombok.Data;

/**
 * @author larry
 * @createTime 2023/9/24
 * @description ApiToken
 */
@Data
@Builder
public class ApiToken {

    private String headerName;
    private String token;
}
