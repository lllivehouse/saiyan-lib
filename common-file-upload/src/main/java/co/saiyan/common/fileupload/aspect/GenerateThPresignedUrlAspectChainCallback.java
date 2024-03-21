package co.saiyan.common.fileupload.aspect;

import co.saiyan.common.fileupload.FileInfo;
import co.saiyan.common.fileupload.platform.FileStorage;

import java.util.Date;

/**
 * 对缩略图文件生成可以签名访问的 URL 切面调用链结束回调
 */
public interface GenerateThPresignedUrlAspectChainCallback {
    String run(FileInfo fileInfo, Date expiration, FileStorage fileStorage);
}
