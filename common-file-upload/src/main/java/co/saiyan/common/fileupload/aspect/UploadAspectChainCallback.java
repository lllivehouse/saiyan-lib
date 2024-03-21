package co.saiyan.common.fileupload.aspect;

import co.saiyan.common.fileupload.FileInfo;
import co.saiyan.common.fileupload.UploadPretreatment;
import co.saiyan.common.fileupload.platform.FileStorage;
import co.saiyan.common.fileupload.recorder.FileRecorder;

/**
 * 上传切面调用链结束回调
 */
public interface UploadAspectChainCallback {
    FileInfo run(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage, FileRecorder fileRecorder);
}
