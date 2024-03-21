package co.saiyan.common.fileupload.aspect;

import co.saiyan.common.fileupload.FileInfo;
import co.saiyan.common.fileupload.platform.FileStorage;
import co.saiyan.common.fileupload.recorder.FileRecorder;

/**
 * 删除切面调用链结束回调
 */
public interface DeleteAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder);
}
