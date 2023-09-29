package co.mgentertainment.common.apiclient.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author larry
 * @createTime 2023/9/24
 * @description ApiToken
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiToken {

    private String headerName;
    private String token;
}
