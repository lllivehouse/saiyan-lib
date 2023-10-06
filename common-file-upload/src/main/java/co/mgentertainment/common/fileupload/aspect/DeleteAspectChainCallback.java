package co.mgentertainment.common.fileupload.aspect;

import co.mgentertainment.common.fileupload.FileInfo;
import co.mgentertainment.common.fileupload.platform.FileStorage;
import co.mgentertainment.common.fileupload.recorder.FileRecorder;

/**
 * 删除切面调用链结束回调
 */
public interface DeleteAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder);
}
