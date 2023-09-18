package co.mgentertainment.common.apiclient.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author larry
 * @createTime 2023/2/2
 * @description MultipartFile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipartFile {

    private String key;

    private File file;

}