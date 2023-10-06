package co.mgentertainment.common.fileupload.aspect;

import co.mgentertainment.common.fileupload.FileInfo;
import co.mgentertainment.common.fileupload.platform.FileStorage;

/**
 * 获取文件的访问控制列表调用链结束回调
 */
public interface SetFileAclAspectChainCallback {
    boolean run(FileInfo fileInfo, Object acl, FileStorage fileStorage);
}
